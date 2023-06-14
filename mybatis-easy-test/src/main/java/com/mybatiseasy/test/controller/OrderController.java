/*
 * Copyright (c) 2023, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.mybatiseasy.test.controller;

import com.mybatiseasy.core.type.RecordList;
import com.mybatiseasy.core.mapper.DbMapper;
import com.mybatiseasy.core.paginate.PageList;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.tables.USER;
import com.mybatiseasy.core.type.Record;
import com.mybatiseasy.core.utils.DbUtil;
import com.mybatiseasy.core.utils.ObjectUtil;
import com.mybatiseasy.test.entity.User;
import com.mybatiseasy.test.mapper.OrderMapper;
import com.mybatiseasy.test.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dudley
 * @since 2022-07-02
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private DbMapper dbMapper;

    @Autowired
    private SqlSessionFactoryBean sqlSessionFactoryBean;


    @GetMapping("query")
    public void query() {
        QueryWrapper queryWrapper = QueryWrapper.create().select(USER.NAME().CREATE_TIME().UPDATE_TIME()).from(USER.as());

        PageList<User> users = userMapper.paginate(queryWrapper, 10, 1);
        log.info("users={}", ObjectUtil.toJson(users));

//        List<User> list = dbMapper.list(QueryWrapper.create().from(USER.as())).toBeanList(User.class);
//        log.info("lists={}", ObjectUtil.toJson(list));
//
//        PageList<User> userList = dbMapper.paginate(queryWrapper, 10, 1, User.class);
//        log.info("list={}", ObjectUtil.toJson(userList));
    }


    @GetMapping("add")
    @Transactional
    public void add() throws Exception {

//
//        User user = new User();
//        user.setName("user1");
//        user.setParentId(1L);
//
//        User user2 = new User();
//        user2.setName("user2");
//        user2.setParentId(1L);
//
//        List<User> userList = new ArrayList<>();
//        userList.add(user);
//        userList.add(user2);
//
//        int affectedRows = userMapper.insertBatch(userList);
//        log.info("affectedRows={}", affectedRows);
//        log.info("userList={}", ObjectUtil.toJson(userList));
//
        Record record = new Record();
        record.set(USER.NAME(), "addName1");
        record.set("parent_id", 3);
        int affectedRows = DbUtil.insert(record, User.class);
        log.info("affectedRows={}", affectedRows);
        log.info("userList={}", ObjectUtil.toJson(record));
        Record record2 = new Record();
        record2.set(USER.NAME(), "addName1");
        record2.set("parent_id", 3);
        record2.set("id", 1);
        affectedRows = DbUtil.insert(record2, User.class);
        log.info("affectedRows={}", affectedRows);
        log.info("userList={}", ObjectUtil.toJson(record));

//        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
//        assert sqlSessionFactory != null;
//        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
//            DbMapper mapper = sqlSession.getMapper(DbMapper.class);
//            RecordList recordList = mapper.list(new QueryWrapper().from(USER.as()).select(USER.NAME()));
//            log.info("recordList={}", ObjectUtil.toJson(recordList));
//        }




    }
}
