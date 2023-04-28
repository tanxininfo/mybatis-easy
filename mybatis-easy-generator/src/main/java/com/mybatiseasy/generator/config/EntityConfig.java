package com.mybatiseasy.generator.config;

import org.springframework.util.Assert;

public class EntityConfig {

    /**
     * 设置父类
     */
    private Class<?> supperClass;

    private String suffix;

    private String supperClassFull;

    /**
     * 是否覆盖已有文件
     */
    private boolean override;

    /**
     * 是否启用lombok
     */
    private boolean enableLombok;

    /**
     * 逻辑删除数据库字段
     */
    private String logicDeleteColumn;
    /**
     * 逻辑删除实体属性名称
     */
    private String logicDeleteName;

    public Class<?> getSupperClass() {
        return supperClass;
    }

    public String getSupperClassFull() {
        return supperClassFull;
    }

    public boolean isOverride() {
        return override;
    }

    public boolean isEnableLombok() {
        return enableLombok;
    }

    public String getLogicDeleteColumn() {
        return logicDeleteColumn;
    }

    public String getLogicDeleteName() {
        return logicDeleteName;
    }

    public static class Builder{

        private final EntityConfig config = new EntityConfig();

        public Builder(boolean override){
            config.override = override;
        }

        public Builder supperClass(Class<?> supperClass) {
            config.supperClass = supperClass;
            if (supperClass != null)
                config.supperClassFull = supperClass.getTypeName();
            return this;
        }

        public Builder supperClass(String supperClass){
            config.supperClassFull = supperClass;
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


        public Builder logicDeleteColumn(String logicDeleteColumn){
            config.logicDeleteColumn = logicDeleteColumn;
            return this;
        }
        public Builder logicDeleteName(String logicDeleteName){
            config.logicDeleteName = logicDeleteName;
            return this;
        }
        public EntityConfig build(){
            Assert.hasLength(config.url, "url不得为空");
            Assert.hasLength(config.username, "username不得为空");
            Assert.hasLength(config.password, "password不得为空");
            return this.config;
        }
    }

}
