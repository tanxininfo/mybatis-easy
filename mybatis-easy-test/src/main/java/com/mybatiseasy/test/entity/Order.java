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

package com.mybatiseasy.test.entity;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.mybatiseasy.annotation.Table;
import com.mybatiseasy.annotation.TableField;
import com.mybatiseasy.annotation.TableId;
import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.test.typehandler.GoodsPriceTypeHandler;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author dudley
 * @since 2022-08-21
 */
@Data
@Table("order")
public class Order{

    @TableId( type = TableIdType.AUTO)
    private Long id;
    @TableField(column="price_info")
    private Map<String, Object> priceInfo;

    @TableField(column="tourist_info")
    private List<Map<String, Object>> touristInfo;

    private Long goodsId;
    private LocalDateTime createTime;

}
