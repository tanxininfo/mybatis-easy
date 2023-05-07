/*
 * Copyright (c) 2023, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
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

package com.mybatiseasy.core.session;

import com.mybatiseasy.keygen.IKeyGenerator;
import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.core.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

/**
 * 实体类字段映射成对象
 */
@Slf4j
public class EntityFieldMap {

  /**
   * 字段的name
   */
  private String name;

  /**
   * 是否主键
   */
  private boolean isId;
  /**
   * 主键类型
   */
  private TableIdType idType;

  /**
   * 当主键类型为 SEQUENCE 时设置此值
   */
  private String sequence;

  /**
   * 当主键类型为 CUSTOM 时设置此值
   */
  private Class<? extends IKeyGenerator> keyGenerator;
  /**
   * 数据表中的字段名
   */
  private String column;
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
   * 是否乐观锁
   */
  private boolean isVersion;
  /**
   * 字段的JdbcType
   */
  private JdbcType jdbcType;
  /**
   * 字段的javaType
   */
  private Class<?> javaType;
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

    public Builder(String name, String column) {
      entityFieldMap.name = name;
      entityFieldMap.column = column;
    }

    public Builder(String name, String column, boolean isLarge) {
      this(name, column);
      entityFieldMap.isLarge = isLarge;
    }

    public Builder(String name, String column, String insertDefault, String updateDefault) {
      this(name, column);
      entityFieldMap.insertDefault = insertDefault;
      entityFieldMap.updateDefault = updateDefault;
    }

    public Builder name(String name) {
      entityFieldMap.name = name;
      return this;
    }
    public Builder column(String column) {
      entityFieldMap.column = column;
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
    public Builder isVersion(boolean isVersion) {
      entityFieldMap.isVersion = isVersion;
      return this;
    }
    public Builder jdbcType(JdbcType jdbcType) {
      entityFieldMap.jdbcType = jdbcType;
      return this;
    }
    public Builder javaType(Class<?> javaType) {
      entityFieldMap.javaType = javaType;
      return this;
    }
    public Builder numericScale(String numericScale) {
      entityFieldMap.numericScale = numericScale;
      return this;
    }
    public Builder isId(boolean isId) {
      entityFieldMap.isId = isId;
      return this;
    }
    public Builder idType(TableIdType idType) {
      entityFieldMap.idType = idType;
      return this;
    }
    public Builder sequence(String sequence) {
      entityFieldMap.sequence = sequence;
      return this;
    }
    public Builder keyGenerator(Class<? extends IKeyGenerator> keyGenerator) {
      entityFieldMap.keyGenerator = keyGenerator;
      return this;
    }
    public Builder typeHandler(Class<? extends TypeHandler> typeHandler) {
      entityFieldMap.typeHandler = typeHandler;
      return this;
    }

    public EntityFieldMap build() {
      entityFieldMap.column = SqlUtil.addBackquote(entityFieldMap.column);
      if(entityFieldMap.desc == null) entityFieldMap.desc = "";
      if(entityFieldMap.typeHandler == null) entityFieldMap.typeHandler = UnknownTypeHandler.class;
      if(entityFieldMap.jdbcType == null) entityFieldMap.jdbcType = JdbcType.UNDEFINED;
      if(entityFieldMap.insertDefault == null) entityFieldMap.insertDefault = "";
      if(entityFieldMap.updateDefault == null) entityFieldMap.updateDefault = "";
      if(entityFieldMap.numericScale == null) entityFieldMap.numericScale = "";
      return entityFieldMap;
    }
  }

  public String getName() {
    return name;
  }

  public String getColumn() {
    return column;
  }

  public String getDesc() {
    return desc;
  }

  public boolean  isLarge() {
    return isLarge;
  }
  public boolean  isVersion() {
    return isVersion;
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
  public boolean isId() {
    return isId;
  }
  public TableIdType  getIdType() {
    return idType;
  }
  public String  getSequence() {
    return sequence;
  }
  public Class<?>  getJavaType() {
    return javaType;
  }
  public Class<? extends TypeHandler>  getTypeHandler() {
    return typeHandler;
  }
  public Class<? extends IKeyGenerator>  getKeyGenerator() {
    return keyGenerator;
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
    final StringBuilder sb = new StringBuilder("{");
    sb.append("name='").append(name).append('\'');
    sb.append(", column='").append(column).append('\'');
    sb.append(", isId='").append(isId).append('\'');
    sb.append(", desc='").append(desc).append('\'');
    sb.append(", typeHandler='").append(typeHandler).append('\'');
    sb.append(", jdbcType='").append(jdbcType).append('\'');
    sb.append(", javaType='").append(javaType).append('\'');
    sb.append(", insertDefault='").append(insertDefault).append('\'');
    sb.append(", updateDefault='").append(updateDefault).append('\'');
    sb.append(", isLarge='").append(isLarge).append('\'');
    sb.append(", numericScale='").append(numericScale).append('\'');
    sb.append('}');
    return sb.toString();
  }

}
