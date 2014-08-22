/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zouzias.rediscachingfilter.filters;

import org.zouzias.rediscachingfilter.redis.RedisConnector;
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

public class RedisHTTPGetFilter implements Filter {

    private static final Logger logger = Logger.getLogger(RedisHTTPGetFilter.class);
    private FilterConfig filterConfig = null;

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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        logger.debug("RedisHTTPGetFilter:doFilter()");

        HttpServletRequest req = (HttpServletRequest) request;
        String hashURL = null;
        String cachedContent = null;

        // Cache only the GET method
        if (req.getMethod().compareTo("GET") == 0) {
            hashURL = req.getRequestURL() + requestParamsToString(req);
            logger.debug("Request URL : " + hashURL);
            cachedContent = RedisConnector.getKey(hashURL);
        }

        if (cachedContent != null) {
            logger.debug("Cached element hit!");
            response.getOutputStream().print(cachedContent);
        } else {
            logger.debug("Cache miss! on hashURL " + hashURL);
            chain.doFilter(request, response);
            response.getOutputStream().print(response.toString());

            if (hashURL != null && response.toString() != null) {
                logger.debug("Hashing URL\n -->HASH: " + hashURL + "\n-->VALUE: " + response.toString());
                RedisConnector.addKey(hashURL, response.toString(), true);
            }
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
