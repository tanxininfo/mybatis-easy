package com.mybatiseasy.core.session;

import com.mybatiseasy.core.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;

import java.util.*;

import static com.mybatiseasy.core.utils.TypeUtil.ArrayToDelimitedString;

@Slf4j
public class MyConfiguration extends Configuration {

    protected final Map<String, ResultMap> entityMaps;

    public MyConfiguration(Environment environment) {
        super(environment);
        entityMaps = new StrictMap<ResultMap>("Entity Maps collection");
    }

    public MyConfiguration() {
        entityMaps = new StrictMap<ResultMap>("Entity Maps collection");
    }

    public void addEntityMap(ResultMap rm) {
        resultMaps.put(rm.getId(), rm);
        checkLocallyForDiscriminatedNestedResultMaps(rm);
        checkGloballyForDiscriminatedNestedResultMaps(rm);
    }

    public Collection<String> getEntityMapNames() {
        return entityMaps.keySet();
    }

    public Collection<ResultMap> getEntityMaps() {
        return entityMaps.values();
    }

    public ResultMap getEntityMap(String id) {
        return entityMaps.get(id);
    }

    public boolean hasEntityMap(String id) {
        return entityMaps.containsKey(id);
    }

    /**
     * 生成数据表的ResultMap
     * @param msId MappedStatement.getId
     * @return ResultMap
     */
    private void buildResultMap(String msId) {
        String entityName = getEntityType(msId);
        log.info("entityName={}",entityName);
        if(entityName==null) return;
    }

    private void getEntityInfo(String entity){

    }

    private String getEntityType(String msId) {
        int dottedIndex = msId.lastIndexOf(".");
        String mapperName = msId.substring(0, dottedIndex);
        try {
            Class<?> mapperClass = Class.forName(mapperName);
            String classType = mapperClass.getGenericInterfaces()[0].getTypeName();
            return  classType.split("[<>]")[1];
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {

        log.info("ms={},{}", ms.getId(), ObjectUtil.toJsonString(ms.getResultMaps()));

        String keyProperty = ArrayToDelimitedString(ms.getKeyProperties());
        String keyColumn = ArrayToDelimitedString(ms.getKeyColumns());
        String resultSets = ArrayToDelimitedString(ms.getResultSets());
        List<ResultMap> resultMaps = new ArrayList<>();   //getStatementResultMaps(resultMap, resultType, id);
        buildResultMap(ms.getId());

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

        super.mappedStatements.put(statement.getId(), statement);
    }
}
