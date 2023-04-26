package com.mybatiseasy.core.controller;

import com.mybatiseasy.core.entity.User;
import com.mybatiseasy.core.mapper.UserMapper;
import com.mybatiseasy.core.table._USER;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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

        User one = userMapper.getOne(_USER.AGE().gt(10).and(_USER.NAME().like("张%")).and(_USER.NAME().like("李%")));
        //User one = userMapper.getOne(_USER.ID().eq(1));


        log.info("one={}", one);
    }


    private void sayHi( Object... strings ){

        System.out.println("----------" + strings);

        if ( strings != null ) {

            for (Object object : strings) {

                System.out.println(object.toString());
            }
        }
        else {
            System.out.println("=========null");
        }
    }



    @GetMapping("/query")
    public void query(@RequestParam("param") Long param){
        // (id=1 or id=2) and (id=3 or id=4)
        //Order one = orderMapper.getByConditions(ORDER._id.eq(1).or(ORDER._id.eq(2)).and(ORDER._id.eq(3).or(ORDER._id.eq(4))));
        //Condition condition = _ORDER.ID.gt(1).and(_ORDER.ID.gt(2).and(_ORDER.ID.ne(3)));
        //Condition condition = _ORDER.ID.gt(true, 1).and(_ORDER.ID.gt(true, 2).and(false, _ORDER.ID.ne(true, 3)));
        //Condition condition =  _ORDER.ID.gt(true, 2).and(_ORDER.ID.ne(true, 3));
        //log.info("condition={}", condition.getSql());

//        SnowFlakeIdGenerator snowflakeIdGenerator = new SnowFlakeIdGenerator();
//
//        // 生成50个id
//        Set<Long> set = new TreeSet<>();
//        for (int i = 0; i < 50; i++) {
//            set.add(snowflakeIdGenerator.nextId());
//        }
//        System.out.println(set.size());
//        System.out.println(set);
//
//        // 验证生成100万个id需要多久
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 1000000; i++) {
//            snowflakeIdGenerator.nextId();
//        }
//        System.out.println(System.currentTimeMillis() - startTime);



        List<User> userList = new ArrayList<>();

        for(int i=0; i<1;i++) {

        }

//        User user = new User();
//        user.setAge(100);
//        user.setName("李四四");
//        user.setId(5L);
//
//        int affectedRows = userMapper.update(user);
//
//
//        log.info("{}, user={}", affectedRows, user);






    }


}
