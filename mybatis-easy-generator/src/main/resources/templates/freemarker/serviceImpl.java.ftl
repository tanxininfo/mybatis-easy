package ${global.packageName}.${serviceImpl.packageName};

import info.tanxin.system.common.exception.TanXinException;
import info.tanxin.system.common.util.SortUtil;
import info.tanxin.system.common.api.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybatiseasy.core.paginate.Page;
import com.mybatiseasy.core.paginate.PageList;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;

import ${global.packageName}.${entity.packageName}.${table.name?cap_first}${entity.suffix};
import ${global.packageName}.${dto.packageName}.${table.name?cap_first}${dto.suffix};
import ${global.packageName}.${service.packageName}.${table.name?cap_first}${service.suffix};
import ${global.packageName}.${mapper.packageName}.${table.name?cap_first}${mapper.suffix};

<#assign entityClassName="${table.name?cap_first}${entity.suffix}" />
<#assign dtoClassName="${table.name?cap_first}${dto.suffix}" />
<#assign mapperClassName="${table.name?cap_first}${mapper.suffix}" />
<#assign serviceClassName="${table.name?cap_first}${service.suffix}" />
<#assign serviceImplClassName="${table.name?cap_first}${serviceImpl.suffix}" />
<#assign priColumnName="${table.priColumn.name}"/>
<#assign priColumnType="${table.priColumn.javaTypeName}"/>
/**
* ${table.comment!} 服务实现类
*
* @author ${global.author!}
* @since ${global.commentDate!.now?string("yyyy-MM-dd")}
*/
@Service
public class ${serviceImplClassName}<#if serviceImpl.supperClass??> extends ${serviceImpl.supperClass!}<${entityClassName}></#if> implements ${serviceClassName} {

    @Autowired
    private ${mapperClassName} ${mapperClassName?uncap_first};

    /**
    * ${table.comment!}查询
    * @param ${priColumnName} 主键
    * @return 结果
    */
    public ${entityClassName} get(${priColumnType} ${priColumnName}) {
        return ${mapperClassName?uncap_first}.getById(${priColumnName});
    }

    /**
    * ${table.comment!}分页查询
    * @param ${entityClassName?uncap_first}Dto 查询条件
    * @param sort 排序
    * @param page 分页
    * @return 结果
    */
    public PageList<${entityClassName}> page(${dtoClassName} ${dtoClassName?uncap_first}, Sort sort, Page page) {
        QueryWrapper wrapper = new QueryWrapper();

        Sort formattedSort = SortUtil.format(sort, "${priColumnName}");
        wrapper.orderBy(formattedSort.getColumn(), formattedSort.isDesc());

        return ${mapperClassName?uncap_first}.paginate(wrapper, page.getSize(), page.getCurrent());
    }

    /**
    * ${table.comment!}添加
    * @param ${entityClassName?uncap_first} ${table.comment!}信息
    * @return 结果
    */
    public ${priColumnType} create(${entityClassName} ${entityClassName?uncap_first}) {
        int affectedRows = ${mapperClassName?uncap_first}.insert(${entityClassName?uncap_first});
        if(affectedRows == 1) {
            return ${entityClassName?uncap_first}.get${priColumnName?cap_first}();
        }
        else throw new TanXinException("记录添加失败");
    }

    /**
    * ${table.comment!}修改
    * @param ${priColumnName}
    * @param ${entityClassName?uncap_first} ${table.comment!}信息
    * @return 结果
    */
    public int update(${priColumnType} ${priColumnName}, ${entityClassName} ${entityClassName?uncap_first}) {
        ${entityClassName?uncap_first}.set${priColumnName?cap_first}(${priColumnName});
        ${entityClassName} old${entityClassName} = ${mapperClassName?uncap_first}.getById(${priColumnName});
        if(old${entityClassName} == null) throw new TanXinException("记录未找到");
        <#if entity.versionName?? && table.columnNames?contains(entity.versionName) >
        ${entityClassName?uncap_first}.setVersion(old${entityClassName}.getVersion());
        </#if>

        return ${mapperClassName?uncap_first}.updateById(${entityClassName?uncap_first});
    }

    /**
    * ${table.comment!}删除
    * @param ${priColumnName} ${table.comment!}id
    * @return 结果
    */
    public int remove(${priColumnType} ${priColumnName}) {
        ${entityClassName} ${entityClassName?uncap_first} = ${mapperClassName?uncap_first}.getById(${priColumnName});

        if(${entityClassName?uncap_first} == null) return 0;
        return ${mapperClassName?uncap_first}.deleteById(${priColumnName});
    }
}
