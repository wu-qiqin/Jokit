package com.hujinwen.client.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * Created by hu-jinwen on 2021/1/8
 * <p>
 * mongo client builder
 * 简化 com.mongodb.MongoClient 对象的创建
 * <p>
 * FIXME 这只是一个非常简单的Mongo单例连接例子
 * <p>
 * TODO 完善代码、添加集群
 */
public class MongoClientBuilder {
    private static final Logger logger = LoggerFactory.getLogger(MongoClientBuilder.class);

    private static final MessageFormat URL_FORMAT = new MessageFormat("mongodb://{0}:{1}");
    private static final MessageFormat URL_FORMAT_WITH_AUTH = new MessageFormat("mongodb://{0}:{1}@{2}:{3}");

    /**
     * host
     */
    private String host = "localhost";

    /**
     * port
     */
    private int port = 27107;

    /**
     * user
     */
    private String user = null;

    /**
     * password
     */
    private String password = null;

    /**
     * 查询timeout
     */
    private long queryTimeout = 3000;

    private MongoClientBuilder() {
    }

    public static MongoClientBuilder defaultBuilder() {
        return new MongoClientBuilder();
    }

    /**
     * 设置host
     */
    public MongoClientBuilder host(String host) {
        this.host = host;
        return this;
    }

    /**
     * 设置port
     */
    public MongoClientBuilder port(int port) {
        this.port = port;
        return this;
    }

    /**
     * 设置user
     */
    public MongoClientBuilder user(String user) {
        this.user = user;
        return this;
    }

    /**
     * 设置password
     */
    public MongoClientBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * 设置查询 timeout
     */
    public MongoClientBuilder queryTimeout(long queryTimeout) {
        this.queryTimeout = queryTimeout;
        return this;
    }


    public MongoClient build() {
        String url;
        if (user != null && password != null) {
            url = URL_FORMAT_WITH_AUTH.format(new Object[]{user, password, host, String.valueOf(port)});
        } else {
            url = URL_FORMAT.format(new Object[]{host, String.valueOf(port)});
        }
        return new MongoClient(new MongoClientURI(url));
    }

}
