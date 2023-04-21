package com.mybatiseasy.core.controller;

import com.mybatiseasy.core.entity.User;
import com.mybatiseasy.core.mapper.UserMapper;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.table._USER;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dudley
 * @since 2022-07-02
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public void get(){

        User one = userMapper.getByCondition(_USER.AGE().gt(10).and(_USER.NAME().like("张%")));

        log.info("one={}", one);
    }

    static class TestClass{
        TestClass(){

        }
        public void print(){
            log.info("TestClass");
        }
        public TestClass test() {return new TestClass();}
    }


    @GetMapping("/query")
    public void query(@RequestParam("param") Long param){
        // (id=1 or id=2) and (id=3 or id=4)
        //Order one = orderMapper.getByConditions(ORDER._id.eq(1).or(ORDER._id.eq(2)).and(ORDER._id.eq(3).or(ORDER._id.eq(4))));
        //Condition condition = _ORDER.ID.gt(1).and(_ORDER.ID.gt(2).and(_ORDER.ID.ne(3)));
        //Condition condition = _ORDER.ID.gt(true, 1).and(_ORDER.ID.gt(true, 2).and(false, _ORDER.ID.ne(true, 3)));
        //Condition condition =  _ORDER.ID.gt(true, 2).and(_ORDER.ID.ne(true, 3));
        //log.info("condition={}", condition.getSql());

        TestClass test = new TestClass();
        test.test().print();

//        QueryWrapper unionWrapper = new QueryWrapper();
//        unionWrapper.select();
//        unionWrapper.from(_USER.nm());
//
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.select(_USER.ID().NAME().AGE());
//        wrapper.union(unionWrapper);
//        wrapper.unionAll(unionWrapper);
//        wrapper.unionAll(unionWrapper);
//
//
//        List<User> list = userMapper.listByWrapper(wrapper);
//        log.info("list={}", list);
    }


}
