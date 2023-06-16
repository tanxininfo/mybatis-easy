package ${global.packageName}.${entity.packageName};

import com.mybatiseasy.annotation.*;
import com.mybatiseasy.emums.TableIdType;

<#if entity.swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entity.enableLombok>
import lombok.Data;
<#if entity.chain>
import lombok.experimental.Accessors;
</#if>
</#if>

<#if entity.supperClass??>
import ${entity.supperClass.name};
<#else>
import java.io.Serializable;
import java.io.Serial;
</#if>

<#if table.keyGenerator??>
import ${table.keyGenerator.name};
</#if>
<#-- ----------  字段循环遍历 开始  ---------->
<#list table.columns as column>
<#if column.javaTypeName == "LocalDateTime" || column.javaTypeName == "LocalDate" || column.javaTypeName == "LocalTime">
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
<#break>
</#if>
</#list>
<#------------  字段循环遍历 结束  ---------->
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
 * ${table.comment!}
 *
 * @author ${global.author!}
 * @since ${global.commentDate!.now?string("yyyy-MM-dd")}
 */
<#if entity.enableLombok>
@Data
<#if entity.chain>
@Accessors(chain = true)
</#if>
</#if>
@Table("${table.tableName}")
<#if entity.swagger>
@ApiModel(value = "${table.name?cap_first}对象", description = "${table.comment!}")
</#if>
<#if entity.supperClass??>
public class ${table.name?cap_first}${entity.suffix} extends ${entity.supperClass.simpleName} {
<#else>
public class ${table.name?cap_first}${entity.suffix} implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
</#if>

<#-- ----------  字段循环遍历 开始  ---------->
<#list table.columns as column>

    <#if column.comment!?length gt 0>
    <#if entity.swagger>
    @ApiModelProperty("${column.comment}")
    <#else>
    /**
     * ${column.comment}
     */
    </#if>
    </#if>
    <#-- 主键 -->
    <#if column.pri>
    <#if column.autoIncrement>
    @TableId(type = TableIdType.AUTO)
    <#elseif global.idType??>
    @TableId(type = TableIdType.${global.idType}<#if global.sequence!?length gt 0>, sequence="${global.sequence}"</#if><#if global.keyGenerator??>, keyGenerator=${global.keyGenerator.simpleName}.class</#if>)
    </#if>
    </#if>
    <#if column.javaTypeName == "LocalDateTime">
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    <#if column.javaTypeName == "LocalDate">
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    </#if>
    <#if column.javaTypeName == "LocalTime">
        @JsonFormat(
        pattern = "HH:mm:ss",
        timezone = "GMT+8"
        )
        @DateTimeFormat(pattern = "HH:mm:ss")
    </#if>
    @TableField(column = "${column.columnName}"<#if column.insert!?length gt 0>, insert = "${column.insert!}"</#if><#if column.update!?length gt 0>, update = "${column.update!}"</#if><#if column.javaTypeName=="Float" || column.javaTypeName=="BigDecimal">, numericScale = "${column.numericScale}"</#if>)
    <#if column.version!false>
    @Version
    </#if>
    <#if column.logicDelete!false>
    @LogicDelete(deleteValue = "${column.logicDeleteValue}", notDeleteValue = "${column.logicNotDeleteValue}")
    </#if>
    <#if column.tenantId!false>
    @TenantId
    </#if>
    private ${column.javaTypeName} ${column.name};

</#list>
<#------------  字段循环遍历 结束  ---------->

<#if !entity.enableLombok>
    <#list table.columns as column>
        <#if column.javaTypeName == "Boolean">
            <#assign prefix="is"/>
        <#else>
            <#assign prefix="get"/>
        </#if>
    public ${column.javaTypeName} ${prefix}${column.name?cap_first}() {
        return ${column.name};
    }

        <#if entity.enableLombok && entity.chain>
    public ${table.name} set${column.name?cap_first}(${column.javaTypeName} ${column.name}) {
        <#else>
    public void set${column.name?cap_first}(${column.javaTypeName} ${column.name}) {
        </#if>
        this.${column.name} = ${column.name};
        <#if entity.enableLombok && entity.chain>
        return this;
        </#if>
    }

    </#list>
</#if>


<#if !entity.enableLombok>
    @Override
    public String toString() {
    return "${table.name}{" +
    <#list table.columns as column>
        <#if column_index==0>
            "${column.name}=" + ${column.name} +
        <#else>
            ", ${column.name}=" + ${column.name} +
        </#if>
    </#list>
    "}";
    }
</#if>
}
