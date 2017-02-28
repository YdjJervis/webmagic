package com.eccang.spider.base.service;

import com.eccang.spider.base.util.SerializeUtil;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/8 16:27
 */
public class RedisCacheService implements Cache {

    private static Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
    private Jedis redisClient = createRedis();

    /** The ReadWriteLock. */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String id;

    public RedisCacheService(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("==================== MybatisRedisCache : id = "+id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getSize() {

        return Integer.valueOf(redisClient.dbSize().toString());
    }

    @Override
    public void putObject(Object key, Object value) {
        logger.debug("====================  putObject:"+key+"="+value);
        try {
            redisClient.set(key.toString().getBytes("utf-8"), value.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public Object getObject(Object key) {
        Object value = null;
        try {
            value = SerializeUtil.unserialize(redisClient.get(key.toString().getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        }
        logger.debug("=================== getObject:"+key+"="+value);
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        return redisClient.expire(SerializeUtil.serialize(key.toString()),0);
    }

    @Override
    public void clear() {
//        redisClient.flushDB();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    protected static Jedis createRedis() {
//        JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.100.110");
//        Jedis jedis = pool.getResource();
        /**以下参数分别填写您的redis实例内网IP，端口号，实例id和密码*/
        String host = "10.66.136.127";
        int port = 6379;
        String instanceid = "crs-0nl0zy8q";
        String password = "Olwpocj*9";
        //连接redis
        Jedis jedis = new Jedis(host, port);
        //鉴权
        jedis.auth(instanceid + ":" + password);
        return jedis;
    }
}