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

import com.mybatiseasy.core.utils.SqlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类映射成对象
 */
public class Entity {

  /**
   * 实体的name
   */
  private String name;

  private String fullName;
  /**
   * 实体的desc
   */
  private String desc;
  /**
   * 实体的schema
   */
  private String schema;
  /**
   * 主键字段
   */
  private EntityField primaryFieldMap;

  /**
   * 乐观锁字段
   */
  private EntityField versionFieldMap;
  /**
   * 逻辑删除字段
   */
  private EntityField logicDeleteFieldMap;
  /**
   * 租户字段
   */
  private EntityField tenantIdFieldMap;

  /**
   * 实体的字段映射
   */
  private List<EntityField> entityFieldList;

  private Entity() {
  }

  public String getName() {
    return name;
  }
  public String getFullName() {
    return fullName;
  }

  public String getDesc() {
    return desc;
  }

  public String  getSchema() {
    return schema;
  }

  public EntityField getPrimaryFieldMap() {
    return primaryFieldMap;
  }

  public EntityField getVersionFieldMap() {
    return versionFieldMap;
  }
  public EntityField getLogicDeleteFieldMap() {
    return logicDeleteFieldMap;
  }
  public EntityField getTenantIdFieldMap() {
    return tenantIdFieldMap;
  }

  public List<EntityField> getEntityFieldMapList() {
    return entityFieldList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Entity that = (Entity) o;

    return name != null && name.equals(that.name);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityMap{");
    sb.append("name='").append(name).append('\'');
    sb.append(", desc='").append(desc).append('\'');
    sb.append(", schema='").append(schema).append('\'');
    sb.append(", fieldList=[");

    for(int i = 0; i < entityFieldList.size(); i++) {
      if (i > 0) sb.append(",");
      sb.append(entityFieldList.get(i).toString());
    }
    sb.append("]");
    sb.append('}');
    return sb.toString();
  }


  public static class Builder {
    private final Entity entity = new Entity();

    public Builder(String name, String desc, List<EntityField> entityFieldList) {
      entity.name = name;
      entity.desc = desc;
      entity.entityFieldList = entityFieldList;
    }


    public Builder(String name, String desc) {
      this(name, desc, new ArrayList<>());
    }

    public Builder(String name, List<EntityField> entityFieldList) {
      this(name, "", entityFieldList);
    }

    public Builder name(String name) {
      entity.name = name;
      return this;
    }
    public Builder fullName(String fullName) {
      entity.fullName = fullName;
      return this;
    }
    public Builder desc(String desc) {
      entity.desc = desc;
      return this;
    }
    public Builder schema(String schema) {
      entity.schema = schema;
      return this;
    }

    public Builder primaryFieldMap(EntityField primaryFieldMap) {
      entity.primaryFieldMap = primaryFieldMap;
      return this;
    }

    public Builder versionFieldMap(EntityField versionFieldMap) {
      entity.versionFieldMap = versionFieldMap;
      return this;
    }
    public Builder logicDeleteFieldMap(EntityField logicDeleteFieldMap) {
      entity.logicDeleteFieldMap = logicDeleteFieldMap;
      return this;
    }
    public Builder tenantIdFieldMap(EntityField tenantIdFieldMap) {
      entity.tenantIdFieldMap = tenantIdFieldMap;
      return this;
    }

    public Builder entityFieldMapList(List<EntityField> entityFieldList) {
      entity.entityFieldList = entityFieldList;
      return this;
    }

    public Entity build() {
      entity.name = SqlUtil.addBackquote(entity.name);
      return entity;
    }
  }


}
