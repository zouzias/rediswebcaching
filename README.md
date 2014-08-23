# Redis Servlet Caching <a href="https://travis-ci.org/zouzias/rediswebcaching.svg?branch=master"><img src="https://travis-ci.org/zouzias/rediswebcaching.svg?branch=master"/></a>

This project provides an implementation for caching HTTP GET requests that are handled by Java Servlets. 

More precisely, this project provides an implementation of a web caching mechanism of Java Servlets (HTTP GET requests) using Redis key-value store (http://redis.io/) and Servlet Filters.

Assuming that you have your custom servlet, to use this project, just follow the instructions in the Usage Section below.

### Usage

Clone the project. Place your servlets into org.zouzias.rediscachingfilter.servlets. Adjust the web.xml file to point to the servlets whose content will be cached by redis, i.e. for a servlet named WannaBeCachedServlet.java add the following line in web.xml

    <servlet>
        <servlet-name>WannaBeCachedServlet</servlet-name>
        <servlet-class>org.zouzias.rediscachingfilter.servlets.WannaBeCachedServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>WannaBeCachedServlet</servlet-name>
        <url-pattern>/cached/WannaBeCachedServlet</url-pattern>
    </servlet-mapping>

See the file 'web.xml' that contains the servlets CachedServlet and UncachedServlet as an example of a cached and not-cached servlets, respectively.

### Introduction

To install, you'll need Git, Java (>= 7), Redis server (http://redis.io/) and Maven. Once those are installed and setup, you can download the code using Git:

    git clone https://github.com/zouzias/rediswebcaching.git
    cd rediswebcaching/RedisCachingFilter/

To start the jetty server with caching mechanism enabled on all /cached/* HTTP Get requests, type:

    mvn compile jetty:run

The jetty server will run on port 99999.

Open your browser and type:

    http://localhost:9999/cached/CachedServlet
    
Check the traffic on redis. Open the terminal/console and type:

    redis-cli
    127.0.0.1:6379> MONITOR

Note: To install Redis, see http://redis.io/ for details depending on your system. We assume that redis runs on port 6379. See RedisConnector class for tweaks.

### How to include these files into your servlet container.

TODO

### Run tests

    cd RedisCachingFilter
    mvn compile war:war test

### License

Apache Software License, version 2.0



