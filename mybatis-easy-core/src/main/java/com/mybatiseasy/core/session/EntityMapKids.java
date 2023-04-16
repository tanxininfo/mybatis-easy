package com.mybatiseasy.core.session;

import com.mybatiseasy.core.annotations.Table;
import com.mybatiseasy.core.annotations.TableField;
import com.mybatiseasy.core.annotations.TableId;
import com.mybatiseasy.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EntityMapKids {
    public static EntityMap reflectEntity(String entityName)   {
        try {
            Class<?> entityClass = Class.forName(entityName);
            Table table = AnnotationUtils.findAnnotation(entityClass, Table.class);
            if(table == null) return null;

            List<EntityFieldMap> entityFieldMapList = new ArrayList<>();
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                entityFieldMapList.add(reflectEntityMap(field));
            }

            return new EntityMap.Builder(table.name(), table.desc()).schema(table.schema()).entityFieldMapList(entityFieldMapList).build();
        }catch (Exception ignored){
            return null;
        }
    }

    public static EntityFieldMap reflectEntityMap(Field field){
        TableField tableField = AnnotationUtils.findAnnotation(field, TableField.class);
        TableId tableId = AnnotationUtils.findAnnotation(field, TableId.class);
        String name = field.getName();
        String column = StringUtil.camelToSnake(field.getName());
        EntityFieldMap.Builder builder = new EntityFieldMap.Builder(name, column).javaType(field.getType());


        if(tableField!= null) {
            builder.desc(tableField.desc()).insertDefault(tableField.insertDefault())
                    .updateDefault(tableField.updateDefault()).jdbcType(tableField.jdbcType())
                    .isLarge(tableField.isLarge()).numericScale(tableField.numericScale())
                    .typeHandler(tableField.typeHandler());
            if(!tableField.column().isEmpty()) builder.column(tableField.column());
        }
        if(tableId!= null) builder.isId(true);
        return builder.build();

    }
}
