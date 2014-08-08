package com.ibm.zurich.rediscacherexperiment;


import com.ibm.zurich.rediscacherexperiment.searchers.MultiFacetSearcher;
import com.ibm.zurich.solrcacheserver.servlets.models.MultiFacetResponse;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author azo
 */
public class ExperimentRedisCache {

    private static JedisPool pool = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int n = 1000;
        pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);


        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            String query = RandomStringUtils.randomAlphabetic(10);
            System.out.println("Asking query " + query);
            askQuery(query);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("\n\nTotal execution time: " + (endTime - startTime) / 1000  + " secs");

        pool.destroy();

    }

    private static void askQuery(String query) {
        String value;
        Jedis jedis = null;

        try {
            jedis = pool.getResource();

            String cacheKey = getCacheKey(query);
            value = jedis.get(cacheKey);
            if (value == null) {
                System.out.println("\nKey ***" + query + "*** returned NULL\n");
                List<String> facets = new LinkedList<>();
                facets.add("CP.SELECTIONS.MARKET");
                facets.add("CP.SELECTIONS.INDUSTRY");
                facets.add("CP.SELECTIONS.SUBINDUSTRY");
                MultiFacetResponse response = MultiFacetSearcher.querySolrCore("joiner", query, facets);
                
                
                jedis.set(cacheKey, response.getFacetJSONResults().toString());
            }
        } finally {
            pool.returnResource(jedis);
        }

    }

    private static String getCacheKey(String query) {
        return query;
    }

}
