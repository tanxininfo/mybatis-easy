package com.mybatiseasy.core.paginate;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询分页信息
 *
 */
@Data
public class Page implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 数据总数
     */
    private long total;

    /**
     * 每页记录数
     */
    private long size;

    /**
     * 当前页
     */
    private long current;

    /**
     * 页数
     */
    private long pages;

    public Page(long total, long size, long current){
        this.setCurrent(current);
        this.setTotal(total);
        this.setSize(size);
        this.setPages( Math.ceilDiv(this.getTotal(), this.getSize()));
    }
}
