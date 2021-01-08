package com.hujinwen.client.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;

class MongoClientBuilderTest {

    @Test
    void test() {
        final MongoClientBuilder clientBuilder = MongoClientBuilder.defaultBuilder();
        final MongoClient mongoClient = clientBuilder.host("42.192.200.105").port(10001)
                .user("root").password("imh9AdhJ5n3BGcZ0").build();

        final MongoDatabase database = mongoClient.getDatabase("sns_ghost");
        final MongoCollection<Document> collection = database.getCollection("ghost_douyin_user_trend");
        final long count = collection.countDocuments();
        System.out.println();
    }

}