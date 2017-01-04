package com.eccang.amazon.util;

import org.junit.Test;
import redis.clients.jedis.BinaryClient;
import com.eccang.spider.amazon.util.RedisUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hardy
 * @version V0.1
 */
public class RedisUtilsTest {
    @Test
    public void setStringTest() {
        RedisUtils.setString("redisStringTest", "hana");

        RedisUtils.setString("date1", 5000, "过期时间1");
    }

    @Test
    public void getStringTest() {
        System.out.println(RedisUtils.getString("date"));
    }

    @Test
    public void hset() {
        RedisUtils.hset("page", "http://www.amazon.com?a=1", "gyeghie");
    }

    @Test
    public void hmsetTest() {
        String[] str1 = new String[]{"1", "2", "3"};
        RedisUtils.hmset("name", str1, str1);
    }

    @Test
    public void hmsetMapTest() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "xxx");
        map.put("sex", "man");
        map.put("age", "26");
        System.out.println(RedisUtils.hmset("addHashMap", map));
    }

    @Test
    public void hsetNXTest() {
        System.out.println(RedisUtils.hsetNX("addHashMap", "weight", "60KG"));
    }

    @Test
    public void hgetTest() {
        System.out.println(RedisUtils.hget("page", "https://www.amazon.com/gp/offer-listing/0071809252"));
    }

    @Test
    public void hmgetTest() {
        System.out.println(RedisUtils.hmget("addHashMap", "name", "sex", "age"));

    }

    @Test
    public void hgetAllTest() {
        System.out.println(RedisUtils.hgetAll("addHashMap"));
    }

    @Test
    public void hdelTest() {
        RedisUtils.hdel("addHashMap", "name");
    }

    @Test
    public void hexistsTest() {
        System.out.println(RedisUtils.hexists("addHashMap", "age"));
        System.out.println(RedisUtils.hexists("addHashMap", "name"));
    }

    @Test
    public void hincrByTest() {
        RedisUtils.hincrBy("addHashMap", "age", 2);
    }

    @Test
    public void hkeysTest() {
        System.out.println(RedisUtils.hkeys("addHashMap"));
    }

    @Test
    public void hvalsTest() {
        System.out.println(RedisUtils.hvals("addHashMap"));
    }

    @Test
    public void hlenTest() {
        System.out.println(RedisUtils.hlen("addHashMap"));
    }

    @Test
    public void lpushTest() {
        RedisUtils.lpush("list", "test1");
        RedisUtils.lpush("list", "test3");
    }

    @Test
    public void lrangeTest() {
        System.out.println(RedisUtils.lrange("list", 0, 1));
    }

    @Test
    public void lindexTest() {
        System.out.println(RedisUtils.lindex("list", 1));

    }

    @Test
    public void llenTest() {
        System.out.println(RedisUtils.llen("list"));
    }

    @Test
    public void linsertTest() {
        System.out.println(RedisUtils.linsert("list", BinaryClient.LIST_POSITION.BEFORE, "test3", "before"));
    }

    @Test
    public void lpushxTest() {
        RedisUtils.lpushx("list", "addFirst");
    }

    @Test
    public void lremTest() {
        System.out.println(RedisUtils.lrem("list", 1, "test1"));
    }

    @Test
    public void lsetTest() {
        RedisUtils.lset("list", 0, "addFirstTest");
    }

    @Test
    public void ltrimTest() {
        RedisUtils.ltrim("list", 0, 4);
    }

    @Test
    public void lpopTest() {
        System.out.println(RedisUtils.lpop("list"));
    }

    @Test
    public void rpopTest() {
        System.out.println(RedisUtils.rpop("list"));
    }

    @Test
    public void rpushTest() {
        System.out.println(RedisUtils.rpush("list", "end"));
    }

    @Test
    public void rpushxTest() {
        System.out.println(RedisUtils.rpushx("list", "end1"));
    }

    @Test
    public void rpopLpushTest() {
        System.out.println(RedisUtils.rpopLpush("list", "list1"));
    }

    @Test
    public void blpopTest() {
        List<String> result = RedisUtils.blpop(3000, "list", "list1");
        for (String s : result) {
            System.out.println(s);
        }
    }

    @Test
    public void brpopTest() {
        List<String> result = RedisUtils.brpop(3000, "list1", "list");
        for (String s : result) {
            System.out.println(s);
        }
    }

    @Test
    public void brpopLpushTest() {
        System.out.println(RedisUtils.brpopLpush("list1", "list", 3000));
    }
}