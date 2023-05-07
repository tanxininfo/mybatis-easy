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

public class ControllerConfig {
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
     * 是否支持swagger
     */
    private boolean swagger;

    /**
     * 是否@RestController
     */
    private boolean restful;

    /**
     * 是否覆盖已有文件
     */
    private boolean override;

    public boolean isRestful() {
        return restful;
    }

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

    public boolean isSwagger() {
        return swagger;
    }

    public static class Builder{

        private final ControllerConfig config = new ControllerConfig();

        public Builder(String packageName, String suffix){
            config.packageName = packageName;
            config.suffix = suffix;
        }

        public Builder supperClass(Class<?> supperClass) {
            config.supperClass = supperClass;
            return this;
        }


        public Builder swagger(boolean swagger){
            config.swagger = swagger;
            return this;
        }
        public Builder restful(boolean restful){
            config.restful = restful;
            return this;
        }

        public Builder override(boolean override){
            config.override = override;
            return this;
        }

        public ControllerConfig build(){
            if(Utils.isEmpty(config.suffix)) config.suffix = "Controller";
            if(Utils.isEmpty(config.packageName)) config.packageName = "controller";
            return this.config;
        }
    }
}
