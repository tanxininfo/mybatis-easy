package ${package.ServiceImpl};

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.*;
import info.tanxin.system.common.cache.CacheManager;
import info.tanxin.system.common.exception.TanXinException;
import info.tanxin.system.common.util.JetCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;


import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import ${package.Service?replace(".core.service", "")}.common.util.SortUtil;
import ${package.Service?replace(".core.service", "")}.common.util.WrapperUtil;

import ${package.Service?replace(".core.service", "")}.common.api.*;
import ${package.Service?replace(".service", "")}.dto.${entity}Dto;

import org.springframework.stereotype.Service;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
        <#assign keyPropertyType="${field.propertyType}"/>
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
/**
* ${table.comment!} 服务实现类
*
* @author ${author}
* @since ${date}
*/
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {


    @Autowired
    private CacheManager cacheManager;

    @CreateCache(name="${entity}Service.page.", expire = 3600)
    private Cache<String, PageList<${entity}>> cache;

    /**
    * ${table.comment!}查询
    * @param ${keyPropertyName} 主键
    * @return 结果
    */
    @Cached(name="${entity}Service.", key = "#${keyPropertyName}",  cacheType = CacheType.REMOTE)
    public ${entity} get(${keyPropertyType} ${keyPropertyName}) {
        return getById(${keyPropertyName});
    }

    /**
    * ${table.comment!}分页查询
    * @param ${entity?uncap_first}Dto 查询条件
    * @param sort 排序
    * @param page 分页
    * @return 结果
    */
    public PageList<${entity}> page(${entity}Dto ${entity?uncap_first}Dto, Sort sort, Page<${entity}> page) {
        QueryWrapper<${entity}> wrapper = new QueryWrapper<>();
        Sort formattedSort = SortUtil.format(sort, "${keyPropertyName}");
        wrapper.orderBy(true, formattedSort.isAsc(), formattedSort.getColumn());

        String key = JetCacheUtil.generateKey(wrapper, page);
        PageList<${entity}> list = cache.get(key);
        if(list!= null) return list;

        IPage<${entity}> iPage = page(page, wrapper);
        list = new PageList<>(iPage);
        cache.put(key, list);
        return list;
    }

    /**
    * ${table.comment!}添加
    * @param ${entity?uncap_first} ${table.comment!}信息
    * @return 结果
    */
    public Long create(${entity} ${entity?uncap_first}) {
        boolean bResult = save(${entity?uncap_first});

        removeListCache();

        if(bResult) {
            return ${entity?uncap_first}.get${keyPropertyName?cap_first}();
        }
        else throw new TanXinException();
    }

    /**
    * ${table.comment!}修改
    * @param ${keyPropertyName}
    * @param ${entity?uncap_first} ${table.comment!}信息
    * @return 结果
    */
    @CacheInvalidate(name="${entity}Service.", key="#${keyPropertyName}")
    public boolean update(${keyPropertyType} ${keyPropertyName}, ${entity} ${entity?uncap_first}) {
        ${entity?uncap_first}.set${keyPropertyName?cap_first}(${keyPropertyName});
        ${entity} old${entity} = getById(${keyPropertyName});
        if(old${entity} == null) throw new TanXinException("记录未找到");
        <#if config.strategyConfig.entity().versionColumnName != '' && table.fieldNames?contains(config.strategyConfig.entity().versionColumnName) >
        ${entity?uncap_first}.setVersion(old${entity}.getVersion());
        </#if>

        removeListCache();

        return updateById(${entity?uncap_first});
    }


    private void removeListCache(){
        cacheManager.removeAll("${entity}Service.page.*");
    }

    /**
    * ${table.comment!}删除
    * @param ${keyPropertyName} ${table.comment!}id
    * @return 结果
    */
    @CacheInvalidate(name="${entity}Service.", key="#${keyPropertyName}")
    public boolean remove(${keyPropertyType} ${keyPropertyName}) {
        ${entity} ${entity?uncap_first} = getById(${keyPropertyName});
        if(${entity?uncap_first} == null) return true;

        removeListCache();

        return removeById(${keyPropertyName});
    }
}
</#if>
