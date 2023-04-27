package com.mybatiseasy.generator.config;

import lombok.Data;

import java.util.List;


public class GlobalConfig {

    /**
     * 输出目录的总目录
     */
    private String baseDir;
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

    public String getBaseDir(){return this.baseDir;}

    public String getAuthor(){return this.author;}
    public String getCommentDate(){return this.commentDate;}
    public List<String> getTableLikeList(){return this.tableLikeList;}
    public List<String> getTableNotLikeList(){return this.tableNotLikeList;}
    public List<TemplateType> getTemplateTypeList(){return this.templateTypeList;}

    public static class Builder{

        private final GlobalConfig config = new GlobalConfig();
        public Builder(String baseDir){
            config.baseDir = baseDir;
        }

        public Builder baseDir(String baseDir){
            config.baseDir = baseDir;
            return this;
        }

        public Builder templateType(TemplateType ...templateTypes) {
            for (TemplateType templateType : templateTypes
            ) {
                if (!config.templateTypeList.contains(templateType)) config.templateTypeList.add(templateType);

            }
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
            if(config.getTemplateTypeList()==null) templateType(TemplateType.ALL);
            return this.config;
        }

    }
}
