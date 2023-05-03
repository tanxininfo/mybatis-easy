/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
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

import com.mybatiseasy.core.sqlbuilder.Condition;
import com.mybatiseasy.core.sqlbuilder.QueryWrapper;
import com.mybatiseasy.core.tables._ORDER;
import com.mybatiseasy.test.entity.Order;
//import com.mybatiseasy.test.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
//
//    @Autowired
//    private OrderMapper orderMapper;
//
//    @GetMapping
//    public void getList(){
//
////        List<Order> orderList = orderMapper.list(_ORDER.ID().eq(1).or(_ORDER.ID().eq(2)).and(_ORDER.ID().eq(3).or(_ORDER.ID().eq(4))));
////        Condition condition = _ORDER.ID().gt(1).and(_ORDER.ID().gt(2).and(_ORDER.ID().ne(3)));
////        Condition condition = _ORDER.ID().gt(1, true).and(_ORDER.ID().gt(2, true).and(_ORDER.ID().ne(3, true), false));
////        Condition condition =  _ORDER.ID().gt(2, true).and(_ORDER.ID().ne(3, true));
//
//        List<Order> orderList = orderMapper.list(new QueryWrapper().limit(5L));
//
//        log.info("orderList={}", orderList);
//    }
}
