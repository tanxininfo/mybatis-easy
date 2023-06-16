package ${global.packageName}.${service.packageName};

import ${global.packageName}.${entity.packageName}.${table.name?cap_first}${entity.suffix};
import ${global.packageName}.${dto.packageName}.${table.name?cap_first}${dto.suffix};

import org.springframework.transaction.annotation.Transactional;
import com.mybatiseasy.core.paginate.PageList;
import com.mybatiseasy.core.paginate.Page;

import info.tanxin.system.common.api.Sort;

/**
* @author ${global.author!}
* @since ${global.commentDate!.now?string("yyyy-MM-dd")}
*/
<#assign entityClassName="${table.name?cap_first}${entity.suffix}" />
<#assign dtoClassName="${table.name?cap_first}${dto.suffix}" />
<#assign serviceClassName="${table.name?cap_first}${service.suffix}" />
<#assign priColumnName="${table.priColumn.name}"/>
<#assign priColumnType="${table.priColumn.javaTypeName}"/>
@Transactional
public interface ${serviceClassName}<#if service.supperClass??> extends ${service.supperClass!}<${entityClassName}></#if> {

/**
* ${entityClassName}详情
* @param ${priColumnName} 主键
* @return 结果
*/
${entityClassName} get(${priColumnType} ${priColumnName});

/**
* ${entityClassName}分页查询
* @param ${dtoClassName?uncap_first} 查询条件
* @param sort 排序
* @param page 分页
* @return 结果
*/
PageList<${entityClassName}> page(${dtoClassName} ${dtoClassName?uncap_first}, Sort sort, Page page);

/**
* ${entityClassName}新增
* @param ${entityClassName?uncap_first} ${table.comment}信息
* @return 结果
*/
${priColumnType} create(${entityClassName} ${entityClassName?uncap_first});

/**
* ${entityClassName}修改
* @param ${entityClassName?uncap_first} ${table.comment}信息
* @return 影响记录数
*/
int update(${priColumnType} ${priColumnName}, ${entityClassName} ${entityClassName?uncap_first});

/**
* ${entityClassName}删除
* @param ${priColumnName} ${table.comment}id
* @return 影响记录数
*/
int remove(${priColumnType} ${priColumnName});


}

