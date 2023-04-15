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
 * 实体类映射成对象
 */
public class EntityMap {

  /**
   * 实体的name
   */
  private String name;
  /**
   * 实体的desc
   */
  private String desc;
  /**
   * 实体的schema
   */
  private String schema;
  /**
   * 实体的字段映射
   */
  private List<EntityFieldMap> entityFieldMapList;

  private EntityMap() {
  }

  public static class Builder {
    private final EntityMap entityMap = new EntityMap();

    public Builder(String name, String desc, List<EntityFieldMap>  entityFieldMapList) {
      entityMap.name = name;
      entityMap.desc = desc;
      entityMap.entityFieldMapList = entityFieldMapList;
    }

    public Builder(String name, List<EntityFieldMap>  entityFieldMapList) {
      this(name, "", entityFieldMapList);
    }

    public Builder name(String name) {
      entityMap.name = name;
      return this;
    }
    public Builder desc(String desc) {
      entityMap.desc = desc;
      return this;
    }
    public Builder schema(String schema) {
      entityMap.schema = schema;
      return this;
    }
    public Builder entityFieldMapList(List<EntityFieldMap> entityFieldMapList) {
      entityMap.entityFieldMapList = entityFieldMapList;
      return this;
    }

    public EntityMap build() {
      return entityMap;
    }
  }

  public String getName() {
    return name;
  }

  public String getDesc() {
    return desc;
  }

  public String  getSchema() {
    return schema;
  }

  public List<EntityFieldMap> getEntityFieldMapList() {
    return entityFieldMapList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EntityMap that = (EntityMap) o;

    return name != null && name.equals(that.name);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityMap{");
    sb.append("name='").append(name).append('\'');
    sb.append(", desc='").append(desc).append('\'');
    sb.append(", schema=").append(schema).append('\'');
    sb.append(", fieldList=[").append(schema).append('\'');

    for(int i = 0;i < entityFieldMapList.size(); i++) {
      if (i > 0) sb.append(",");
      sb.append(" schema=").append(entityFieldMapList.get(i).toString()).append('\'');
    }
    sb.append("]").append(schema);
    sb.append('}');
    return sb.toString();
  }

}
