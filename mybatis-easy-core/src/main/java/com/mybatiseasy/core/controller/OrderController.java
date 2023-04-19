package com.mybatiseasy.core.controller;

import com.mybatiseasy.core.entity.Order;
import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.table.$;
import com.mybatiseasy.core.table._ORDER;
import com.mybatiseasy.core.table._T;
import com.mybatiseasy.core.table._USER;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mybatiseasy.core.mapper.OrderMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dudley
 * @since 2022-07-02
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping
    public void get(){

        Order one = orderMapper.getById(2301010015420437L);
        log.info("one.sqlSessionFactory={}",one.sqlSessionFactory);
        log.info("one={}", one);
    }


    @GetMapping("/query")
    public void query(@RequestParam("param") Long param){
        // (id=1 or id=2) and (id=3 or id=4)
        //Order one = orderMapper.getByConditions(ORDER._id.eq(1).or(ORDER._id.eq(2)).and(ORDER._id.eq(3).or(ORDER._id.eq(4))));
        //Condition condition = _ORDER.ID.gt(1).and(_ORDER.ID.gt(2).and(_ORDER.ID.ne(3)));
        Condition condition = _ORDER.ID.gt(true, 1).and(_ORDER.ID.gt(true, 2).and(false, _ORDER.ID.ne(true, 3)));
        //Condition condition =  _ORDER.ID.gt(true, 2).and(_ORDER.ID.ne(true, 3));
        log.info("condition={}", condition.getSql());


        QueryWrapper wrapper = new QueryWrapper();
        wrapper.selectFrom("son").column(_USER.ID, _USER.NAME, _USER.AGE);
        wrapper.selectFrom("parent").column(_USER.NAME.as("parentName"));
        wrapper.from($._USER.as("son"), $._USER.as("parent"));
        wrapper.where(_USER.PARENT_ID.of("son").eq(_USER.ID.of("parent")));
        log.info("aaa={}", wrapper.getSql());

        List<Order> list = orderMapper.listByWrapper(wrapper);
        log.info("list={}", list);
    }


}
