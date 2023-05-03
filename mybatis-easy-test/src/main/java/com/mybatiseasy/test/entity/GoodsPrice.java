/*
 *
 *  * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (soft@tanxin.info).
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.mybatiseasy.test.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatiseasy.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

    @Data
    @Accessors(chain = true)
    @Table
    public class GoodsPrice implements Serializable {

        private Long id;


        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTime;


        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updateTime;


        private Long goodsSetId;


        private Long tenantId;


        private Long goodsId;


        private Long levelId;


        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;


        private BigDecimal priceAdult;

        private BigDecimal priceChild;

        private BigDecimal priceBaby;


        private Integer stockAdult;


        private Integer stockChild;


        private Integer stockBaby;


        private Integer stockAdultMax;


        private Integer stockChildMax;


        private Integer stockBabyMax;


        private Integer week;


    }
