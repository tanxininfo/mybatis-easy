package com.mybatiseasy.apt.generate;


import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FileWriter {
    private final ProcessingEnvironment processingEnv;
    private final Messager messager;

    private final String COLS_PACKAGE_NAME = "com.mybatiseasy.core.cols";
    private final String TABLES_PACKAGE_NAME = "com.mybatiseasy.core.tables";

    private Template colsTemplate;
    private Template tablesTemplate;

    public FileWriter(ProcessingEnvironment processingEnv, Messager messager) {
        this.processingEnv = processingEnv;
        this.messager = messager;
        this.init();
    }

    private void init() {
        try {
            Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

            config.setClassForTemplateLoading(getClass(), "/templates");
            colsTemplate = config.getTemplate("cols.java.ftl");
            tablesTemplate = config.getTemplate("tables.java.ftl");

            config.setDefaultEncoding("utf-8");
        } catch (Exception ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, "模板初始化失败" + ex.getMessage());
        }
    }

    /**
     * 通过JavaPoet生成新的源文件
     */
    public void createTableFile(Map<String, Object> root) {
        writeColsFile(root);
        writeTablesFile(root);
    }

    private void writeColsFile(Map<String, Object> paramsMap) {
        try {

            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(COLS_PACKAGE_NAME + "." + paramsMap.get("colClassName"));
            Writer writer = fileObject.openWriter();
            colsTemplate.process(paramsMap, writer);
            writer.close();
        } catch (Exception ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, "文件生成失败["+COLS_PACKAGE_NAME + "." + paramsMap.get("colClassName")+"]" + ex.getMessage());
        }
    }

    private void writeTablesFile(Map<String, Object> paramsMap) {
        try {

            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(TABLES_PACKAGE_NAME + "." + paramsMap.get("tableClassName"));
            Writer writer = fileObject.openWriter();
            tablesTemplate.process(paramsMap, writer);
            writer.close();
        } catch (Exception ex) {
            messager.printMessage(Diagnostic.Kind.ERROR, "文件生成失败["+TABLES_PACKAGE_NAME + "." + paramsMap.get("tableClassName")+"]" + ex.getMessage());
        }
    }
}
