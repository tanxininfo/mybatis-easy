package com.mybatiseasy.core.base;

import com.mybatiseasy.core.consts.Method;
import com.mybatiseasy.core.consts.MethodParam;
import com.mybatiseasy.core.provider.SqlProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * 所有Entity对应Mapp氏  er通过继承该接口取得CRUD功能。
 */
public class BaseEntity<T> {
    /**
     * 旧数据,用以比较是否更新了数据
     */
    protected T oldData;

    protected void setOldData(T data){
        oldData = data;
    }

    /**
     * 保存数据
     */
    public void insert(){

    }

    /**
     * 更新数据
     */
    public void update(){

    }
}
