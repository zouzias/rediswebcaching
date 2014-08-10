/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.zurich.rediscachingfilter.filters;

import com.ibm.zurich.rediscachingfilter.redis.RedisConnector;
import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpMethod;

public class RedisHTTPGetFilter implements Filter {
    private static final Logger logger = Logger.getLogger(RedisHTTPGetFilter.class);
    private FilterConfig filterConfig = null;

    // Get URL serves as hash value and cachedContent it's possible content.
    private String hashURL = null;
    private String cachedContent = null;

    public RedisHTTPGetFilter() {
    }

    private String requestParamsToString(HttpServletRequest req) {
        StringBuilder builder = new StringBuilder();

        Map<String, String[]> params = req.getParameterMap();
        for (String param : params.keySet()) {
            for (String value : params.get(param)) {
                if (builder.length() == 0) {
                    builder.append(param).append("=").append(value);
                } else {
                    builder.append("&").append(param).append("=").append(value);
                }
            }
        }
        return builder.toString();
    }

    private void doBeforeProcessing(ServletRequest request)
            throws IOException, ServletException {
        logger.debug("RedisHTTPGetFilter:DoBeforeProcessing");

        HttpServletRequest req = (HttpServletRequest) request;
        hashURL = null;
        cachedContent = null;
        
        // Cache only the GET method
        if (req.getMethod().compareTo(HttpMethod.GET.asString()) == 0) {
            hashURL = req.getRequestURL() + requestParamsToString(req);
            logger.info("Request URL : " + hashURL);
            cachedContent = RedisConnector.getKey(hashURL);

        } 
    }
    
    /**
     * After processing the request, cache the content response.toString() with key hashURL.
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException 
     */
    private void doAfterProcessing(ServletResponse response)
            throws IOException, ServletException {
        logger.debug("RedisHTTPGetFilter:DoAfterProcessing");

        if (hashURL != null) {
            logger.info("Hashing URL " + hashURL);
            RedisConnector.addKey(hashURL, response.toString(), true);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        logger.debug("RedisHTTPGetFilter:doFilter()");

        doBeforeProcessing(request);

        if (cachedContent != null) {
            logger.debug("Cached element hit!");
            response.getOutputStream().print(cachedContent);
        } else {
            logger.debug("Cache miss! on hashURL " + hashURL);
            chain.doFilter(request, response);
            response.getOutputStream().print(response.toString());
            doAfterProcessing(response);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            logger.debug("RedisHTTPGetFilter:Initializing filter");
        }
    }
}
