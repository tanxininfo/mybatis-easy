package com.mybatiseasy.core.entity;

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
