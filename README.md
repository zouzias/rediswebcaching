# Redis Servlet Caching <a href="https://travis-ci.org/zouzias/rediswebcaching.svg?branch=master"><img src="https://travis-ci.org/zouzias/rediswebcaching.svg?branch=master"/></a>

This project provides an implementation for the following problem:

<h5> Cache the content of custom HTTP GET requests that are handled by Java Servlets.</h5>

This project uses the Redis key-value store (http://redis.io/) as its caching service and servlet Filters for agnostically implementing caching.

The caching mechanism is implemented via Servlet Filters and therefore it is easy to add or remove the caching functionality from the servlet container.

### Introduction

To install, you'll need Git, Java (>= 7), Redis server (http://redis.io/) and Maven. Once those are installed and setup, you can download the code using Git:

    git clone https://github.com/zouzias/rediswebcaching.git
    cd rediswebcaching/RedisCachingFilter/

To start the jetty server with caching mechanism enabled on all /cached/* HTTP Get requests, type:

    mvn compile jetty:run

The jetty server will run on port 9999.

First, start the redis server, open the terminal/console and type:

    redis-server
    
Then, open another terminal/console and type:

    redis-cli
    127.0.0.1:6379> MONITOR
    
to start a monitoring session for Redis.

Third, open your browser and type:

    http://localhost:9999/cached/CachedServlet
    
Check the traffice on redis.

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

TODO (copy the classes contained in the core, filters and redis packages. Add the filter definitions into your web.xml)

### Run tests

    cd RedisCachingFilter
    mvn compile war:war test

### License

Apache Software License, version 2.0



