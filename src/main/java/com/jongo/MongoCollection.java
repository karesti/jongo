/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jongo;

import com.jongo.jackson.EntityProcessor;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MongoCollection {

    public static final String MONGO_ID = "_id";
    private final DBCollection collection;
    private final EntityProcessor entityProcessor;

    public MongoCollection(DBCollection dbCollection, EntityProcessor entityProcessor) {
        this.collection = dbCollection;
        this.entityProcessor = entityProcessor;
    }

    public <T> T findOne(String query, Class<T> clazz) {
        return findOne(new Query(query), clazz);
    }

    public <T> T findOne(Query query, Class<T> clazz) {
        return findOne(query, entityProcessor.createEntityMapper(clazz));
    }

    public <T> T findOne(String query, DBObjectMapper<T> resultMapper) {
        return findOne(new Query(query), resultMapper);
    }

    public <T> T findOne(Query query, DBObjectMapper<T> dbObjectMapper) {
        DBObject result = collection.findOne(query.toDBObject());
        if (result == null)
            return null;
        else
            return dbObjectMapper.map(result);
    }

    public <T> Iterator<T> find(String query, Class<T> clazz) {
        return find(new Query(query), clazz);
    }

    public <T> Iterator<T> find(Query query, Class<T> clazz) {
        return find(query, entityProcessor.createEntityMapper(clazz));
    }

    public <T> Iterator<T> find(String query, DBObjectMapper<T> dbObjectMapper) {
        return find(new Query(query), dbObjectMapper);
    }

    public <T> Iterator<T> find(Query query, DBObjectMapper<T> dbObjectMapper) {
        DBCursor cursor = collection.find(query.toDBObject());
        return new MongoIterator(cursor, dbObjectMapper);
    }

    public long count(String query) {
        Query staticQuery = new Query(query);
        return collection.count(staticQuery.toDBObject());
    }

    @SuppressWarnings("unchecked")
    public <T> Iterator<T> distinct(String key, String query, Class<T> clazz) {
        Query staticQuery = new Query(query);
        DBObject ref = staticQuery.toDBObject();
        List<?> distinct = collection.distinct(key, ref);
        if (BSONPrimitives.contains(clazz))
            return (Iterator<T>) distinct.iterator();
        else
            return new MongoIterator<T>((Iterator<DBObject>) distinct.iterator(), entityProcessor.createEntityMapper(clazz));
    }

    public <D> String save(D document) throws IOException {
        DBObject dbObject = entityProcessor.getEntityAsDBObject(document);
        collection.save(dbObject);
        return dbObject.get(MONGO_ID).toString();
    }

    @Deprecated
    // TODO use save or generic method
    public void index(String query) {
        DBObject dbObject = ((DBObject) JSON.parse(query));
        collection.insert(dbObject); // TODO don't save id
    }

    public void drop() {
        collection.drop();
    }

    public String getName() {
        return collection.getName();
    }

    public DBCollection getDBCollection() {
        return collection;
    }
}
