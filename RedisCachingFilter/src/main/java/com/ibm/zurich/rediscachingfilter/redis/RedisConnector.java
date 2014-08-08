/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.zurich.rediscachingfilter.redis;

import java.util.Set;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnector {
    private static final Logger logger = Logger.getLogger(RedisConnector.class);
    private static JedisPool pool_ = null;
    private static final String redisHostname = "localhost";
    private static int redisPort;

    public static JedisPool getRedisConnection() {
        if (pool_ != null) {
            return pool_;
        }

        redisPort = 6379;

        try {
            pool_ = new JedisPool(new JedisPoolConfig(), redisHostname, redisPort);

            logger.info("[REDIS] Redis pool has been initialized at "
                    + redisHostname + " and port # " + redisPort);

            Jedis jedis = pool_.getResource();
            pool_.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis is required on " + redisHostname + " and port # " + redisPort + " and does not seem to be active. Quitting.");
            return null;
        }

        return pool_;
    }

    public static boolean addKey(String key, String value, boolean update) {
        logger.debug("Redi added key " + key);

        boolean ret = false;
        String key_val = null;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return false;
        }

        Jedis redis = RedisConnector.getRedisConnection().getResource();
        if (redis != null) {
            key_val = redis.get(key);
            if (key_val == null || update) {
                redis.set(key, value);
                ret = true;
            }
        }

        RedisConnector.getRedisConnection().returnResource(redis);
        return ret;
    }

    public static boolean addKeyExpire(String key, String value, boolean update, int lifetime) {
        boolean ret = false;
        String key_val = null;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return false;
        }

        Jedis redis = RedisConnector.getRedisConnection().getResource();
        if (redis != null) {
            key_val = redis.get(key);
            if (key_val == null || update) {
                redis.setex(key, lifetime, value);
                ret = true;
            }
        }

        RedisConnector.getRedisConnection().returnResource(redis);
        return ret;
    }

    public static String getKey(String key) {
        String value = null;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return null;
        }

        Jedis redis = RedisConnector.getRedisConnection().getResource();
        if (redis != null) {
            value = redis.get(key);
        }

        RedisConnector.getRedisConnection().returnResource(redis);
        return value;
    }

    public static Set<String> getKeysByPrefix(String prefix) {
        Set<String> keys = null;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return null;
        }

        Jedis redis = RedisConnector.getRedisConnection().getResource();
        if (redis != null) {
            keys = redis.keys(prefix + "*");
        }
        RedisConnector.getRedisConnection().returnResource(redis);
        return keys;
    }

    public static Long delKey(String key) {
        Long value = null;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return null;
        }

        Jedis redis = RedisConnector.getRedisConnection().getResource();
        if (redis != null) {
            value = redis.del(key);
        }

        RedisConnector.getRedisConnection().returnResource(redis);
        return value;
    }

    public static int delByPrefix(String prefix) {
        JedisPool pool = getRedisConnection();
        int key_cnt = -1;
        if (pool == null) {
            return -1;
        }

        Jedis redis = RedisConnector.getRedisConnection().getResource();
        if (redis != null) {
            Set<String> keys = redis.keys(prefix + "*");
            for (String key : keys) {
                redis.del(key);
            }
            key_cnt = keys.size();
        }

        RedisConnector.getRedisConnection().returnResource(redis);
        return key_cnt;
    }
}
