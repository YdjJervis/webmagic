package com.eccang.cxf.test;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 测试WebService实现类
 * @date 2016/12/15 14:16
 */
public class TestWSImpl implements TestWS {

    @Override
    public String doRequest(String param) {
        return "这简单的话语";
    }

}
