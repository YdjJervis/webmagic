package com.eccang.amazon.grammar;

import junit.framework.TestCase;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/11/8 15:10
 */
public class RedisTest extends TestCase {

    @Test
    public void testConnect(){
        Jedis jedis = new Jedis("192.168.0.121",6379);
//        Jedis jedis = new Jedis("127.0.0.1",6379);

        System.out.println(jedis.get("liugang"));
    }
}