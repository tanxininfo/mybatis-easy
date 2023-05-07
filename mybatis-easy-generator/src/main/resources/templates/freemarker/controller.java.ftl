package ${global.packageName}.${controller.packageName};

<#if controller.swagger>
import io.swagger.annotations.ApiOperation;
</#if>
import com.mybatiseasy.core.paginate.Page;
import com.mybatiseasy.core.paginate.PageList;
import info.tanxin.system.common.annotation.ParamConvert;
import info.tanxin.system.common.api.AddResult;
import info.tanxin.system.common.api.DataDetail;
import info.tanxin.system.common.api.Response;
import info.tanxin.system.common.api.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.annotation.Validated;

import ${global.packageName}.${entity.packageName}.${table.name?cap_first}${entity.suffix};
import ${global.packageName}.${dto.packageName}.${table.name?cap_first}${dto.suffix};
import ${global.packageName}.${service.packageName}.${table.name?cap_first}${service.suffix};

<#assign controllerClassName="${table.name?cap_first}${controller.suffix}" />
<#assign entityClassName="${table.name?cap_first}${entity.suffix}" />
<#assign dtoClassName="${table.name?cap_first}${dto.suffix}" />
<#assign serviceClassName="${table.name?cap_first}${service.suffix}" />
<#assign serviceImplClassName="${table.name?cap_first}${serviceImpl.suffix}" />
<#assign priColumnName="${table.priColumn.name}"/>
<#assign priColumnType="${table.priColumn.javaTypeName}"/>
<#------------  END 字段循环遍历  ---------->
<#if controller.restful??>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if controller.superClass??>
import ${controller.superClass};
</#if>
/**
* @author ${global.author!}
* @since ${global.commentDate!.now?string("yyyy-MM-dd")}
*/
<#if controller.restful??>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/${entityClassName?uncap_first}")
<#if controller.supperClass??>
public class ${controllerClassName} extends ${controller.supperClass} {
<#else>
public class ${controllerClassName} {
</#if>

    @Autowired
    private ${entityClassName}Service ${entityClassName?uncap_first}Service;

    @GetMapping("/{${priColumnName}}")
    <#if controller.swagger>
    @ApiOperation("${entityClassName}查询")
    </#if>
    public Response<DataDetail<${entityClassName}>> get(@PathVariable("${priColumnName}") ${priColumnType} ${priColumnName}){
        ${entityClassName} ${entityClassName?uncap_first} = ${serviceClassName?uncap_first}.get(${priColumnName});
        return ${entityClassName?uncap_first} == null? Response.dataNotFound(): Response.success(new DataDetail<>(${entityClassName?uncap_first}));
    }

    @GetMapping("/page")
    <#if controller.swagger>
    @ApiOperation("${entityClassName}分页查询")
    </#if>
    public Response<PageList<${entityClassName}>> page(@ParamConvert ${entityClassName}Dto ${entityClassName?uncap_first}Dto, Sort sort, Page page){
        PageList<${entityClassName}> list = ${entityClassName?uncap_first}Service.page(${entityClassName?uncap_first}Dto, sort, page);
        return Response.success(list);
    }

    @PostMapping
    <#if controller.swagger>
    @ApiOperation("${table.comment}新增")
    </#if>
    public Response<AddResult> create(@Validated @RequestBody ${entityClassName} ${entityClassName?uncap_first}) {
        return Response.success(new AddResult(${entityClassName?uncap_first}Service.create(${entityClassName?uncap_first})));
    }

    @PutMapping("/{${priColumnName}}")
    <#if controller.swagger>
    @ApiOperation("${table.comment}修改")
    </#if>
    public Response<Object> update(@PathVariable("${priColumnName}") ${priColumnType} ${priColumnName}, @Validated @RequestBody ${entityClassName} ${entityClassName?uncap_first}) {
        ${entityClassName?uncap_first}Service.update(${priColumnName}, ${entityClassName?uncap_first});
        return Response.success();
    }

    @DeleteMapping("/{${priColumnName}}")
    <#if controller.swagger>
    @ApiOperation("${table.comment}删除")
    </#if>
    public Response<Object> remove(@PathVariable("${priColumnName}") ${priColumnType} ${priColumnName}) {
        ${entityClassName?uncap_first}Service.remove(${priColumnName});
        return Response.success();
    }
}
