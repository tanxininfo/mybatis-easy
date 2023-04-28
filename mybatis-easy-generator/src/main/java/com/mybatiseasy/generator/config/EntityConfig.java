package com.mybatiseasy.generator.config;

import com.mybatiseasy.generator.utils.Utils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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

    public String getSuffix() {
        return suffix;
    }

    public String getPackageName() {
        return packageName;
    }

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

        public Builder(String packageName, String suffix){
            config.packageName = packageName;
            config.suffix = suffix;
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
            if(Utils.isEmpty(config.suffix)) config.suffix = "Entity";
            if(Utils.isEmpty(config.packageName)) config.packageName = "entity";
            return this.config;
        }
    }

}
