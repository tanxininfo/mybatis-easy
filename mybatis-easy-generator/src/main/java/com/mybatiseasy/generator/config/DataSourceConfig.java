package com.mybatiseasy.generator.config;


import com.mybatiseasy.generator.utils.Utils;
import org.springframework.util.Assert;

public class DataSourceConfig {

    private String url;
    private String username;
    private String password;

    private String schema;

    public String getUrl(){ return url;}
    public String getUsername(){ return username;}
    public String getPassword(){ return password;}

    public String getSchema() {
        return schema;
    }

    public static class Builder{

        private final DataSourceConfig config = new DataSourceConfig();

        public Builder(String url, String username, String password){
            this.url(url);
            config.username = username;
            config.password = password;
        }

        public Builder url(String url){
            config.url = url;
            if(Utils.isNotEmpty(url)) {
                String[] strArr = url.split("/");
                strArr = strArr[strArr.length - 1].split("\\?");
                String schema = strArr[0];
                schema(schema);
            }
            return this;
        }

        public Builder username(String username){
            config.username = username;
            return this;
        }

        public Builder password(String password){
            config.password = password;
            return this;
        }


        public Builder schema(String schema){
            config.schema = schema;
            return this;
        }

        public DataSourceConfig build(){
            Assert.hasLength(config.url, "url不得为空");
            Assert.hasLength(config.username, "username不得为空");
            Assert.hasLength(config.password, "password不得为空");
            return this.config;
        }
    }

}
