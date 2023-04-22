package com.mybatiseasy.core.paginate;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页查询列表
 *
 */
@Data
public class PageList<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 分页信息
     */
    private Page page;

    public PageList(List<T> list, Page page){
        this.setList(list);
        this.setPage(page);
    }
}
