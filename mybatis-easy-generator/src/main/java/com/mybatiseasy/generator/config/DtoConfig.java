package com.mybatiseasy.generator.config;

import com.mybatiseasy.generator.utils.Utils;

public class DtoConfig {

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


    public boolean isChain() {
        return chain;
    }

    public boolean isSwagger() {
        return swagger;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isOverride() {
        return override;
    }

    public boolean isEnableLombok() {
        return enableLombok;
    }

    public static class Builder{

        private final DtoConfig config = new DtoConfig();

        public Builder(String packageName, String suffix){
            config.packageName = packageName;
            config.suffix = suffix;
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

        public Builder swagger(boolean swagger){
            config.swagger = swagger;
            return this;
        }

        public DtoConfig build(){
            if(Utils.isEmpty(config.suffix)) config.suffix = "Dto";
            if(Utils.isEmpty(config.packageName)) config.packageName = "dto";
            return this.config;
        }
    }
}
