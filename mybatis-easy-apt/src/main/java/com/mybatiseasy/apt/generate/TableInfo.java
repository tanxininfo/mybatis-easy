package com.mybatiseasy.apt.generate;


import com.mybatiseasy.annotation.Table;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;


/**
 * 映射Entity信息
 */
public class TableInfo {

    /**
     * 类文件名
     */
    private String className;
    /**
     * 真实表名
     */
    private String tableName;

    private Table table;
    private Messager messager;

    public TableInfo(Messager messager){
        this.messager = messager;
    }

    public void getFrom(Element element){
        this.messager = messager;

        //类元素
        TypeElement classElement = (TypeElement) element;

        this.table = classElement.getAnnotation(Table.class);
        this.setClassName(classElement);
        this.setTableName(classElement);


    }

    private void setClassName(TypeElement classElement){
        className = Utils.uncapitalize(classElement.getSimpleName().toString());
    }

    private void setTableName(TypeElement classElement){
        if(Utils.isEmpty(className)) this.setClassName(classElement);
        tableName = Utils.isEmpty(table.name())? Utils.camelToSnake(className): table.name();
    }


}
