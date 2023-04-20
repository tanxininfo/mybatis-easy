package com.mybatiseasy.core.entity;

import com.mybatiseasy.core.annotations.Table;
import lombok.Data;

@Data
@Table("user")
public class User {

    private Long id;
    private String name;
    private String age;
    private Short sex;
    private Long parentId;

}
