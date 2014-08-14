/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.zurich.rediscachingfilter.redis;

import org.apache.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author azo
 */
public class RedisConnectorIT {
    private static final Logger logger = Logger.getLogger(RedisConnectorIT.class);

    public RedisConnectorIT() {
    }

    /**
     * Test of getRedisConnection method, of class RedisConnector.
     */
    @Test
    public void testGetRedisConnection() {
        JedisPool expResult = null;
        JedisPool result = RedisConnector.getRedisConnection();
        
        assertNotSame(expResult, result);
    }

    /**
     * Test of addKey method, of class RedisConnector.
     */
    @Test
    public void testAddKey() {
        String key = "__test__key";
        String value = "_OK_";
        boolean update = true;
        boolean expResult = false;
        boolean result = RedisConnector.addKey(key, value, update);
        assertEquals("Key did not inserted into Redis", expResult, result);
        RedisConnector.
    }

    /**
     * Test of addKeyExpire method, of class RedisConnector.
     */
    @Test
    public void testAddKeyExpire() {
       String key = "__test__key";
        String value = "_OK_";
        boolean update = true;
        boolean expResult = true;
        boolean result = RedisConnector.addKey(key, value, update);
        assertEquals("Key did not inserted into Redis", expResult, result);
    }

    /**
     * Test of getKey method, of class RedisConnector.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        String key = "";
        String expResult = "";
        String result = RedisConnector.getKey(key);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
