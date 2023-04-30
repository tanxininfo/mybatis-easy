package com.mybatiseasy.test.entity;

import com.mybatiseasy.annotation.Table;
import com.mybatiseasy.annotation.TableField;
import com.mybatiseasy.annotation.TableId;
import com.mybatiseasy.emums.TableIdType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table("user")
public class Yser implements Serializable{
    private String user;
    private Integer age;
}
