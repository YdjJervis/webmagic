package com.eccang.cxf;

import javax.jws.WebService;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 测试WebService调用
 * @date 2016/12/15 14:14
 */
@WebService
public interface TestWS {

    String doRequest(String param);
}
