/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.zurich.rediscachingfilter.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnector {
    private static final Logger logger = Logger.getLogger(RedisConnector.class);
    private static JedisPool pool_ = null;
    private static final String redisHostname = "localhost";
    private static final int redisPort = 6379;

    public static JedisPool getRedisConnection() {
        if (pool_ != null) {
            return pool_;
        }

        pool_ = new JedisPool(new JedisPoolConfig(), redisHostname, redisPort);

        logger.info("[REDIS] Redis pool has been initialized at "
                + redisHostname + " and port # " + redisPort);

        return pool_;
    }
    
    private static Jedis getResource(){
        try{
            return pool_.getResource();
        } catch (Exception ex) {
            logger.error("Redis is required on " + redisHostname + " and port # " + redisPort + " and does not seem to be active. Ignoring Caching.", ex);
            return null;
        }
    }

    public static boolean addKey(String key, String value, boolean update) {
        logger.debug("Redis: added key " + key);

        boolean ret = false;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return false;
        }

        Jedis redis = RedisConnector.getResource();
        if (redis != null) {
            String key_val = redis.get(key);
            if (key_val == null || update) {
                redis.set(key, value);
                ret = true;
            }
            pool.returnResource(redis);
        }

        return ret;
    }

    public static boolean addKeyExpire(String key, String value, boolean update, int lifetime) {
        boolean ret = false;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return false;
        }

        Jedis redis = RedisConnector.getResource();
        if (redis != null) {
            String key_val = redis.get(key);
            if (key_val == null || update) {
                redis.setex(key, lifetime, value);
                ret = true;
            }
            pool.returnResource(redis);
        }

        return ret;
    }

    public static String getKey(String key) {
        String value = null;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return null;
        }

        Jedis redis = RedisConnector.getResource();
        if (redis != null) {
            value = redis.get(key);
            pool.returnResource(redis);
        }

        return value;
    }
    
    public static boolean delKey(String key) {
        long retValue = -1;
        JedisPool pool = getRedisConnection();
        if (pool == null) {
            return false;
        }

        Jedis redis = RedisConnector.getResource();
        if (redis != null) {
            retValue = redis.del(key);
            pool.returnResource(redis);
        }

        return (retValue == 0);
    }
}
