package com.example.mongoaggregation.shops;

import com.example.mongoaggregation.shops.model.Address;
import com.example.mongoaggregation.shops.model.ShopList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static java.util.Arrays.asList;

@Component
public class CustomShopListRepoImpl2 {
    MongoClient mongoClient;

    MongoDatabase database;
    MongoCollection<Document> collection;
    @PostConstruct
    public  void setUpDB() throws IOException {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("test");
        collection = database.getCollection("shopList");
        //    Bson filter = FindIterableilters.eq("_id", new ObjectId("62d01d17cdd1b7c8a5f945b9"));
        //    FindIterable<Document> documents = collection.find(filter);
    }




    public Collection<Address> findByIdAndAddressSubstr1(String id, String search) {
//will store as col1
//        collection.aggregate(Arrays.asList(
//                sort(Sorts.descending("id")),
//                limit(7),
//                out("col1"))).toCollection();
/*
let cursor = db
    .collection("somecollection")
    .aggregate(
      { $match: { outerparam: outermatch } },
      { $unwind: "$innerarray" },
      { $match: { "innerarray.property": propertymatch } },
      { $project: { "innerarray.$": 1 } });
*/
        Bson bordering = project(Projections.fields(Projections.excludeId(),
                Projections.include("addresses.street","addresses.zip")));


        AggregateIterable<Document> rty = collection.aggregate(Arrays.asList(bordering,
                match(Filters.eq("street", search))));
                ;

        var docs = new ArrayList<Document>();
        rty.into(docs);
        for (Document doc : docs) {
            System.out.println(doc);
        }
//        rty.forEach(a->a.toBsonDocument());
        return null;
    }

}
//https://stackoverflow.com/questions/19696282/make-elemmatch-projection-return-all-objects-that-match-criteria
//https://stackoverflow.com/questions/3985214/retrieve-only-the-queried-element-in-an-object-array-in-mongodb-collection?rq=1
//https://stackoverflow.com/questions/56780436/project-new-array-field-with-spring-data
//https://stackoverflow.com/questions/3985214/retrieve-only-the-queried-element-in-an-object-array-in-mongodb-collection
//https://stackoverflow.com/questions/44471800/spring-data-match-and-filter-nested-array
//https://stackoverflow.com/questions/3985214/retrieve-only-the-queried-element-in-an-object-array-in-mongodb-collection/3985982#3985982

//https://www.mongodb.com/docs/manual/reference/operator/query/



//https://www.baeldung.com/spring-data-mongodb-projections-aggregations
//https://www.baeldung.com/queries-in-spring-data-mongodb
//https://www.baeldung.com/spring-data-mongodb-projections-aggregations



