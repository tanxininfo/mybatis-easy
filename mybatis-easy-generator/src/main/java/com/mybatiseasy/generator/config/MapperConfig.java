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

import com.mybatiseasy.generator.utils.Utils;


public class MapperConfig {

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


    public String getSuffix() {
        return suffix;
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


    public static class Builder{

        private final MapperConfig config = new MapperConfig();

        public Builder(String packageName, String suffix){
            config.packageName = packageName;
            config.suffix = suffix;
        }

        public Builder supperClass(Class<?> supperClass) {
            config.supperClass = supperClass;
            return this;
        }


        public Builder override(boolean override){
            config.override = override;
            return this;
        }

        public MapperConfig build(){
            if(Utils.isEmpty(config.suffix)) config.suffix = "Mapper";
            if(Utils.isEmpty(config.packageName)) config.packageName = "mapper";
            return this.config;
        }
    }

}
