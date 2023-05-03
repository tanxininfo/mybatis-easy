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

package com.mybatiseasy.core.session;

import com.mybatiseasy.core.consts.Method;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.core.keygen.CustomKeyGenerator;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mybatiseasy.core.utils.TypeUtil.ArrayToDelimitedString;

@Slf4j
public class MeConfiguration extends Configuration {

    public MeConfiguration(Environment environment) {
        super(environment);
        this.addTotalResultMap();
    }

    public MeConfiguration() {
        this.addTotalResultMap();
    }

    /**
     * 生成数据表的ResultMap
     * @param mapperName mapperName
     */
    private void buildResultMap(String mapperName) {
        if(hasResultMap(mapperName)) return;

        String entityName = EntityMapKids.getEntityName(mapperName);
        if(entityName==null) return;

        EntityMap entityMap = EntityMapKids.getEntityMap(entityName);
        if(entityMap==null) return;

        try {
            List<ResultMapping> resultMappingList = new ArrayList<>();
            for (EntityFieldMap fieldMap: entityMap.getEntityFieldMapList()
                 ) {
                ResultMapping.Builder resultMapping = new ResultMapping.Builder(this, fieldMap.getName(), SqlUtil.removeBackquote(fieldMap.getColumn()), fieldMap.getJavaType());
                Class<? extends TypeHandler> typeHandlerClass = fieldMap.getTypeHandler();

                if (typeHandlerClass != null && typeHandlerClass != UnknownTypeHandler.class) {
                    TypeHandlerRegistry typeHandlerRegistry = getTypeHandlerRegistry();
                    TypeHandler<?> typeHandler = typeHandlerRegistry.getInstance(fieldMap.getJavaType(), fieldMap.getTypeHandler());
                    resultMapping.typeHandler(typeHandler);
                }

                if(fieldMap.getIsId()) resultMapping.flags(List.of(ResultFlag.ID));
                resultMappingList.add(resultMapping.build());
            }
             ResultMap newMap = new ResultMap.Builder(this, mapperName, Class.forName(entityName), resultMappingList).build();
            addResultMap(newMap);
        }catch (Exception ex){
            log.error("ResultMap format error:{}",ex.getMessage());
        }
    }

    /**
     * 添加记录总数的ResultMap
     */
    private void addTotalResultMap() {
        String mapperName = Sql.TOTAL_CLASS_NAME;
        if(hasResultMap(mapperName)) return;

        try {
            List<ResultMapping> resultMappingList = new ArrayList<>();

            ResultMapping.Builder resultMapping = new ResultMapping.Builder(this, "total", "total", Long.class);
            resultMappingList.add(resultMapping.build());

            ResultMap newMap = new ResultMap.Builder(this, mapperName, Class.forName(mapperName), resultMappingList).build();
            addResultMap(newMap);
        }catch (Exception ex){
            log.error("ResultMap format error:{}",ex.getMessage());
        }
    }


    @Override
    public void addMappedStatement(MappedStatement ms) {
        int dottedIndex = ms.getId().lastIndexOf(".");
        String mapperName = ms.getId().substring(0, dottedIndex);
        String methodName = ms.getId().substring(dottedIndex + 1);
        String[] selectMethods = {Method.GET_BY_ID, Method.GET_BY_CONDITION, Method.GET_BY_WRAPPER, Method.LIST_BY_CONDITION, Method.LIST_BY_WRAPPER, "queryEasy"};
        String[] insertMethods = {Method.INSERT, Method.INSERT_BATCH};

        if(Arrays.asList(selectMethods).contains(methodName)){
            ms = this.replaceResultMapOfMappedStatement(mapperName, methodName, ms);
        }else if(Arrays.asList(insertMethods).contains(methodName)){
            ms = this.replaceKeyGeneratorMappedStatement(mapperName, methodName, ms);
        }

        super.addMappedStatement(ms);
    }

    private MappedStatement replaceKeyGeneratorMappedStatement(String mapperName, String methodName, MappedStatement ms) {
        if(!ms.getKeyGenerator().equals(NoKeyGenerator.INSTANCE)){
            return ms;
        }
        String entityName = EntityMapKids.getEntityName(mapperName);
        if(entityName==null) return ms;

        EntityMap entityMap = EntityMapKids.getEntityMap(entityName);
        if(entityMap==null) return ms;

        EntityFieldMap primary = entityMap.getPrimary();
        if(primary == null) return ms;

        TableIdType primaryType = primary.getIdType();

        if(primaryType == TableIdType.NONE) return ms;

        boolean isBatch = methodName.equals(Method.INSERT_BATCH);

        String keyProperty = (isBatch?MethodParam.ENTITY_LIST:MethodParam.ENTITY) +"."+ primary.getName();
        String keyColumn = SqlUtil.removeBackquote(primary.getColumn());
        String resultSets = ArrayToDelimitedString(ms.getResultSets());

        KeyGenerator keyGenerator;

        if(primaryType == TableIdType.AUTO){
            keyGenerator = Jdbc3KeyGenerator.INSTANCE;
        }
        else if(primaryType == TableIdType.SEQUENCE){
            replaceSequenceMappedStatement(primary, keyProperty, keyColumn, ms.getId(), ms);
            keyGenerator = new CustomKeyGenerator(primary);
        }
        else {
            keyGenerator = new CustomKeyGenerator(primary);
        }

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), ms.getSqlSource(), ms.getSqlCommandType())
                .resource(ms.getResource())
                .fetchSize(ms.getFetchSize())
                .timeout(ms.getTimeout())
                .statementType(ms.getStatementType())
                .keyGenerator(keyGenerator)
                .keyProperty(keyProperty)
                .keyColumn(keyColumn)
                .databaseId(databaseId)
                .lang(ms.getLang())
                .resultOrdered(ms.isResultOrdered())
                .resultSets(resultSets)
                .resultMaps(ms.getResultMaps())
                .resultSetType(ms.getResultSetType())
                .flushCacheRequired(ms.isFlushCacheRequired())
                .useCache(ms.isUseCache())
                .cache(ms.getCache());

        ParameterMap statementParameterMap = ms.getParameterMap();
        if (statementParameterMap != null) {
            statementBuilder.parameterMap(statementParameterMap);
        }

        return statementBuilder.build();
    }

    private void replaceSequenceMappedStatement(EntityFieldMap primary,String keyProperty, String keyColumn, String baseStatementId, MappedStatement ms){

        String id = baseStatementId + SelectKeyGenerator.SELECT_KEY_SUFFIX;

        // defaults
        boolean useCache = false;
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        Integer fetchSize = null;
        Integer timeout = null;
        String databaseId = ms.getDatabaseId();

        SqlSource sqlSource = ms.getLang().createSqlSource(ms.getConfiguration(), primary.getSequence(), primary.getJavaType());
        SqlCommandType sqlCommandType = SqlCommandType.SELECT;

        final ResultMap rm = new ResultMap.Builder(ms.getConfiguration(), "keyResultMap", primary.getJavaType(), new ArrayList<>()).build();

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), id, sqlSource, sqlCommandType)
                .resource(ms.getResource())
                .fetchSize(fetchSize)
                .timeout(timeout)
                .statementType(ms.getStatementType())
                .keyGenerator(keyGenerator)
                .keyProperty(keyProperty)
                .keyColumn(keyColumn)
                .databaseId(databaseId)
                .lang(ms.getLang())
                .resultOrdered(ms.isResultOrdered())
                .resultSets(null)
                .resultMaps(new ArrayList<ResultMap>() {
                    private static final long serialVersionUID = 1L;
                    {
                        add(rm);
                    }
                })
                .resultSetType(ms.getResultSetType())
                .flushCacheRequired(false)
                .useCache(useCache)
                .cache(ms.getCache());
        addMappedStatement(statementBuilder.build());
        //return new SelectKeyGenerator(statementBuilder.build(), true);
    }

    private MappedStatement replaceResultMapOfMappedStatement(String mapperName, String methodName, MappedStatement ms){
        String keyProperty = ArrayToDelimitedString(ms.getKeyProperties());
        String keyColumn = ArrayToDelimitedString(ms.getKeyColumns());
        String resultSets = ArrayToDelimitedString(ms.getResultSets());

        buildResultMap(mapperName);
        List<ResultMap> resultMaps = new ArrayList<>(hasResultMap(mapperName) ? List.of(getResultMap(mapperName)) : new ArrayList<>());

        if(methodName.equals("queryEasy")){
            ResultMap map = getResultMap(Sql.TOTAL_CLASS_NAME);
            if(resultMaps.size() > 0 && map != null) resultMaps.add(map);
        }

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), ms.getSqlSource(), ms.getSqlCommandType())
                .resource(ms.getResource()).fetchSize(ms.getFetchSize()).timeout(ms.getTimeout()).statementType(ms.getStatementType())
                .keyGenerator(ms.getKeyGenerator()).keyProperty(keyProperty).keyColumn(keyColumn).databaseId(databaseId).lang(ms.getLang())
                .resultOrdered(ms.isResultOrdered()).resultSets(resultSets)
                .resultMaps(resultMaps).resultSetType(ms.getResultSetType())
                .flushCacheRequired(ms.isFlushCacheRequired()).useCache(ms.isUseCache()).cache(ms.getCache());

        ParameterMap statementParameterMap = ms.getParameterMap();
        if (statementParameterMap != null) {
            statementBuilder.parameterMap(statementParameterMap);
        }

        return statementBuilder.build();
    }
}
