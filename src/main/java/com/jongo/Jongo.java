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
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class Jongo {

    private final DB database;

    public Jongo(DB database) {
        this.database = database;
    }

    public MongoCollection getCollection(String name) {
        DBCollection dbCollection = database.getCollection(name);
        return new MongoCollection(dbCollection, new EntityProcessor());
    }

    public DB getDatabase() {
        return database;
    }
}
