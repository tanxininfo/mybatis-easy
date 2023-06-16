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
import com.mybatiseasy.core.keygen.RecordKeyGenerator;
import com.mybatiseasy.core.typehandler.*;
import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.core.keygen.CustomKeyGenerator;
import com.mybatiseasy.core.utils.SqlUtil;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

import org.apache.ibatis.type.UnknownTypeHandler;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.mybatiseasy.core.utils.TypeUtil.ArrayToDelimitedString;

public class MeConfiguration extends Configuration {

    public MeConfiguration(Environment environment) {
        super(environment);
        this.init();
    }

    public MeConfiguration() {
        this.init();
    }

    public Map<String, String> entityMapperMap = new HashMap<>();


    public void init(){
        this.addTotalResultMap();

        /**
         * 在sqlite中不支持数据类型:LocalDateTime, LocalDate, LocalTime
         */
        this.typeHandlerRegistry.register(LocalDateTime.class, LocalDateTimeTypeHandler.class);
        this.typeHandlerRegistry.register(LocalDate.class, LocalDateTypeHandler.class);
        this.typeHandlerRegistry.register(LocalTime.class, LocalTimeTypeHandler.class);
    }

    /**
     * 生成数据表的ResultMap
     * @param mapperName mapperName
     */
    private void buildResultMap(String mapperName) {
        if(hasResultMap(mapperName)) return;

        String entityName = EntityKids.getEntityName(mapperName);
        if(entityName==null) return;

        Entity entity = EntityKids.getEntityMap(entityName);
        if(entity ==null) return;

        try {
            List<ResultMapping> resultMappingList = new ArrayList<>();
            for (EntityField fieldMap: entity.getEntityFieldMapList()
                 ) {
                ResultMapping.Builder resultMapping = new ResultMapping.Builder(this, fieldMap.getName(), SqlUtil.removeBackquote(fieldMap.getColumn()), fieldMap.getJavaType());
                Class<? extends TypeHandler> typeHandlerClass = fieldMap.getTypeHandler();
                Class<?> javaType = fieldMap.getJavaType();

                if(typeHandlerClass == null || typeHandlerClass.equals(UnknownTypeHandler.class)){
                    if(javaType.getName().equals("java.util.Map")) typeHandlerClass = MapTypeHandler.class;
                    else if(javaType.getName().equals("java.util.List")) typeHandlerClass = ListMapTypeHandler.class;
                }

                if (typeHandlerClass != null && typeHandlerClass != UnknownTypeHandler.class) {
                    TypeHandler<?> typeHandler = typeHandlerRegistry.getInstance(fieldMap.getJavaType(), typeHandlerClass);
                    resultMapping.typeHandler(typeHandler);
                }

                if(fieldMap.isId()) resultMapping.flags(List.of(ResultFlag.ID));
                resultMappingList.add(resultMapping.build());
            }
             ResultMap newMap = new ResultMap.Builder(this, mapperName, Class.forName(entityName), resultMappingList).build();
            addResultMap(newMap);
        }catch (Exception ex){
            throw new RuntimeException("ResultMap format error:" + ex.getMessage());
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
            throw new RuntimeException("ResultMap format error:" + ex.getMessage());
        }
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {
        int dottedIndex = ms.getId().lastIndexOf(".");
        String mapperName = ms.getId().substring(0, dottedIndex);
        String methodName = ms.getId().substring(dottedIndex + 1);
        String[] selectMethods = {Method.GET_BY_ID, Method.GET_BY_CONDITION, Method.GET_BY_WRAPPER, Method.LIST_BY_CONDITION, Method.LIST_BY_WRAPPER, "queryEasy"};
        String[] insertMethods = {Method.INSERT, Method.INSERT_BATCH};

        if (Arrays.asList(selectMethods).contains(methodName)) {
            ms = this.replaceResultMapOfMappedStatement(mapperName, methodName, ms);
        } else if (Arrays.asList(insertMethods).contains(methodName)) {
            ms = this.replaceKeyGeneratorMappedStatement(mapperName, methodName, ms);
        }

        String entityName = EntityKids.getEntityName(mapperName);
        if (!entityMapperMap.containsKey(entityName)) {
            entityMapperMap.put(entityName, mapperName);
        }

        super.addMappedStatement(ms);
    }

    private MappedStatement replaceKeyGeneratorMappedStatement(String mapperName, String methodName, MappedStatement ms) {
        if(!ms.getKeyGenerator().equals(NoKeyGenerator.INSTANCE)){
            return ms;
        }

        boolean isDbMapper = mapperName.contains(".DbMapper");

        TableIdType primaryType = null;
        EntityField primary = null;
        String keyProperty = "";
        String keyColumn = "";
        boolean isBatch = methodName.equals(Method.INSERT_BATCH);

        if(!isDbMapper) {
            String entityName = EntityKids.getEntityName(mapperName);
            if (entityName == null) return ms;

            Entity entity = EntityKids.getEntityMap(entityName);
            if (entity == null) return ms;

            primary = entity.getPrimaryFieldMap();
            if (primary == null) return ms;

            primaryType = primary.getIdType();

            if(primaryType == TableIdType.NONE) return ms;

            keyProperty = (isBatch?MethodParam.ENTITY_LIST:MethodParam.ENTITY) +"."+ primary.getName();
            keyColumn = SqlUtil.removeBackquote(primary.getColumn());
        }else{
            keyProperty = ArrayToDelimitedString(ms.getKeyProperties());
            keyColumn = ArrayToDelimitedString(ms.getKeyColumns());
        }

        String resultSets = ArrayToDelimitedString(ms.getResultSets());

        KeyGenerator keyGenerator;
        if(isDbMapper){
            keyGenerator = RecordKeyGenerator.INSTANCE;
        }
        else if(primaryType == TableIdType.AUTO){
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

    private void replaceSequenceMappedStatement(EntityField primary, String keyProperty, String keyColumn, String baseStatementId, MappedStatement ms){

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
                    @Serial
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
    }

    private MappedStatement replaceResultMapOfMappedStatement(String mapperName, String methodName, MappedStatement ms){
        String keyProperty = ArrayToDelimitedString(ms.getKeyProperties());
        String keyColumn = ArrayToDelimitedString(ms.getKeyColumns());
        String resultSets = ArrayToDelimitedString(ms.getResultSets());

        if(!mapperName.contains(".DbMapper")) {
            buildResultMap(mapperName);
        }
        List<ResultMap> resultMaps = new ArrayList<>(hasResultMap(mapperName) ? List.of(getResultMap(mapperName)) : ms.getResultMaps());

        if(methodName.equals("queryEasy")){
            ResultMap map = getResultMap(Sql.TOTAL_CLASS_NAME);
            if(resultMaps.size() > 0 && map != null) resultMaps.add(map);
        }

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), ms.getSqlSource(), ms.getSqlCommandType())
                .resource(ms.getResource())
                .fetchSize(ms.getFetchSize())
                .timeout(ms.getTimeout())
                .statementType(ms.getStatementType())
                .keyGenerator(ms.getKeyGenerator())
                .keyProperty(keyProperty)
                .keyColumn(keyColumn)
                .databaseId(databaseId)
                .lang(ms.getLang())
                .resultOrdered(ms.isResultOrdered())
                .resultSets(resultSets)
                .resultMaps(resultMaps)
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
}
