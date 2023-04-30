package ${global.packageName}.${dto.packageName};

<#if dto.swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>

<#if dto.enableLombok>
import lombok.Data;
<#if dto.chain>
import lombok.experimental.Accessors;
</#if>
</#if>

/**
 *
 * ${table.comment!} ${table.name} Dto对象
 *
 *
* @author ${global.author!}
 * @since ${global.commentDate!.now?string("yyyy-MM-dd")}
 */
<#if dto.enableLombok>
@Data
<#if dto.chain>
@Accessors(chain = true)
</#if>
</#if>
<#if dto.swagger>
@ApiModel(value = "${table.name} Dto对象"<#if table.comment!?length gt 0>, description = "${table.comment!}"</#if>)
</#if>
public class ${table.name?cap_first}${dto.suffix} {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.columns as column>
    <#if column.comment!?length gt 0>
    <#if dto.swagger>
    @ApiModelProperty("${column.comment}")
    <#else>
    /**
    * ${column.comment}
    */
    </#if>
    </#if>
    private ${column.javaTypeName} ${column.name};

</#list>
<#------------  END 字段循环遍历  ---------->

<#if !dto.enableLombok>
    <#list table.columns as column>
    <#if column.javaTypeName == "Boolean">
        <#assign prefix="is"/>
    <#else>
        <#assign prefix="get"/>
    </#if>
    public ${column.javaTypeName} ${prefix}${column.name?cap_first}() {
        return ${column.name};
    }

    <#if dto.enableLombok && dto.chain>
    public ${table.name} set${column.name?cap_first}(${column.javaTypeName} ${column.name}) {
    <#else>
    public void set${column.name?cap_first}(${column.javaTypeName} ${column.name}) {
    </#if>
        this.${column.name} = ${column.name};
    <#if dto.enableLombok && dto.chain>
        return this;
    </#if>
    }
        
    </#list>
</#if>
}
