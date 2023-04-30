package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import ${package.Service?replace(".core.service", "")}.common.api.*;
import ${package.Service?replace(".service", "")}.dto.${table.entityName}Dto;
/**
* @author ${author}
* @since ${date}
*/
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
        <#assign keyPropertyType="${field.propertyType}"/>
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
@Transactional
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
    * ${table.entityName}详情
    * @param ${keyPropertyName} 主键
    * @return 结果
    */
    ${table.entityName} get(${keyPropertyType} ${keyPropertyName});

    /**
    * ${table.entityName}分页查询
    * @param ${table.entityName?uncap_first}Dto 查询条件
    * @param sort 排序
    * @param page 分页
    * @return 结果
    */
    PageList<${table.entityName}> page(${table.entityName}Dto ${table.entityName?uncap_first}Dto, Sort sort, Page<${table.entityName}> page);

    /**
    * ${table.entityName}新增
    * @param ${table.entityName?uncap_first} ${table.comment}信息
    * @return 结果
    */
    Long create(${table.entityName} ${table.entityName?uncap_first});

    /**
    * ${table.entityName}修改
    * @param ${table.entityName?uncap_first} ${table.comment}信息
    * @return 结果
    */
    boolean update(${keyPropertyType} ${keyPropertyName}, ${table.entityName} ${table.entityName?uncap_first});

    /**
    * ${table.entityName}删除
    * @param ${keyPropertyName} ${table.comment}id
    * @return 结果
    */
    boolean remove(${keyPropertyType} ${keyPropertyName});

}

</#if>

