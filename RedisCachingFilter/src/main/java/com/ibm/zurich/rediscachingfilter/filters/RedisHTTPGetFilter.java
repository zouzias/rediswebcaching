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

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    private String hashURL = null;
    private String cachedContent = null;

    public RedisHTTPGetFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("RedisHTTPGetFilter:DoBeforeProcessing");
        }

        HttpServletRequest req = (HttpServletRequest) request;
        StringBuilder builder = new StringBuilder();

        // If method is GET
        if (req.getMethod().compareTo(HttpMethod.GET.asString()) == 0) {

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

            hashURL = req.getRequestURL() + builder.toString();
            logger.info("Request URL : " + hashURL);
            cachedContent = RedisConnector.getKey(hashURL);

        } else {
            hashURL = null;
            cachedContent = null;
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("RedisHTTPGetFilter:DoAfterProcessing");
        }

        if (hashURL != null) {
            logger.info("Hashing URL " + hashURL);
            RedisConnector.addKey(hashURL, response.toString(), true);
        }
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        if (debug) {
            log("RedisHTTPGetFilter:doFilter()");
        }

        doBeforeProcessing(request, response);

               
        if (cachedContent != null) {
            logger.info("Found cached element");
            response.getOutputStream().print(cachedContent);
        } else {
            logger.info("Cache miss! on hashURL " + hashURL);

            chain.doFilter(request, response);
            response.getOutputStream().print(response.toString());

            //logger.info("\n\n\nResponse before doAfter\n" + response.toString());
            doAfterProcessing(request, response);
            //logger.info("\n\n\nResponse after doAfter\n" + response.toString());
        }
        
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("RedisHTTPGetFilter:Initializing filter");
            }
        }
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
