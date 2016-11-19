package com.eccang.pojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 公共请求参数
 * @date 2016/11/19 9:42
 */
public class BaseReqParam {

    public String cutomerCode;
    public String platformCode;
    public String token;

    @Override
    public String toString() {
        return "BaseReqParam{" +
                "cutomerCode='" + cutomerCode + '\'' +
                ", platformCode='" + platformCode + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}