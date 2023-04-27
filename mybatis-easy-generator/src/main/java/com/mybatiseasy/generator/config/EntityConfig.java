package com.mybatiseasy.generator.config;

import org.springframework.util.Assert;

public class EntityConfig {

    /**
     * 设置父类
     */
    private Class<?> supperClass;
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
    private boolean logicDeleteColumn;
    /**
     * 逻辑删除实体属性名称
     */
    private boolean logicDeleteName;
    private String password;

    public String getUrl(){ return url;}
    public String getUsername(){ return username;}
    public String getPassword(){ return password;}

    public static class Builder{

        private final EntityConfig config = new EntityConfig();

        public Builder(String url, String username, String password){
            config.url = url;
            config.username = username;
            config.password = password;
        }

        public Builder url(String url){
            config.url = url;
            return this;
        }

        public Builder username(String username){
            config.username = username;
            return this;
        }

        public Builder password(String password){
            config.password = password  `   `   ``  `   `;
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
