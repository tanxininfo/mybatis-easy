/*
 * Copyright (c) 2023, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.mybatiseasy.core.keygen;

import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.session.EntityField;
import com.mybatiseasy.core.session.Entity;
import com.mybatiseasy.core.session.EntityKids;
import com.mybatiseasy.core.session.MeConfiguration;
import com.mybatiseasy.core.utils.CollectionUtil;
import com.mybatiseasy.core.utils.SqlUtil;
import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.keygen.IKeyGenerator;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ExecutorType;

import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Record记录主键
 */
public class RecordKeyGenerator extends Jdbc3KeyGenerator {

    public static final RecordKeyGenerator INSTANCE = new RecordKeyGenerator();


    private Executor keyExecutor;

    public RecordKeyGenerator() {
    }

    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        processGeneratedKeys(executor, ms, stmt, parameter, true);

    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        processGeneratedKeys(executor, ms, stmt, parameter, false);
    }

    @SuppressWarnings("unchecked")
    private void processGeneratedKeys(Executor executor, MappedStatement ms, Statement stmt, Object parameter, boolean isBefore) {
        try {
            if (parameter != null && ms != null) {

                Map<String, Object> map = (Map<String, Object>) parameter;
                Class<?> entityClass = (Class<?>)map.get(MethodParam.ENTITY_CLASS);

                Entity entityMap = EntityKids.getEntityMap(entityClass.getName());
                if (entityMap == null) return;

                EntityField primary = entityMap.getPrimaryFieldMap();
                if (primary == null) return;
                String column = SqlUtil.removeBackquote(primary.getColumn());

                TableIdType idType = primary.getIdType();
                Class<? extends IKeyGenerator> clazz = primary.getKeyGenerator();

                final MeConfiguration configuration = (MeConfiguration) ms.getConfiguration();
                String suffix = (ms.getId().contains("insertBatch")?".insertBatch":".insert");

                IKeyGenerator generator;
                if(idType == TableIdType.NONE) return;
                else if(idType == TableIdType.AUTO) {
                    if(isBefore) return;
                    String newMsId = configuration.entityMapperMap.get(entityMap.getFullName())+ suffix;
                    RecordJdbc3KeyGenerator.INSTANCE.processAfter(executor, configuration.getMappedStatement(newMsId), stmt, parameter);
                    return;
                }
                else if(idType == TableIdType.SNOW_FLAKE) clazz = SnowFlakeKeyGenerator.class;
                else if(idType == TableIdType.UUID) clazz = UUIDKeyGenerator.class;
                else if(idType == TableIdType.ID_MAKER) clazz = IdMakerKeyGenerator.class;

                if(idType == TableIdType.SEQUENCE){
                    String newMsId = configuration.entityMapperMap.get(entityMap.getFullName())+ suffix;
                    this.keyExecutor = ms.getConfiguration().newExecutor(executor.getTransaction(), ExecutorType.SIMPLE);
                    generator = new SelectKeyGenerator(ms.getConfiguration(),
                            keyExecutor,
                            parameter,
                            newMsId + org.apache.ibatis.executor.keygen.SelectKeyGenerator.SELECT_KEY_SUFFIX,
                            column
                            );
                }
                else{
                    generator = KeyGeneratorFactory.getInstance(clazz);
                }
                String key = (ms.getId().contains("insertBatch")?"recordList":"record");

                Object entityOrList = map.get(key);
                if(Objects.equals(key, MethodParam.RECORD)){
                    MetaObject metaParam = configuration.newMetaObject(entityOrList);
                    Object nextId = generator.generateId();
                    setValue(metaParam, column, nextId);
                }
                else{
                    Collection<?> list = CollectionUtil.collectionize(entityOrList);
                    for (Object entity: list
                         ) {
                        MetaObject metaParam = configuration.newMetaObject(entity);
                        Object nextId = generator.generateId();
                        setValue(metaParam, column, nextId);
                    }
                }
            }
        } catch (ExecutorException e) {
            throw e;
        } catch (Exception e) {
            throw new ExecutorException("Error generating key or setting result to parameter object. Cause: " + e, e);
        }
    }

    private void setValue(MetaObject metaParam, String property, Object value) {
        if (!metaParam.hasSetter(property)) {
            throw new ExecutorException("No setter found for the keyProperty '" + property + "' in "
                    + metaParam.getOriginalObject().getClass().getName() + ".");
        }
        metaParam.setValue(property, value);
    }
}
