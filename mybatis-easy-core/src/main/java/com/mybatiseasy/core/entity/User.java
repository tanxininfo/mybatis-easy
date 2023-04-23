package com.mybatiseasy.core.entity;

import com.mybatiseasy.core.annotations.Table;
import com.mybatiseasy.core.annotations.TableId;
import com.mybatiseasy.core.enums.TableIdType;
import lombok.Data;

@Data
@Table("user")
public class User {

    @TableId( idType = TableIdType.AUTO)
    private Long id;
    private String name;
    private String age;
    private Short sex;
    private Long parentId;

}
