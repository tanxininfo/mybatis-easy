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

package com.mybatiseasy.test.entity;


import java.time.LocalDateTime;

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

    @TableId( idType = TableIdType.AUTO)
    private Long id;
    @TableField(column="price_info", typeHandler = GoodsPriceTypeHandler.class)
    private GoodsPrice priceInfo;

    private Long goodsId;
    private LocalDateTime createTime;

}
