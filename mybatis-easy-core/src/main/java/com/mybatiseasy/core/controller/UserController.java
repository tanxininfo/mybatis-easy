package com.mybatiseasy.core.controller;

import com.mybatiseasy.core.entity.User;
import com.mybatiseasy.core.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author dudley
 * @since 2022-07-02
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public void get(){
        User user = new User();
        user.setId(11111L);
        user.setUsername("dudley");
        log.info("userMapper={}", userMapper);
        User one = userMapper.getById(1238);
        log.info("one={}", one);
    }


}
