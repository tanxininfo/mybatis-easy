package ${global.packageName}.${serviceImpl.packageName};

import info.tanxin.system.common.exception.TanXinException;
import info.tanxin.system.common.util.SortUtil;
import info.tanxin.system.common.api.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybatiseasy.core.paginate.Page;
import com.mybatiseasy.core.paginate.PageList;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;

<#if serviceImpl.supperClass??>
import ${serviceImpl.supperClass.name};
</#if>

<#assign entityClassName="${table.name?cap_first}${entity.suffix}" />
<#assign dtoClassName="${table.name?cap_first}${dto.suffix}" />
<#assign mapperClassName="${table.name?cap_first}${mapper.suffix}" />
<#assign serviceClassName="${table.name?cap_first}${service.suffix}" />
<#assign serviceImplClassName="${table.name?cap_first}${serviceImpl.suffix}" />
<#assign priColumnName="${table.priColumn.name}"/>
<#assign priColumnType="${table.priColumn.javaTypeName}"/>

import com.mybatiseasy.core.defs.${entityClassName}Def;
import info.tanxin.system.core.utils.DataUtil;


import ${global.packageName}.${entity.packageName}.${table.name?cap_first}${entity.suffix};
import ${global.packageName}.${dto.packageName}.${table.name?cap_first}${dto.suffix};
import ${global.packageName}.${service.packageName}.${table.name?cap_first}${service.suffix};
import ${global.packageName}.${mapper.packageName}.${table.name?cap_first}${mapper.suffix};


/**
* ${table.comment!} 服务实现类
*
* @author ${global.author!}
* @since ${global.commentDate!.now?string("yyyy-MM-dd")}
*/
@Service
<#if serviceImpl.supperClass??>
public class ${serviceImplClassName} extends ${serviceImpl.supperClass!}<${mapperClassName}, ${entityClassName}> implements ${serviceClassName} {
<#else>
public class ${serviceImplClassName} extends BaseServiceImpl<${mapperClassName}, ${entityClassName}> implements ${serviceClassName} {
</#if>

    /**
    * ${table.comment!}查询
    * @param ${priColumnName} 主键
    * @return 结果
    */
    public ${entityClassName} get(${priColumnType} ${priColumnName}) {
        return super.getById(${priColumnName});
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

        sort.setColumn(DataUtil.getColumnByName(${entityClassName?cap_first}Def.className, sort.getColumn()));
        Sort formattedSort = SortUtil.format(sort, "${priColumnName}");
        wrapper.orderBy(formattedSort.getFullColumn(), formattedSort.isDesc());

        return super.paginate(wrapper, page.getSize(), page.getCurrent());
    }

    /**
    * ${table.comment!}添加
    * @param ${entityClassName?uncap_first} ${table.comment!}信息
    * @return 结果
    */
    public ${priColumnType} create(${entityClassName} ${entityClassName?uncap_first}) {
        int affectedRows = super.insert(${entityClassName?uncap_first});
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
        ${entityClassName} old${entityClassName} = super.getById(${priColumnName});
        if(old${entityClassName} == null) throw new TanXinException("记录未找到");
        <#if entity.versionName?? && table.columnNames?seq_contains(entity.versionName) >
        ${entityClassName?uncap_first}.setVersion(old${entityClassName}.getVersion());
        </#if>

        return super.updateById(${entityClassName?uncap_first});
    }

    /**
    * ${table.comment!}删除
    * @param ${priColumnName} ${table.comment!}id
    * @return 结果
    */
    public int remove(${priColumnType} ${priColumnName}) {
        ${entityClassName} ${entityClassName?uncap_first} = super.getById(${priColumnName});

        if(${entityClassName?uncap_first} == null) return 0;
        return super.deleteById(${priColumnName});
    }
}
