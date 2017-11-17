package com.cmx.springbootshiro.config;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.cmx.springbootshiro.domain.mongo")
public class MongodbConfig extends AbstractMongoConfiguration{


    @Value("${spring.data.mongodb.host}")
    private String host = "127.0.0.1";

    @Value("${spring.data.mongodb.port}")
    private String port = "27017";

    @Value("${spring.data.mongodb.database}")
    private String database = "pubtrans";


    @Override
    protected String getDatabaseName() {
        return this.database;
    }


    /**
     * 没有使用用户名和密码 可以这样配置
     * @return
     * @throws Exception
     */
    @Override
    public Mongo mongo() throws Exception {
        ServerAddress addr = new ServerAddress(this.host, Integer.valueOf(this.port));
        MongoClientOptions ops = MongoClientOptions.builder().connectionsPerHost(50).threadsAllowedToBlockForConnectionMultiplier(5).build();
        MongoClient mongoClient = new MongoClient(addr,ops);
        mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);

        return mongoClient;
    }

    /**
     * 扫描mongo实体类包路径
     * @return
     */
    @Override
    public String getMappingBasePackage() {
        return "com.cmx.springbootshiro.domain.mongo";
    }
}
