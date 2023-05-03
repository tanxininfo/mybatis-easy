/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
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

import java.sql.Statement;
import java.util.*;

import com.mybatiseasy.keygen.IKeyGenerator;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.core.session.EntityFieldMap;
import com.mybatiseasy.core.utils.CollectionUtil;
import com.mybatiseasy.core.utils.ObjectUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;

/**
 * 自定义前置生成主键
 */
public class CustomKeyGenerator implements KeyGenerator {

    private final EntityFieldMap fieldMap;

    private Executor keyExecutor;

    public CustomKeyGenerator(EntityFieldMap fieldMap) {
        this.fieldMap = fieldMap;;
    }

    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        processGeneratedKeys(executor, ms, parameter);

    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {

    }

    private void processGeneratedKeys(Executor executor, MappedStatement ms, Object parameter) {
        try {
            if (parameter != null && ms != null && ms.getKeyProperties() != null) {
                String[] keyProperties = ms.getKeyProperties();
                String keyProperty = keyProperties[0];
                String[] keyPath = keyProperty.split("\\.");
                String key = keyPath[0];
                String column = keyPath[1];

                final Configuration configuration = ms.getConfiguration();

                Class<? extends IKeyGenerator> clazz = this.fieldMap.getKeyGenerator();
                TableIdType idType = this.fieldMap.getIdType();

                IKeyGenerator generator;

                if(idType == TableIdType.SNOW_FLAKE) clazz = SnowFlakeKeyGenerator.class;
                else if(idType == TableIdType.UUID) clazz = UUIDKeyGenerator.class;
                else if(idType == TableIdType.ID_MAKER) clazz = IdMakerKeyGenerator.class;

                if(idType == TableIdType.SEQUENCE){
                    this.keyExecutor = ms.getConfiguration().newExecutor(executor.getTransaction(), ExecutorType.SIMPLE);
                    generator = new SelectKeyGenerator(ms.getConfiguration(),
                            keyExecutor,
                            parameter,
                            ms.getId() + org.apache.ibatis.executor.keygen.SelectKeyGenerator.SELECT_KEY_SUFFIX,
                            column
                            );
                }
                else{
                    generator = KeyGeneratorFactory.getInstance(clazz);
                }

                Map<String, Object> map = ObjectUtil.beanToMap(parameter, false, false);
                Object entityOrList = map.get(key);
                if(Objects.equals(key, MethodParam.ENTITY)){
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
