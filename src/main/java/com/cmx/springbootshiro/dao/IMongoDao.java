package com.cmx.springbootshiro.dao;

import com.mongodb.DBObject;

import java.util.List;


/**
 * mongoDB template
 */
public interface IMongoDao {


    public DBObject findOne(String collection, DBObject query, DBObject fields);

    public List<DBObject> find(String collection, DBObject query, DBObject fields, DBObject orderBy, int pageNum, int pageSize);

    public List<DBObject> find(String collection, DBObject query, DBObject fields, DBObject orderBy, int limit);

    public void delete(String collection, DBObject dbObject);

    public void save(String collection, DBObject dbObject);

    public void update(String collection, DBObject query, DBObject update, boolean upsert, boolean multi);

    public Long count(String collection, DBObject query);

    public List<?> distinct(String collection, String key, DBObject query);


}
