package com.mybatiseasy.core.session;

import com.mybatiseasy.core.consts.Method;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.enums.TableIdType;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
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
            ms = this.replaceKeyGeneratorMappedStatement(mapperName, ms);
        }

        super.addMappedStatement(ms);
    }

    private MappedStatement replaceKeyGeneratorMappedStatement(String mapperName, MappedStatement ms) {
        log.info("111");
        if(!ms.getKeyGenerator().equals(NoKeyGenerator.INSTANCE)){
            return ms;
        }
        log.info("222");

        String entityName = EntityMapKids.getEntityName(mapperName);
        if(entityName==null) return ms;
        log.info("333");

        EntityMap entityMap = EntityMapKids.getEntityMap(entityName);
        if(entityMap==null) return ms;
        log.info("444");

        EntityFieldMap primary = entityMap.getPrimary();
        if(primary == null) return ms;
        log.info("555");

        if(primary.getIdType().equals(TableIdType.NONE)) return ms;
        log.info("666");


        String keyProperty = MethodParam.ENTITY+"."+ primary.getName();
        String keyColumn = SqlUtil.removeBackquote(primary.getColumn());
        String resultSets = ArrayToDelimitedString(ms.getResultSets());

        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;

        if(primary.getIdType().equals(TableIdType.AUTO)){
            keyGenerator = Jdbc3KeyGenerator.INSTANCE;
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
