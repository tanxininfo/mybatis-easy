package com.mybatiseasy.core.session;

import com.mybatiseasy.core.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

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


    public Builder(String name, String desc) {
      this(name, desc, new ArrayList<>());
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
      entityMap.name = StringUtil.addBackquote(entityMap.name);
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
    sb.append(", schema='").append(schema).append('\'');
    sb.append(", fieldList=[");

    for(int i = 0;i < entityFieldMapList.size(); i++) {
      if (i > 0) sb.append(",");
      sb.append(entityFieldMapList.get(i).toString());
    }
    sb.append("]").append(schema);
    sb.append('}');
    return sb.toString();
  }

}
