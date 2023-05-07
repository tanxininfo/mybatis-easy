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

package com.mybatiseasy.generator.config;

import com.mybatiseasy.emums.TableIdType;
import com.mybatiseasy.generator.utils.Utils;
import com.mybatiseasy.keygen.IKeyGenerator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


public class GlobalConfig {

    /**
     * 输出目录的总目录
     * 如：d:\project\com-mybatiseasy-generator\src\main\java
     */
    private String baseDir;
    /**
     * 父包名,如：
     */
    private String packageName;
    /**
     * 作者名
     */
    private String author;
    /**
     * 注释日期
     */
    private String commentDate;
    /**
     * 表like过滤 like
     */
    private List<String> tableLikeList;
    /**
     * 表like过滤 not like
     */
    private List<String> tableNotLikeList;

    private List<TemplateType> templateTypeList;

    private TableIdType idType;
    private String  sequence;
    private Class<? extends IKeyGenerator>  keyGenerator;

    public TableIdType getIdType() {
        return idType;
    }

    public String getSequence() {
        return sequence;
    }

    public Class<? extends IKeyGenerator> getKeyGenerator() {
        return keyGenerator;
    }

    public String getBaseDir(){return this.baseDir;}

    public String getPackageName() {
        return packageName;
    }

    public String getAuthor(){return this.author;}
    public String getCommentDate(){return this.commentDate;}
    public List<String> getTableLikeList(){return this.tableLikeList;}
    public List<String> getTableNotLikeList(){return this.tableNotLikeList;}
    public List<TemplateType> getTemplateTypeList(){return this.templateTypeList;}

    public static class Builder{

        private final GlobalConfig config = new GlobalConfig();
        public Builder(String baseDir, String packageName){
            config.baseDir = baseDir;
            config.packageName = packageName;
        }

        public Builder baseDir(String baseDir){
            config.baseDir = baseDir;
            return this;
        }

        public Builder templateType(TemplateType ...templateTypes) {
            if(config.templateTypeList == null ) config.templateTypeList = new ArrayList<>();

            for (TemplateType templateType : templateTypes
            ) {
                if (!config.templateTypeList.contains(templateType)) config.templateTypeList.add(templateType);

            }
            return this;
        }

        public Builder packageName(String packageName) {
            config.packageName = packageName;
            return this;
        }


        public Builder idType(TableIdType idType) {
            config.idType = idType;
            return this;
        }

        public Builder sequence(String sequence) {
            config.sequence = sequence;
            return this;
        }

        public Builder keyGenerator(Class<? extends IKeyGenerator> keyGenerator) {
            config.keyGenerator = keyGenerator;
            return this;
        }


        public Builder addTableLike(String like) {
            if (!config.tableLikeList.contains(like)) config.tableLikeList.add(like);
            return this;
        }

        public Builder addTableNotLike(String like) {
            if (!config.tableNotLikeList.contains(like)) config.tableNotLikeList.add(like);
            return this;
        }
        public GlobalConfig build(){
            if(Utils.isEmpty(config.packageName)) packageName("com.mybatiseasy");
            if(config.getTemplateTypeList()==null) templateType(TemplateType.ALL);
            return this.config;
        }

    }
}
