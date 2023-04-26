package com.mybatiseasy.core.entity;


import java.time.LocalDateTime;

import com.mybatiseasy.annotation.Table;
import com.mybatiseasy.annotation.TableField;
import com.mybatiseasy.annotation.TableId;
import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.core.temp.GoodsPriceTypeHandler;
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
