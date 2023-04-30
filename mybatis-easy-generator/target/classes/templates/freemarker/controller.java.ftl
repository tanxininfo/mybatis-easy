package ${package.Controller};

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import ${package.Service?replace(".core.service", "")}.common.api.*;
import ${package.Service?replace(".service", "")}.dto.${table.entityName}Dto;
import ${package.Service?replace(".core.service", "")}.common.annotation.ParamConvert;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
        <#assign keyPropertyType="${field.propertyType}"/>
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
<#if restControllerStyle>
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
/**
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/core/${table.entityName?uncap_first}")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.entityName}Service ${table.entityName?uncap_first}Service;

    @GetMapping("/{${keyPropertyName}}")
    @ApiOperation("${table.entityName}查询")
    public Response<DataDetail<${table.entityName}>> get(@PathVariable("${keyPropertyName}") ${keyPropertyType} ${keyPropertyName}){
        ${table.entityName} ${table.entityName?uncap_first} = ${table.entityName?uncap_first}Service.get(${keyPropertyName});
        return ${table.entityName?uncap_first} == null? Response.dataNotFound(): Response.success(new DataDetail<>(${table.entityName?uncap_first}));
    }

    @GetMapping("/page")
    @ApiOperation("${table.entityName}分页查询")
    public Response<PageList<${table.entityName}>> page(@ParamConvert ${table.entityName}Dto ${table.entityName?uncap_first}Dto, Sort sort, Page<${table.entityName}> page){
        PageList<${table.entityName}> list = ${table.entityName?uncap_first}Service.page(${table.entityName?uncap_first}Dto, sort, page);
        return Response.success(list);
    }

    @PostMapping
    @ApiOperation("${table.comment}新增")
    public Response<AddResult> create(@Validated @RequestBody ${table.entityName} ${table.entityName?uncap_first}) {
        return Response.success(new AddResult(${table.entityName?uncap_first}Service.create(${table.entityName?uncap_first})));
    }

    @PutMapping("/{${keyPropertyName}}")
    @ApiOperation("${table.comment}修改")
    public Response<Object> update(@PathVariable("${keyPropertyName}") ${keyPropertyType} ${keyPropertyName}, @Validated @RequestBody ${table.entityName} ${table.entityName?uncap_first}) {
        boolean result = ${table.entityName?uncap_first}Service.update(${keyPropertyName}, ${table.entityName?uncap_first});
        return result? Response.success(): Response.fail();
    }

    @DeleteMapping("/{${keyPropertyName}}")
    @ApiOperation("${table.comment}删除")
    public Response<Object> remove(@PathVariable("${keyPropertyName}") ${keyPropertyType} ${keyPropertyName}) {
        boolean result = ${table.entityName?uncap_first}Service.remove(${keyPropertyName});
        return result? Response.success(): Response.fail();
    }
}
</#if>
