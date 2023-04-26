package ${config.package};

import lombok.Data;

<#list importPackages as pkg>
import ${pkg};
</#list>

<#function snakeToCamel(s)>
    <#return s
    ?replace('(^_+)|(_+$)', '', 'r')
    ?replace('\\_+(\\w)?', ' $1', 'r')
    ?replace('([A-Z])', ' $1', 'r')
    ?capitalize
    ?replace(' ' , '')
    ?uncap_first
    >
</#function>
<#function camelToSnake(s)>
    <#return s
    <#-- "fooBar" to "foo_bar": -->
    ?replace('([a-z])([A-Z])', '$1_$2', 'r')
    <#-- "FOOBar" to "FOO_Bar": -->
    ?replace('([A-Z])([A-Z][a-z])', '$1_$2', 'r')
    <#-- All of those to "FOO_BAR": -->
    ?upper_case
    >
</#function>
/**
 * <p>
 * ${tableInfo.comment}
 * </p>
 *
 * @author ${config.author}
 * @since ${config.commentDate}
 */
@Data
public class ${name?cap_first}Dto {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    <#if column.comment!?length gt 0>
    /**
    * ${column.comment}
    */
    </#if>
    private ${snakeToCamel(column.dataType)?cap_first} ${column.name};

</#list>
<#------------  END 字段循环遍历  ---------->
}
