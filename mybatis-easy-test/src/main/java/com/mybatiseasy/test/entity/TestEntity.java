package com.mybatiseasy.test.entity;

import com.mybatiseasy.annotation.Table;
import com.mybatiseasy.annotation.TableField;
import com.mybatiseasy.annotation.TableId;
import com.mybatiseasy.emums.TableIdType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table("testEntity")
public class TestEntity  implements Serializable {

    @TableId( idType = TableIdType.SEQUENCE, sequence = "SELECT id+1 as id from `user` order by id desc limit 1")
    private Long id;
    private String name;
    private Integer age;
    private Short sex;
    private Long parentId;
    @TableField(insert = "NOW()")
    private LocalDateTime createTime;
    @TableField(insert = "NOW()", update = "NOW()")
    private LocalDateTime updateTime;

}
