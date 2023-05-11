package com.mybatiseasy.core.tables;

import com.mybatiseasy.core.defs.${defClassName};
import com.mybatiseasy.core.base.Table;

public class ${tableClassName} {
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    public static ${defClassName} ${column.capitalName}() {
        return new ${defClassName}().${column.capitalName}();
    }

</#list>
<#------------  END 字段循环遍历  ---------->


<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    public static ${defClassName} ${column.capitalName}(String alias) {
        return new ${defClassName}().${column.capitalName}(alias);
    }

</#list>
<#------------  END 字段循环遍历  ---------->

    public static ${defClassName} as(String tableAlias) {
        return new ${defClassName}("`${tableName}`", tableAlias);
    }

    public static ${defClassName} as() {
        return new ${defClassName}("`${tableName}`", "");
    }
}
