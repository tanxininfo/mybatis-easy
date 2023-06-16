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
package com.mybatiseasy.core.typehandler;

import com.mybatiseasy.annotation.EnumValue;
import com.mybatiseasy.core.config.GlobalConfig;
import com.mybatiseasy.core.utils.MetaObjectUtil;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Clinton Begin
 */
public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

  private final Class<E> type;
  private final E[] enums;

  private Invoker getInvoker;

  public EnumTypeHandler(Class<E> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
    this.enums = type.getEnumConstants();
    if (this.enums == null) {
      throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
    }
    this.initEnum();
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
    if (jdbcType == null) {
      ps.setObject(i, this.getValue(parameter));
    } else {
      ps.setObject(i, this.getValue(parameter), jdbcType.TYPE_CODE);
    }
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Object dbValue = rs.getObject(columnName);
    if (dbValue == null) {
      return null;
    }
    return getEnumType(dbValue);
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Object dbValue = rs.getObject(columnIndex);
    if (dbValue == null) {
      return null;
    }
    return getEnumType(dbValue);
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Object dbValue = cs.getObject(columnIndex);
    if (dbValue == null) {
      return null;
    }
    return getEnumType(dbValue);
  }

  /**
   * 取得数据库值对应的EnumType
   * @param dbValue 数据库值
   * @return EnumType
   */
  @SuppressWarnings("unchecked")
  private E getEnumType(Object dbValue) {
    try {
      return (E)GlobalConfig.getEnumTypeMap(this.type).get(dbValue);
    } catch (Exception ex) {
      throw new IllegalArgumentException(
              "Cannot convert " + dbValue + " to " + type.getSimpleName() + " by ordinal value.", ex);
    }
  }

  /**
   * 第一次访问Enum, 反射并进行缓存
   */
  private void initEnum() {
    if (GlobalConfig.existsEnumTypeMap(this.type)) return;

    MetaClass metaClass = MetaObjectUtil.forClass(this.type);
    String name = getValueColumn();
    getInvoker = metaClass.getGetInvoker(name);

    Map<Object, E> enumTypeMap = new HashMap<>();
    Map<String, Object> enumValueMap = new HashMap<>();
    for (E anEnum : this.enums) {
      Object value = getValue(anEnum);
      enumTypeMap.put(value, anEnum);
      enumValueMap.put(anEnum.name(), value);
    }
    GlobalConfig.addEnumTypeMap(this.type, enumTypeMap);
    GlobalConfig.addEnumValueMap(this.type, enumValueMap);
  }


  /**
   * 取得EnumValue标注的属性名
   * @return 属性名称
   */
  private String getValueColumn() {
      Field field = Arrays.stream(this.type.getDeclaredFields()).filter(item-> item.getAnnotation(EnumValue.class)!=null).findFirst().orElse(null);
      assert field != null;
      return field.getName();
  }

  private Object getValue(E enumType) {
    try {
      return getInvoker.invoke(enumType, new Object[0]);
    } catch (Exception ex) {
      throw new RuntimeException("Cannot get value of an enum type [" + enumType.name() + "] of " + this.type);
    }
  }
}
