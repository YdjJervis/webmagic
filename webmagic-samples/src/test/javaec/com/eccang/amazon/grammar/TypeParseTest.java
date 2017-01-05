package com.eccang.amazon.grammar;

import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.asin.AsinRsp;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/1/5 10:45
 */
public class TypeParseTest extends TestCase {

    @Test
    public void testParentToChild() {
        BaseRspParam rspParam = new AsinRsp();
        rspParam.customerCode = "001";

        AsinRsp asinRsp = (AsinRsp) rspParam;
        System.out.println(asinRsp.customerCode);
    }
}
