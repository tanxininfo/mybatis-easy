package com.mybatiseasy.core.entity;


import java.time.LocalDateTime;

import com.mybatiseasy.core.annotations.Table;
import com.mybatiseasy.core.annotations.TableField;
import com.mybatiseasy.core.annotations.TableId;
import com.mybatiseasy.core.enums.TableIdType;
import com.mybatiseasy.core.temp.GoodsPriceTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author dudley
 * @since 2022-08-21
 */
@Data
@Accessors(chain = true)
@Table("order")
public class Order{

    @TableId( idType = TableIdType.AUTO)
    private Long id;
    @TableField(column="price_info", typeHandler = GoodsPriceTypeHandler.class)
    private GoodsPrice priceInfo;

    private Long goodsId;
    private LocalDateTime createTime;

}
