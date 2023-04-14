package com.mybatiseasy.core.controller;

import com.mybatiseasy.core.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mybatiseasy.core.mapper.OrderMapper;

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
        log.info("one={}", one);
    }


}
