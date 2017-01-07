package com.eccang.pojo;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 公共返回参数
 * @date 2016/11/19 9:42
 */
public class BaseRspParam {

    public String customerCode = "";
    public int status;
    public String msg = "";

    private boolean success;

    public BaseRspParam() {}

    public BaseRspParam(boolean success, int status) {
        this.success = success;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BaseRspParam{" +
                "customerCode='" + customerCode + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String toJson(){
        return new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(this);
    }
}