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
import javax.ws.rs.HttpMethod;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class RedisHTTPFilter implements Filter {

    private static final Logger logger = Logger.getLogger(RedisHTTPFilter.class);
    private FilterConfig filterConfig = null;

    public RedisHTTPFilter() {
    }

    /**
     *
     * @param req
     * @return
     */
    private static String requestParamsToString(HttpServletRequest req) {
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

    /**
     * Given HTTP Get Request, compute its md5hex hash string
     * 
     * @param req
     * @return 
     */
    private String stringifyHttpGetRequest(HttpServletRequest req) {
        return req.getRequestURL() + requestParamsToString(req);
    }

    /**
     *  Given HTTP Post Request, compute its md5 hex hash string
     * 
     * @param req
     * @return 
     */
    private String stringifyHttpPostRequest(HttpServletRequest req) {
        try {
            String postTextContent = IOUtils.toString(req.getInputStream());
            logger.info("HTTP Post content is " + postTextContent);
            return req.getRequestURL() + requestParamsToString(req) + ":" + DigestUtils.md5Hex(postTextContent);
        } catch (IOException ex) {
            logger.error("Could not read POST input stream or stream is not UTF-8", ex);
        }

        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        logger.debug("RedisHTTPGetFilter:doFilter()");

        HttpServletRequest req = (HttpServletRequest) request;
        String hashURL = null;
        String cachedContent = null;

        // Cache HTTP GET method
        if (req.getMethod().compareTo(HttpMethod.GET) == 0) {
            hashURL = stringifyHttpGetRequest(req);
            logger.debug("HTTP GET on [" + hashURL + "]");
            cachedContent = RedisConnector.getKey(hashURL);
        } // Cache HTTP Post method 
        else if (req.getMethod().compareTo(HttpMethod.POST) == 0) {
            hashURL = stringifyHttpPostRequest(req);
            logger.debug("HTTP POST on [" + req.getRequestURL() + "]");
            String hashedPOSTContent = stringifyHttpPostRequest(req);

            // Check if the content is in Redis
            cachedContent = RedisConnector.getKey(hashedPOSTContent);
        }

        if (cachedContent != null) {
            logger.debug("Cached element hit!");
            response.getOutputStream().write(cachedContent.getBytes("UTF-8"));
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
