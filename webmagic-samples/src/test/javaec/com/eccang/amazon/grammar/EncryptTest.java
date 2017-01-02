package com.eccang.amazon.grammar;

import junit.framework.TestCase;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 加密测试
 * @date 2016/10/29 10:42
 */
public class EncryptTest extends TestCase {

    @Test
    public void testMD5() {
        System.out.println(DigestUtils.md2Hex("nobody nobody but you..."));
    }
}