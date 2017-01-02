package com.eccang.spider.amazon.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/9 14:31
 */
public class RedisUtils {

    protected static Logger logger = Logger.getLogger(RedisUtils.class);

    //访问密码
//    private static String AUTH = FileUtil.getPropertyValue("/properties/redis.properties", "auth");

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private final static int MAX_ACTIVE = 8;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private final static int MAX_IDLE = 8;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private final static int MAX_WAIT = 3000;

    //超时时间
    private final static int TIMEOUT = 5000;

    /**
     * 成功,"OK"
     */
    private static final String SUCCESS_OK = "OK";

    /**
     * 成功,1L
     */
    private static final Long SUCCESS_STATUS_LONG = 1L;

    /**
     * 只用key不存在时才设置。Only set the key if it does not already exist
     */
    private static final String NX = "NX";

    /**
     * XX -- 只有key存在时才设置。和NX相反。Only set the key if it already exist.
     */
    private static final String XX = "XX";

    /**
     * EX|PX, 时间单位，EX是秒，PX是毫秒。expire time units: EX = seconds; PX = milliseconds
     */
    private static final String EX = "EX";

    /**
     * EX|PX, 时间单位，EX是秒，PX是毫秒。expire time units: EX = seconds; PX = milliseconds
     */

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private final static boolean TEST_ON_BORROW = false;

    private static JedisPool jedisPool = null;

    /**
     * redis过期时间,以秒为单位
     */
    public final static int EXRP_HOUR = 60 * 60;          //一小时
    public final static int EXRP_DAY = 60 * 60 * 24;        //一天
    public final static int EXRP_MONTH = 60 * 60 * 24 * 30;   //一个月

    /**
     * 初始化Redis连接池
     */
    private static void initialPool() {
        String address_array = "127.0.0.1,192.168.0.1";
        int port = 6379;
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxIdle(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, address_array.split(",")[0], port, TIMEOUT);
        } catch (Exception e) {
            logger.error("First create JedisPool error : " + e);
            try {
                //如果第一个IP异常，则访问第二个IP
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(MAX_ACTIVE);
                config.setMaxIdle(MAX_IDLE);
                config.setMaxIdle(MAX_WAIT);
                config.setTestOnBorrow(TEST_ON_BORROW);
                jedisPool = new JedisPool(config, address_array.split(",")[1], port, TIMEOUT);
            } catch (Exception e2) {
                logger.error("Second create JedisPool error : " + e2);
            }
        }
    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (jedisPool == null) {
            initialPool();
        }
    }

    /**
     * 同步获取Jedis实例
     * @return Jedis
     */
    private synchronized static Jedis getJedis() {
        if (jedisPool == null) {
            poolInit();
        }
        Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
            }
        } catch (Exception e) {
            logger.error("Get jedis error : " + e);
        }
        return jedis;
    }


    /**
     * 释放jedis资源
     */
    private static void returnResource(final Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedisPool.returnResource(jedis);
        }
    }

    //**************************** redis String start***************************/

    /**
     * 设置 String
     */
    public static void setString(String key, String value) {
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            getJedis().set(key, value);
        } catch (Exception e) {
            logger.error("Set key error : " + e);
        }
    }

    /**
     * 设置 过期时间
     */
    public static void setString(String key, int seconds, String value) {
        try {
            value = StringUtils.isEmpty(value) ? "" : value;
            getJedis().setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("Set keyex error : " + e);
        }
    }

    /**
     * 获取String值
     */
    public static String getString(String key) {
        if (getJedis() == null || !getJedis().exists(key)) {
            return null;
        }
        return getJedis().get(key);
    }
    //**************************** redis String end ***************************/

    //**************************** redis Hash start***************************/
    //***Redis hash 是一个string类型的field和value的映射表，hash特别适合用于存储对象。***/

    /**
     * 设置Hash的属性
     */
    public static boolean hset(String key, String field, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return false;
        }
        Jedis jedis = getJedis();
        Long statusCode = jedis.hset(key, field, value);
        returnResource(jedis);
        return statusCode > -1;
    }

    /**
     * 批量设置Hash的属性
     */
    public static boolean hmset(String key, String[] fields, String[] values) {
        if (StringUtils.isBlank(key) || null == fields || null == values) {
            return false;
        }
        Jedis jedis = getJedis();
        Map<String, String> hash = new HashMap<String, String>();
        for (int i = 0; i < fields.length; i++) {
            hash.put(fields[i], values[i]);
        }
        String statusCode = jedis.hmset(key, hash);
        returnResource(jedis);
        return SUCCESS_OK.equalsIgnoreCase(statusCode);
    }

    /**
     * 批量设置Hash的属性
     */
    public static boolean hmset(String key, Map<String, String> map) {
        if (StringUtils.isBlank(key) || null == map) {
            return false;
        }
        Jedis jedis = getJedis();
        String statusCode = jedis.hmset(key, map);
        returnResource(jedis);
        return SUCCESS_OK.equalsIgnoreCase(statusCode);
    }

    /**
     * 仅当field不存在时设置值，成功返回true
     */
    public static boolean hsetNX(String key, String field, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return false;
        }
        Jedis jedis = getJedis();
        /*If the field already exists, 0 is returned,*/
        /*otherwise if a new field is created 1 is returned.*/
        Long statusCode = jedis.hsetnx(key, field, value);
        returnResource(jedis);
        return SUCCESS_STATUS_LONG.equals(statusCode);
    }

    /**
     * 获取属性的值
     */
    public static String hget(String key, String field) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return null;
        }
        Jedis jedis = getJedis();
        String value = jedis.hget(key, field);
        returnResource(jedis);
        return value;
    }

    /**
     * 批量获取属性的值
     */
    public static List<String> hmget(String key, String... fields) {
        if (StringUtils.isBlank(key) || null == fields) {
            return null;
        }
        Jedis jedis = getJedis();
        List<String> values = jedis.hmget(key, fields);
        returnResource(jedis);
        return values;
    }

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     */
    public static Map<String, String> hgetAll(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Jedis jedis = getJedis();
        Map<String, String> map = jedis.hgetAll(key);
        returnResource(jedis);
        return map;
    }

    /**
     * 删除hash的属性
     */
    public static boolean hdel(String key, String fields) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(fields)) {
            return false;
        }
        Jedis jedis = getJedis();
        jedis.hdel(key, fields);
        returnResource(jedis);
        //System.out.println("statusCode="+statusCode);
        return true;
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在。
     */
    public static boolean hexists(String key, String field) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return false;
        }
        Jedis jedis = getJedis();
        boolean result = jedis.hexists(key, field);
        returnResource(jedis);
        return result;
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment 。
     *
     * @param increment 正负数、0、正整数
     */
    public static long hincrBy(String key, String field, long increment) {
        Jedis jedis = getJedis();
        long result = jedis.hincrBy(key, field, increment);
        returnResource(jedis);
        return result;
    }

    /**
     * 获取所有哈希表中的字段
     */
    public static Set<String> hkeys(String key) {
        Jedis jedis = getJedis();
        Set<String> result = jedis.hkeys(key);
        returnResource(jedis);
        return result;
    }

    /**
     * 获取哈希表中所有值
     */
    public static List<String> hvals(String key) {
        Jedis jedis = getJedis();
        List<String> result = jedis.hvals(key);
        returnResource(jedis);
        return result;
    }

    /**
     * 获取哈希表中字段的数量，当key不存在时，返回0
     */
    public static Long hlen(String key) {
        Jedis jedis = getJedis();
        Long result = jedis.hlen(key);
        returnResource(jedis);
        return result;
    }
    //**************************** redis Hash end***************************/


    //**************************** redis 列表List start***************************/
    /**
     * 将一个值插入到列表头部，value可以重复，返回列表的长度
     *
     * @param value String
     * @return 返回List的长度
     */
    public static Long lpush(String key, String value) {
        Jedis jedis = getJedis();
        Long length = jedis.lpush(key, value);
        returnResource(jedis);
        return length;
    }

    /**
     * 获取List列表
     *
     * @param start long，开始索引
     * @param end   long， 结束索引
     * @return List<String>
     */
    public static List<String> lrange(String key, long start, long end) {
        Jedis jedis = getJedis();
        List<String> list = jedis.lrange(key, start, end);
        returnResource(jedis);
        return list;
    }

    /**
     * 通过索引获取列表中的元素
     *
     * @param index，索引，0表示最新的一个元素
     * @return String
     */
    public static String lindex(String key, long index) {
        Jedis jedis = getJedis();
        String str = jedis.lindex(key, index);
        returnResource(jedis);
        return str;
    }

    /**
     * 获取列表长度，key为空时返回0
     */
    public static Long llen(String key) {
        Jedis jedis = getJedis();
        Long length = jedis.llen(key);
        returnResource(jedis);
        return length;
    }

    /**
     * 在列表的元素前或者后插入元素，返回List的长度
     *
     * @param where LIST_POSITION
     * @param pivot 以该元素作为参照物，是在它之前，还是之后（pivot：枢轴;中心点，中枢;[物]支点，支枢;[体]回转运动。）
     */
    public static Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        Jedis jedis = getJedis();
        Long length = jedis.linsert(key, where, pivot, value);
        returnResource(jedis);
        return length;
    }

    /**
     * 将一个值插入到已存在的列表头部，当成功时，返回List的长度；当不成功（即key不存在时，返回0）
     */
    public static Long lpushx(String key, String value) {
        Jedis jedis = getJedis();
        Long length = jedis.lpushx(key, value);
        returnResource(jedis);
        return length;
    }

    /**
     * 移除列表元素，返回移除的元素数量
     *
     * @param count，标识，表示动作或者查找方向 当count=0时，移除所有匹配的元素；
     *                            当count为负数时，移除方向是从尾到头；
     *                            当count为正数时，移除方向是从头到尾；
     * @param value               匹配的元素
     */
    public static Long lrem(String key, long count, String value) {
        Jedis jedis = getJedis();
        Long length = jedis.lrem(key, count, value);
        returnResource(jedis);
        return length;
    }

    /**
     * 通过索引设置列表元素的值，当超出索引时会抛错。成功设置返回true
     *
     * @param index 索引
     */
    public static boolean lset(String key, long index, String value) {
        Jedis jedis = getJedis();
        String statusCode = jedis.lset(key, index, value);
        returnResource(jedis);
        return SUCCESS_OK.equalsIgnoreCase(statusCode);
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     *
     * @param start 可以为负数（-1是列表的最后一个元素，-2是列表倒数第二的元素。）
     * @param end   如果start大于end，则返回一个空的列表，即列表被清空
     *              可以为负数（-1是列表的最后一个元素，-2是列表倒数第二的元素。）
     *              可以超出索引，不影响结果
     */
    public static boolean ltrim(String key, long start, long end) {
        Jedis jedis = getJedis();
        String statusCode = jedis.ltrim(key, start, end);
        returnResource(jedis);
        return SUCCESS_OK.equalsIgnoreCase(statusCode);
    }

    /**
     * 移出并获取列表的第一个元素，当列表不存在或者为空时，返回Null
     */
    public static String lpop(String key) {
        Jedis jedis = getJedis();
        String value = jedis.lpop(key);
        returnResource(jedis);
        return value;
    }

    /**
     * 移除并获取列表最后一个元素，当列表不存在或者为空时，返回Null
     */
    public static String rpop(String key) {
        Jedis jedis = getJedis();
        String value = jedis.rpop(key);
        returnResource(jedis);
        return value;
    }

    /**
     * 在列表中的尾部添加一个个值，返回列表的长度
     */
    public static Long rpush(String key, String value) {
        Jedis jedis = getJedis();
        Long length = jedis.rpush(key, value);
        returnResource(jedis);
        return length;
    }

    /**
     * 仅当列表存在时，才会向列表中的尾部添加一个值，返回列表的长度
     */
    public static Long rpushx(String key, String value) {
        Jedis jedis = getJedis();
        Long length = jedis.rpushx(key, value);
        returnResource(jedis);
        return length;
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey 源列表的key，当源key不存在时，结果返回Null
     * @param targetKey 目标列表的key，当目标key不存在时，会自动创建新的
     */
    public static String rpopLpush(String sourceKey, String targetKey) {
        Jedis jedis = getJedis();
        String value = jedis.rpoplpush(sourceKey, targetKey);
        returnResource(jedis);
        return value;
    }

    /**
     * 移出并获取列表的【第一个元素】， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     *
     * @param timeout 单位为秒
     *                当有多个key时，只要某个key值的列表有内容，即马上返回，不再阻塞。
     *                当所有key都没有内容或不存在时，则会阻塞，直到有值返回或者超时。
     *                当超期时间到达时，keys列表仍然没有内容，则返回Null
     * @return List<String>
     */
    public static List<String> blpop(int timeout, String... keys) {
        Jedis jedis = getJedis();
        List<String> values = jedis.blpop(timeout, keys);
        returnResource(jedis);
        return values;
    }

    /**
     * 移出并获取列表的【最后一个元素】， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     *
     * @param timeout 单位为秒
     *                当有多个key时，只要某个key值的列表有内容，即马上返回，不再阻塞。
     *                当所有key都没有内容或不存在时，则会阻塞，直到有值返回或者超时。
     *                当超期时间到达时，keys列表仍然没有内容，则返回Null
     */
    public static List<String> brpop(int timeout, String... keys) {
        Jedis jedis = getJedis();
        List<String> values = jedis.brpop(timeout, keys);
        returnResource(jedis);
        return values;
    }

    /**
     * 从列表中弹出列表最后一个值，将弹出的元素插入到另外一个列表中并返回它；
     * 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     *
     * @param sourceKey 源列表的key，当源key不存在时，则会进行阻塞
     * @param targetKey 目标列表的key，当目标key不存在时，会自动创建新的
     * @param timeout   单位为秒
     */
    public static String brpopLpush(String sourceKey, String targetKey, int timeout) {
        Jedis jedis = getJedis();
        String value = jedis.brpoplpush(sourceKey, targetKey, timeout);
        returnResource(jedis);
        return value;
    }
    //**************************** redis 列表List end***************************/


    //**************************** redis 集合Set start ***************************/
    //**Redis的Set是string类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。**/

    /**
     * 向集合添加一个成员，返回添加成功的数量
     */
    public static Long sadd(String key, String members) {
        Jedis jedis = getJedis();
        Long value = jedis.sadd(key, members);
        returnResource(jedis);
        return value;
    }

    /**
     * 获取集合的成员数
     */
    public static Long scard(String key) {
        Jedis jedis = getJedis();
        Long value = jedis.scard(key);
        returnResource(jedis);
        return value;
    }

    /**
     * 返回集合中的所有成员
     */
    public static Set<String> smembers(String key) {
        Jedis jedis = getJedis();
        Set<String> values = jedis.smembers(key);
        returnResource(jedis);
        return values;
    }

    /**
     * 判断 member 元素是否是集合 key 的成员，在集合中返回True
     */
    public static Boolean sIsMember(String key, String member) {
        Jedis jedis = getJedis();
        Boolean value = jedis.sismember(key, member);
        returnResource(jedis);
        return value;
    }

    /**
     * 返回给定所有集合的差集（获取第一个key中与其它key不相同的值，当只有一个key时，就返回这个key的所有值）
     */
    public static Set<String> sdiff(String... keys) {
        Jedis jedis = getJedis();
        Set<String> values = jedis.sdiff(keys);
        returnResource(jedis);
        return values;
    }

    /**
     * 返回给定所有集合的差集并存储在 targetKey中，类似sdiff，只是该方法把返回的差集保存到targetKey中
     * 当有差集时，返回true
     * 当没有差集时，返回false
     */
    public static boolean sdiffStore(String targetKey, String... keys) {
        Jedis jedis = getJedis();
        Long statusCode = jedis.sdiffstore(targetKey, keys);
        returnResource(jedis);
        return SUCCESS_STATUS_LONG == statusCode;
    }

    /**
     * 返回给定所有集合的交集（获取第一个key中与其它key相同的值，要求所有key都要有相同的值，如果没有相同，返回Null。当只有一个key时，就返回这个key的所有值）
     */
    public static Set<String> sinter(String... keys) {
        Jedis jedis = getJedis();
        Set<String> values = jedis.sinter(keys);
        returnResource(jedis);
        return values;
    }

    /**
     * 返回给定所有集合的交集并存储在 targetKey中，类似sinter
     */
    public static boolean sinterStore(String targetKey, String... keys) {
        Jedis jedis = getJedis();
        Long statusCode = jedis.sinterstore(targetKey, keys);
        returnResource(jedis);
        return SUCCESS_STATUS_LONG == statusCode;
    }

    /**
     * 将 member 元素从 sourceKey 集合移动到 targetKey 集合
     * 成功返回true
     * 当member不存在于sourceKey时，返回false
     * 当sourceKey不存在时，也返回false
     */
    public static boolean smove(String sourceKey, String targetKey, String member) {
        Jedis jedis = getJedis();
        Long value = jedis.smove(sourceKey, targetKey, member);
        returnResource(jedis);
        return value > 0;
    }

    /**
     * 移除并返回集合中的一个随机元素
     * 当set为空或者不存在时，返回Null
     */
    public static String spop(String key) {
        Jedis jedis = getJedis();
        String value = jedis.spop(key);
        returnResource(jedis);
        return value;
    }

    /**
     * 移除集合中一个成员
     */
    public static boolean srem(String key, String members) {
        Jedis jedis = getJedis();
        Long value = jedis.srem(key, members);
        returnResource(jedis);
        return value > 0;
    }

    /**
     * 返回所有给定集合的并集，相同的只会返回一个
     */
    public static Set<String> sunion(String... keys) {
        Jedis jedis = getJedis();
        Set<String> values = jedis.sunion(keys);
        returnResource(jedis);
        return values;
    }

    /**
     * 所有给定集合的并集存储在targetKey集合中
     * 注：合并时，只会把keys中的集合返回，不包括targetKey本身
     * 如果targetKey本身是有值的，合并后原来的值是没有的，因为把keys的集合重新赋值给targetKey
     * 要想保留targetKey本身的值，keys要包含原来的targetKey
     */
    public static boolean sunionStore(String targetKey, String... keys) {
        Jedis jedis = getJedis();
        //返回合并后的长度
        Long statusCode = jedis.sunionstore(targetKey, keys);
        System.out.println("statusCode=" + statusCode);
        returnResource(jedis);
        return statusCode > 0;
    }
    //************************** redis 集合Set end***************************/
}