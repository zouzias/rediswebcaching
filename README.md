# Redis Servlet Caching <a href="https://travis-ci.org/zouzias/rediswebcaching.svg?branch=master"><img src="https://travis-ci.org/zouzias/rediswebcaching.svg?branch=master"/></a>

This project provides an implementation for the following problem:

<h5> Cache the content of custom HTTP GET requests that are handled by Java Servlets.</h5>

The caching mechanism is based on the Redis key-value store (http://redis.io/). The caching mechanism is implemented via Servlet Filters so that adding or removing the caching functionality from any servlet can be done easily.

The following figure gives a high-level description of the project's functionality.

<p align="center">
  <img src="https://raw.github.com/zouzias/rediswebcaching/master/assets/overview.png" alt="Overview of project"/>
</p>

### An illustrative example

The following example demonstrates a case of a cached servlet and an un-cached servlet respectively.

To use this example, you'll need Git, Java (>= 7), Redis server (http://redis.io/) and Maven. Once those are installed and setup, you can download the code using Git:

    git clone https://github.com/zouzias/rediswebcaching.git
    cd rediswebcaching/RedisCachingFilter/

To start the jetty server with caching mechanism enabled on all /cached/* HTTP Get requests, type:

    mvn compile jetty:run

The jetty server will run on port 9999.

First, start the redis server (assume redis runs on localhost and port 6379), open the terminal/console and type:

    redis-server
    
Then, open another terminal/console and type:

    redis-cli
    127.0.0.1:6379> MONITOR
    
to start a monitoring session for Redis.

Third, open your browser and type:

    http://localhost:9999/cached/CachedServlet
    
Check the traffic on redis.

See the `web.xml` file to understand the structure of the servlet container.

Note: To install Redis, see http://redis.io/ for details depending on your system. We assume that redis runs on port 6379. See RedisConnector class for tweaks.

### Usage with custom servlets

Clone the project. Place your servlets into org.zouzias.rediscachingfilter.servlets. Adjust the web.xml file to point to the servlets whose content will be cached by redis, i.e. for a servlet named WannaBeCachedServlet.java add the following lines in web.xml

    <servlet>
        <servlet-name>WannaBeCachedServlet</servlet-name>
        <servlet-class>org.zouzias.rediscachingfilter.servlets.WannaBeCachedServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>WannaBeCachedServlet</servlet-name>
        <url-pattern>/cached/WannaBeCachedServlet</url-pattern>
    </servlet-mapping>

See the file 'web.xml' that contains the servlets CachedServlet and UncachedServlet as an example of a cached and not-cached servlets, respectively.

### How to include these files into your servlet container.

TODO list 

* Copy the classes contained in the `org.zouzias.rediscachingfilter.core`, `org.zouzias.rediscachingfilter.filters` and `org.zouzias.rediscachingfilter.redis` packages. 
* Add the filter definitions into your `web.xml`

### Run tests

    cd RedisCachingFilter
    mvn compile war:war test
    
### Limitations

* The caching mechanism only caches the content of the HTTP GET request (not the response type, etc)

### License

Apache Software License, version 2.0



