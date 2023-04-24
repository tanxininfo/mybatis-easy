package com.mybatiseasy.core.entity;

import com.mybatiseasy.core.annotations.Table;
import com.mybatiseasy.core.annotations.TableField;
import com.mybatiseasy.core.annotations.TableId;
import com.mybatiseasy.core.enums.TableIdType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("user")
public class User {

    @TableId( idType = TableIdType.AUTO)
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
