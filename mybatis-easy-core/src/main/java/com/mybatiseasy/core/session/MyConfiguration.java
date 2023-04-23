package com.mybatiseasy.core.session;

import com.mybatiseasy.core.consts.Sql;
import com.mybatiseasy.core.paginate.Total;
import com.mybatiseasy.core.utils.SqlUtil;
import com.mybatiseasy.core.utils.StringUtil;
import com.mybatiseasy.core.utils.TypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.*;

import static com.mybatiseasy.core.utils.TypeUtil.ArrayToDelimitedString;

@Slf4j
public class MyConfiguration extends Configuration {

    public MyConfiguration(Environment environment) {
        super(environment);
        this.addTotalResultMap();
    }

    public MyConfiguration() {
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
        String[] methods = {"getById", "getByCondition", "listByCondition", "listByWrapper", "queryEasy"};


        if(Arrays.asList(methods).contains(methodName)){
            this.replaceMappedStatement(mapperName, methodName, ms);
            return;
        }

        super.addMappedStatement(ms);
    }

    private void replaceMappedStatement(String mapperName, String methodName, MappedStatement ms){
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

        MappedStatement statement = statementBuilder.build();
        super.addMappedStatement(statement);
    }
}
