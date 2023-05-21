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

package com.mybatiseasy.apt.generate;


import com.mybatiseasy.annotation.Table;
import com.mybatiseasy.annotation.TableField;


import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 映射Entity信息
 */
public class TableInfo {

     Map<String, Object> root = new HashMap<>();

    private Table table;
    private final Messager messager;

    public TableInfo(Messager messager){
        this.messager = messager;
    }

    public Map<String, Object> getFrom(Element element){

        //类元素
        TypeElement classElement = (TypeElement) element;

        this.table = classElement.getAnnotation(Table.class);
        this.setClassName(classElement);
        this.setTableName(classElement);
        this.setColumnList(classElement);
        return this.getRoot();

    }

    private void setClassName(TypeElement classElement) {
        root.put("entityClassFullName", classElement);
        root.put("entityClassName", Utils.uncapitalize(classElement.getSimpleName().toString()));
        root.put("defClassName", Utils.capitalize(classElement.getSimpleName().toString()) + "Def");
        root.put("tableClassName",  Utils.camelToSnake(Utils.uncapitalize(classElement.getSimpleName().toString())).toUpperCase());
    }

    private void setTableName(TypeElement classElement){
        if(root.get("entityClassName") == null) this.setClassName(classElement);
        String tableName = Utils.isEmpty(table.name())? Utils.camelToSnake(root.get("entityClassName").toString()): table.name();
        root.put("tableName", tableName);
    }

    private void setColumnList(TypeElement classElement){
        List<? extends Element> fieldElements = classElement.getEnclosedElements();
        List<Map<String, Object>> columnList = new ArrayList<>();
        for (Element element: fieldElements
             ) {
            if(element.getKind() == ElementKind.FIELD){
                VariableElement fieldElement = (VariableElement) element;
                Map<String, Object> column = new HashMap<>();
                column.put("name", fieldElement.getSimpleName().toString());
                column.put("capitalName", Utils.camelToSnake(fieldElement.getSimpleName().toString()).toUpperCase());
                column.put("column", Utils.camelToSnake(column.get("name").toString()));

                TableField tableField = element.getAnnotation(TableField.class);
                if(tableField != null){
                   if(Utils.isNotEmpty(tableField.column())) {
                       column.put("column", tableField.column());
                   }
                }
                columnList.add(column);
            }
        }
        root.put("columnList", columnList);
    }

    public Map<String, Object> getRoot(){
        return this.root;
    }


}
