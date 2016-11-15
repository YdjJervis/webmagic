package us.codecraft.webmagic.samples.base.service;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.samples.base.util.SerializeUtil;

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
        redisClient.flushDB();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    protected static Jedis createRedis() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.0.121");
        return pool.getResource();
    }
}