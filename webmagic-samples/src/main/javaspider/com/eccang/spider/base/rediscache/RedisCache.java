/**
 * Copyright 2015 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eccang.spider.base.rediscache;

import org.apache.ibatis.cache.Cache;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Cache adapter for Redis.
 *
 * @author Eduardo Macarron
 */
public final class RedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new DummyReadWriteLock();

    private String id;

    private static JedisPool pool;

    public RedisCache(){
        pool = getPool();
    }

    public RedisCache(final String id) {
        System.out.println("RedisCache(final String id)::" + id);
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;

        pool = getPool();
    }

    // TODO Review this is UNUSED
    private Object execute(RedisCallback callback) {
        System.out.println("execute(RedisCallback callback)::");
        Jedis jedis = pool.getResource();
        try {
            return callback.doWithRedis(jedis);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String getId() {
        System.out.println("getId()::" + id);
        return this.id;
    }

    @Override
    public int getSize() {
        System.out.println("getSize()::");
        return (Integer) execute(jedis -> {
            Map<byte[], byte[]> result = jedis.hgetAll(id.getBytes());
            return result.size();
        });
    }

    @Override
    public void putObject(final Object key, final Object value) {
        System.out.println("putObject(final Object key, final Object value)::key=" + key + " value=" + value);
        execute(jedis -> {
            jedis.hset(id.getBytes(), key.toString().getBytes(), SerializeUtil.serialize(value));
            return null;
        });
    }

    @Override
    public Object getObject(final Object key) {
        System.out.println("getObject(final Object key)::" + key);
        return execute(jedis -> SerializeUtil.unserialize(jedis.hget(id.getBytes(), key.toString().getBytes())));
    }

    @Override
    public Object removeObject(final Object key) {
        System.out.println("removeObject(final Object key)::" + key);
        return execute(jedis -> jedis.hdel(id, key.toString()));
    }

    @Override
    public void clear() {
        System.out.println("clear()::" + id);
        execute(jedis -> {
            jedis.del(id);
            return null;
        });

    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        System.out.println("getReadWriteLock()::");
        return readWriteLock;
    }

    @Override
    public String toString() {
        System.out.println("toString()::");
        return "Redis {" + id + "}";
    }

    public JedisPool getPool(){
        RedisConfig redisConfig = RedisConfigurationBuilder.getInstance().parseConfiguration();
        return new JedisPool(redisConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getConnectionTimeout(), redisConfig.getSoTimeout(), redisConfig.getPassword(),
                redisConfig.getDatabase(), redisConfig.getClientName(), redisConfig.isSsl(),
                redisConfig.getSslSocketFactory(), redisConfig.getSslParameters(), redisConfig.getHostnameVerifier());
    }

    public void flushDB(){
        pool.getResource().flushDB();
    }

}
