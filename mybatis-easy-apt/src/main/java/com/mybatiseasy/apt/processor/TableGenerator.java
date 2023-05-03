/*
 * Copyright (c) 2023-2033, 杭州坦信科技有限公司 (https://www.mybatis-easy.com).
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

package com.mybatiseasy.apt.processor;

import com.google.auto.service.AutoService;
import com.mybatiseasy.apt.generate.FileWriter;
import com.mybatiseasy.apt.generate.TableInfo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Map;
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
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("process entity  entry"));
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
                    Map<String, Object> root = tableInfo.getFrom(element);
                    writer.createTableFile(root);
                    messager.printMessage(Diagnostic.Kind.NOTE, String.format("process entity end: %s", elementName));
                }
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }

        return false;
    }


}
