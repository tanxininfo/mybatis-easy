package com.mybatiseasy.apt.generate;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FileWriter {
    private ProcessingEnvironment processingEnv;
    private Messager messager;

    private String COLS_PACKAGE_NAME = "com.mybatiseasy.core.cols";
    private String TABLES_PACKAGE_NAME = "com.mybatiseasy.core.tables";

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
    public void createTableFile(Element element) {
        Map<String, Object> paramsMap = new HashMap<>();
        writeColsFile(paramsMap);
    }


    private void writeColsFile(Map<String, Object> paramsMap) {
        try {
            String className = "_USER";
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(TABLES_PACKAGE_NAME + "." + className);
            Writer writer = fileObject.openWriter();
            colsTemplate.process(paramsMap, writer);
            writer.close();
        } catch (Exception e) {

        }


    }
}
