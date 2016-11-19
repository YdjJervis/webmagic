package com.eccang.cxf;


import com.eccang.pojo.BaseRspParam;

/**
 * @author Jervis
 * @version V0.20
 * @Description:
 * @date 2016/11/17 14:19
 */
public interface SpiderWS {

    /**
     * @param json 客户端调用的参数
     * @return true-鉴权成功/false-鉴权失败
     */
    BaseRspParam auth(String json);
}
