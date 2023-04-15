/*
 *    Copyright 2009-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.mybatiseasy.core.session;

import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 实体类字段映射成对象
 */
public class EntityFieldMap {

  /**
   * 字段的name
   */
  private String name;
  /**
   * 字段的desc
   */
  private String desc;
  /**
   * 字段插入默认值
   */
  private String insertDefault;
  /**
   * 字段更新默认值
   */
  private String updateDefault;
  /**
   * 是否大字段
   */
  private boolean isLarge;
  /**
   * 字段的JdbcType
   */
  private JdbcType jdbcType;
  /**
   * 字段的TypeHandler
   */
  private Class<? extends TypeHandler> typeHandler;
  /**
   * 小数点位数
   */
  private String numericScale;

  private EntityFieldMap() {
  }

  public static class Builder {
    private final EntityFieldMap entityFieldMap = new EntityFieldMap();

    public Builder(String name, String desc) {
      entityFieldMap.name = name;
      entityFieldMap.desc = desc;
    }

    public Builder(String name, String desc, boolean isLarge) {
      this(name, desc);
      entityFieldMap.isLarge = isLarge;
    }

    public Builder(String name, String desc, String insertDefault, String updateDefault) {
      this(name, desc);
      entityFieldMap.insertDefault = insertDefault;
      entityFieldMap.updateDefault = updateDefault;
    }

    public Builder name(String name) {
      entityFieldMap.name = name;
      return this;
    }
    public Builder desc(String desc) {
      entityFieldMap.desc = desc;
      return this;
    }
    public Builder insertDefault(String insertDefault) {
      entityFieldMap.insertDefault = insertDefault;
      return this;
    }
    public Builder updateDefault(String updateDefault) {
      entityFieldMap.updateDefault = updateDefault;
      return this;
    }
    public Builder isLarge(boolean isLarge) {
      entityFieldMap.isLarge = isLarge;
      return this;
    }
    public Builder jdbcType(JdbcType jdbcType) {
      entityFieldMap.jdbcType = jdbcType;
      return this;
    }
    public Builder numericScale(String numericScale) {
      entityFieldMap.numericScale = numericScale;
      return this;
    }
    public Builder typeHandler(Class<? extends TypeHandler> typeHandler) {
      entityFieldMap.typeHandler = typeHandler;
      return this;
    }
  }

  public String getName() {
    return name;
  }

  public String getDesc() {
    return desc;
  }

  public boolean  getIsLarge() {
    return isLarge;
  }

  public String  getNumericScale() {
    return numericScale;
  }
  public String  getInsertDefault() {
    return insertDefault;
  }
  public String  getUpdateDefault() {
    return updateDefault;
  }
  public JdbcType  getJdbcType() {
    return jdbcType;
  }
  public Class<? extends TypeHandler>  getTypeHandler() {
    return typeHandler;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EntityFieldMap that = (EntityFieldMap) o;

    return name != null && name.equals(that.name);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityFieldMap{");
    sb.append("name='").append(name).append('\'');
    sb.append(", desc='").append(desc).append('\'');
    sb.append(", typeHandler=").append(typeHandler).append('\'');
    sb.append(", jdbcType=").append(jdbcType).append('\'');
    sb.append(", insertDefault=").append(insertDefault).append('\'');
    sb.append(", updateDefault=").append(updateDefault).append('\'');
    sb.append(", isLarge=").append(isLarge).append('\'');
    sb.append(", numericScale=").append(numericScale).append('\'');
    sb.append('}');
    return sb.toString();
  }

}
