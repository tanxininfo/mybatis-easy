package com.mybatiseasy.core.controller;

//import com.mybatiseasy.core.mapper.UserMapper;
import com.mybatiseasy.core.entity.User;
import com.mybatiseasy.core.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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

        User one = userMapper.getById(1238);
        log.info("one={}", one);
    }


}
