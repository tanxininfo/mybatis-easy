package com.mybatiseasy.core.paginate;

import com.mybatiseasy.core.enums.Direction;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询排序信息
 *
 * @author Chopper
 */
@Data
public class Sort implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 排序字段
     */
    private String column = "";

    /**
     * 排序顺序
     */
    private Direction direction = Direction.ASC;

    /**
     * 是否升序
     * @return boolean
     */
    public boolean isAsc(){
        return direction.equals(Direction.ASC);
    }
}
