package com.mybatiseasy.core.entity;

import com.mybatiseasy.core.annotations.Table;
import lombok.Data;

@Data
@Table("u_user")
public class User {

    private Long id;

    private String username;

}
