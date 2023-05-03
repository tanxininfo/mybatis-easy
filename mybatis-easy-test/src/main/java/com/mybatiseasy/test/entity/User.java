/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

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
public class User implements Serializable{
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
