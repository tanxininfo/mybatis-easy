/*
 *
 *  *
 *  *  * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
 *  *  *
 *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  * you may not use this file except in compliance with the License.
 *  *  * You may obtain a copy of the License at
 *  *  *
 *  *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  * See the License for the specific language governing permissions and
 *  *  * limitations under the License.
 *  *
 *
 */

package com.mybatiseasy.core.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class BaseAttributeTypeHandler<T> extends BaseTypeHandler<Object> {

    private final JavaType javaType;

    /**
     * ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 构造方法
     */
    public BaseAttributeTypeHandler() {
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        Type type = resolvableType.as(BaseAttributeTypeHandler.class).getGeneric().getType();
        javaType = constructType(type);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
    }

    public static JavaType constructType(Type type) {
        Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
        return TypeFactory.defaultInstance().constructType(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            ps.setString(i, OBJECT_MAPPER.writeValueAsString(parameter));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return convertToEntityAttribute(value);
    }


    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToEntityAttribute(rs.getString(columnIndex));
    }


    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return convertToEntityAttribute(value);
    }


    public Object convertToEntityAttribute(String dbData) {
        if (dbData.isEmpty()) {
            if (List.class.isAssignableFrom(javaType.getRawClass())) {
                return Collections.emptyList();
            } else if (Set.class.isAssignableFrom(javaType.getRawClass())) {
                return Collections.emptySet();
            } else if (Map.class.isAssignableFrom(javaType.getRawClass())) {
                return Collections.emptyMap();
            } else {
                return null;
            }
        }
        return toObject(dbData, javaType);
    }


    public static <T> T toObject(String json, JavaType javaType) {
        Assert.hasText(json, "[Assertion failed] - this json must have text; it must not be null, empty, or blank");
        Assert.notNull(javaType, "[Assertion failed] - javaType is required; it must not be null");
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (com.fasterxml.jackson.core.JsonParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}