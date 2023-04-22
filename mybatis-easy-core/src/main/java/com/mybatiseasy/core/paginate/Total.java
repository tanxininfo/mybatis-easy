package com.mybatiseasy.core.paginate;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询分页信息
 *
 */
@Data
public class Total implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 数据总数
     */
    private long total;
}
