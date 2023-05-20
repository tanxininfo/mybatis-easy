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

import com.mybatiseasy.generator.pojo.ColumnAutoSet;
import com.mybatiseasy.generator.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityConfig {

    /**
     * 设置父类
     */
    private Class<?> supperClass;

    private String suffix;
    /**
     * 包名
     */
    private String packageName;


    /**
     * 是否覆盖已有文件
     */
    private boolean override;

    /**
     * 是否链式
     */
    private boolean chain;

    /**
     * 是否支持swagger
     */
    private boolean swagger;

    /**
     * 是否启用lombok
     */
    private boolean enableLombok;

    /**
     * version的字段名称
     */
    private String versionName;

    /**
     * 逻辑删除实体属性名称
     */
    private String logicDeleteName;

    /**
     * 租户Id字段名称
     */
    private String tenantIdName;

    /**
     * 自动填充的字段
     */
    private final List<ColumnAutoSet> columnAutoSetList = new ArrayList<>();

    private final List<String> prefix = new ArrayList<>();

    public List<ColumnAutoSet> getColumnAutoSetList() {
        return columnAutoSetList;
    }

    public String getVersionName() {
        return versionName;
    }

    public boolean isChain() {
        return chain;
    }

    public boolean isSwagger() {
        return swagger;
    }

    public List<String> getPrefix() {
        return prefix;
    }
    public String getSuffix() {
        return suffix;
    }
    public String getTenantIdName() {
        return tenantIdName;
    }

    public String getPackageName() {
        return packageName;
    }

    public Class<?> getSupperClass() {
        return supperClass;
    }


    public boolean isOverride() {
        return override;
    }

    public boolean isEnableLombok() {
        return enableLombok;
    }

    public String getLogicDeleteName() {
        return logicDeleteName;
    }

    public static class Builder{

        private final EntityConfig config = new EntityConfig();

        public Builder(String packageName, String suffix){
            config.packageName = packageName;
            config.suffix = suffix;
        }

        public Builder supperClass(Class<?> supperClass) {
            config.supperClass = supperClass;
            return this;
        }


        public Builder columnAutoSet(ColumnAutoSet columnAutoSet){
            if(config.getColumnAutoSetList().stream().noneMatch(item -> item.getName().equals(columnAutoSet.getName())))
                config.columnAutoSetList.add(columnAutoSet);
            return this;
        }

        public Builder override(boolean override){
            config.override = override;
            return this;
        }

        public Builder enableLombok(boolean enableLombok){
            config.enableLombok = enableLombok;
            return this;
        }

        public Builder chain(boolean chain){
            config.chain = chain;
            return this;
        }

        public Builder prefix(String ... prefix){
            Collections.addAll(config.prefix, prefix);
            return this;
        }

        public Builder swagger(boolean swagger){
            config.swagger = swagger;
            return this;
        }

        public Builder versionName(String versionName){
            config.versionName = versionName;
            return this;
        }

        public Builder logicDeleteName(String logicDeleteName){
            config.logicDeleteName = logicDeleteName;
            return this;
        }
        public Builder tenantIdName(String tenantIdName){
            config.tenantIdName = tenantIdName;
            return this;
        }
        public EntityConfig build(){
            if(Utils.isEmpty(config.suffix)) config.suffix = "";
            if(Utils.isEmpty(config.packageName)) config.packageName = "entity";
            return this.config;
        }
    }

}
