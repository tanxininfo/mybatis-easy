package ${global.packageName}.${dto.packageName};

<#if dto.swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>

import java.io.Serializable;
import java.io.Serial;

<#if dto.enableLombok>
import lombok.Data;
<#if dto.chain>
import lombok.experimental.Accessors;
</#if>
</#if>

<#-- ----------  字段循环遍历 开始  ---------->
<#list table.columns as column>
<#if column.javaTypeName == "LocalDateTime">
import java.time.LocalDateTime;
<#break>
</#if>
</#list>
<#------------  字段循环遍历 结束  ---------->
<#-- ----------  字段循环遍历 开始  ---------->
<#list table.columns as column>
<#if column.javaTypeName == "LocalDate">
import java.time.LocalDate;
<#break>
</#if>
</#list>
<#------------  字段循环遍历 结束  ---------->
<#-- ----------  字段循环遍历 开始  ---------->
<#list table.columns as column>
<#if column.javaTypeName == "LocalTime">
import java.time.LocalTime;
<#break>
</#if>
</#list>
<#------------  字段循环遍历 结束  ---------->
<#-- ----------  字段循环遍历 开始  ---------->
<#list table.columns as column>
<#if column.javaTypeName == "BigDecimal">
import java.math.BigDecimal;
<#break>
</#if>
</#list>
<#------------  字段循环遍历 结束  ---------->
/**
 *
 * ${table.comment!} ${table.name?cap_first} Dto对象
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
@ApiModel(value = "${table.name?cap_first} Dto对象"<#if table.comment!?length gt 0>, description = "${table.comment!}"</#if>)
</#if>
public class ${table.name?cap_first}${dto.suffix}  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

<#-- ----------  字段循环遍历 开始  ---------->
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
<#------------  字段循环遍历 结束  ---------->

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
<#if !dto.enableLombok>
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("${table.name?cap_first} (");
    <#list table.columns as column>
        if (${column.name} != null) sb.append("${column.name}=").append(${column.name}).append(", ");
    </#list>
        String res = sb.toString();
        if(!res.endsWith("(")) res = res.substring(0, res.length() - 2);
        return res + ")";
    }
</#if>
}
