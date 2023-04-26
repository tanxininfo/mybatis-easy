package com.mybatiseasy.apt.processor;

import com.google.auto.service.AutoService;
import com.mybatiseasy.apt.generate.FileWriter;
import com.mybatiseasy.apt.generate.TableInfo;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("com.mybatiseasy.annotation.Table")
@Slf4j
public class TableGenerator extends AbstractProcessor {

    private Messager messager;
    protected ProcessingEnvironment processingEnv;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        this.processingEnv = processingEnv;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        TableInfo tableInfo = new TableInfo(messager);
        FileWriter writer = new FileWriter(processingEnv, messager);

        for (TypeElement annotation : annotations) {
            try {
                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
                for (Element element : elements) {
                    Name elementName = element.getSimpleName();
                    messager.printMessage(Diagnostic.Kind.NOTE, String.format("process entity begin: %s", elementName));
                    tableInfo.getFrom(element);
                    writer.createTableFile(element);
                    messager.printMessage(Diagnostic.Kind.NOTE, String.format("process entity end: %s", elementName));
                }
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }

        return false;
    }


}
