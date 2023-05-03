package com.mybatiseasy.test.mapper;

import com.mybatiseasy.test.entity.User;
import com.mybatiseasy.core.base.IMapper;
import org.apache.ibatis.annotations.Mapper;

/**
*  Mapper 接口
*
* @author 
* @since 2023-05-03
*/
@Mapper
public interface UserMapper extends IMapper<User> {

}
