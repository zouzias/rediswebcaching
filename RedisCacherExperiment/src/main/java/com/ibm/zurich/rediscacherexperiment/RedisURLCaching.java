/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.zurich.rediscacherexperiment;

import com.ibm.zurich.rediscacherexperiment.searchers.MultiFacetSearcher;
import com.ibm.zurich.solrcacheserver.servlets.models.MultiFacetResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author azo
 */
public class RedisURLCaching {

    private static JedisPool pool = null;

    public RedisURLCaching(String hostname, int port) {
        //localhost, 6379
        pool = new JedisPool(new JedisPoolConfig(), hostname, port);

    }
    
    
    public String fetchCachedURL(URL url){
        String value;
        Jedis jedis = null;

        try {
            jedis = pool.getResource();

            String cacheKey = getCacheKey(url);
            value = jedis.get(cacheKey);
            if (value == null) {
                System.out.println("\nKey ***" + url.toString() + "*** returned NULL\n");
                
                value = fetchURL(url);
                jedis.set(cacheKey, fetch);
            }
        } finally {
            pool.returnResource(jedis);
        }
        
        return value;
        
    }
    
    public String fetchURL(URL url) throws IOException{

        
        String responseString = "";

        try{
            InputStream is = url.openStream();
            responseString = IOUtils.toString(is, "UTF-8");
           

        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(RedisURLCaching.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        return responseString;

    }


        
        
    }
    
    public String getCachedKey(URL url){
        return url.toString();
    }

}
