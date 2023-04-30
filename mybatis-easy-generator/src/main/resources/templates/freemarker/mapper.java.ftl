package ${global.packageName}.${mapper.packageName};

import ${global.packageName}.${entity.packageName}.${table.name};
<#if mapper.supperClass??>
import ${mapper.supperClass.name};
<#else >
import com.mybatiseasy.core.base.IMapper;
</#if>

/**
* ${table.comment!} Mapper 接口
*
* @author ${global.author!}
* @since ${global.commentDate!.now?string("yyyy-MM-dd")}
*/
public interface ${table.name}${mapper.suffix} extends ${mapper.supperClass!"IMapper"}<${table.name}> {

}
