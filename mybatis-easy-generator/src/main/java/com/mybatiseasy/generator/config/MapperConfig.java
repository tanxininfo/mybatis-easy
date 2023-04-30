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
