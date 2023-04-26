package com.mybatiseasy.core.tables;

import com.mybatiseasy.core.cols.${colClassName};
import com.mybatiseasy.core.base.Table;

public class ${tableClassName} extends Table {
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    public static ${colClassName} ${column.capitalName}() {
        return new ${colClassName}().${column.capitalName}();
    }

</#list>
<#------------  END 字段循环遍历  ---------->


<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnList as column>
    public static ${colClassName} ${column.capitalName}(String alias) {
        return new ${colClassName}().${column.capitalName}(alias);
    }

</#list>
<#------------  END 字段循环遍历  ---------->

    public static ${colClassName} as(String tableAlias) {
        return new ${colClassName}("`${tableName}`", tableAlias);
    }

    public static ${colClassName} nm() {
        return new ${colClassName}("`${tableName}`", "");
    }
}
