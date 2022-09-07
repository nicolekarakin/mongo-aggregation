package com.example.mongoaggregation.shops;

import com.example.mongoaggregation.shops.model.ShopList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import  com.example.mongoaggregation.shops.model.Address;
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

import java.util.*;

import static com.mongodb.client.model.Aggregates.project;
import static java.util.Arrays.asList;

@Component
public class CustomShopListRepoImpl implements CustomShopListRepo {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Collection<ShopList> findByIdAndAddressSubstr1(String id, String search) {

//        Query query2 = new Query(Criteria.where("id").is(id).and("addresses").elemMatch(Criteria.where("id").regex(search)));
//        query2.fields().include("addresses").exclude("id");
//        List<ShopList> bo = mongoTemplate.find(query2, ShopList.class);

//        https://stackoverflow.com/questions/28874941/elemmatch-equivalent-in-spring-data-mongodb
//        https://stackoverflow.com/questions/44471800/spring-data-match-and-filter-nested-array

        AggregationOperation project1 =
                aggregationOperationContext -> new Document("addresses", new Document("addresses.city", 0));

        ProjectionOperation project11=Aggregation.project().and(
                aggregationOperationContext -> new Document("addresses", Arrays.asList("$addresses")))
        .as("addresses");


        ProjectionOperation project12=Aggregation.project()
                .andArrayOf(new Document("addresses", Arrays.asList("$addresses")))
//                        aggregationOperationContext -> new Document("addresses", Arrays.asList("$addresses")))
                .as("addresses");

        AggregationOperation project111= aggregationOperationContext -> {
            Document projection = new Document();
            projection.put("addresses", Arrays.<Object>asList("$addresses"));
//            projection.put("_id","$id");
            return new Document("$project", projection);
        };

        ProjectionOperation projectToMatchModel = Aggregation.project()
                .andExpression("_id").as("state")
                .andExpression("avgCityPop").as("statePop");


        Aggregation aggregation1 = Aggregation.newAggregation(
                Aggregation.match(getCriteria(id))
//                ,Aggregation.unwind("$addresses")
//                ,Aggregation.match(getCriteria2(search)),
//                ,project1,
//                ,project11
//                ,project111
//                ,project12

//                ,Aggregation.sort(Sort.Direction.DESC, "street")
                );

        AggregationResults<Address> output1 =
                mongoTemplate.aggregate(aggregation1, "shopList", Address.class);

//        return new HashSet<Address>(output1.getMappedResults());

        AggregationResults<ShopList> output2 =
                mongoTemplate.aggregate(aggregation1, "shopList", ShopList.class);

        return new HashSet<ShopList>(output2.getMappedResults());
    }

    @Override
    public Set<Address> findByIdAndAddressSubstr2(String id, String search) {

        AggregationOperation addFields = aggregationOperationContext -> {
                DBObject dbObject =
                        new BasicDBObject("addresses",
                                new BasicDBObject("$filter",
                                        new BasicDBObject("input", "addresses").
                                                append("as", "p").
                                                append("cond", new BasicDBObject("$or", asList(
                                                        new BasicDBObject("regexMatch", asList("$$p.city", search)),
                                                        new BasicDBObject("regexMatch", asList("$$p.zip", search)),
                                                        new BasicDBObject("regexMatch", asList("$$p.street", search))
                                                )))
                                ));
                return new Document("$addFields", dbObject);
        };

        AggregationOperation project2 = aggregationOperationContext -> {
            BasicDBObject ob = new BasicDBObject("$map",
                    new BasicDBObject("input", new BasicDBObject(
                            "$filter", new BasicDBObject(
                            "input", "$addresses")
                            .append("as", "p")
                            .append("cond", new BasicDBObject("$or", asList(
                                    new BasicDBObject("regexMatch", asList("$$p.city", search)),
                                    new BasicDBObject("regexMatch", asList("$$p.zip", search)),
                                    new BasicDBObject("regexMatch", asList("$$p.street", search))
                            )))
                    ))
            );

            return new Document("$project", ob);
        };


        Aggregation aggregation2 = Aggregation.newAggregation(
                Aggregation.match(getCriteria(id)),
                Aggregation.unwind("$addresses"),
                Aggregation.match(getCriteria2(search)),
                project2, //addFields,
                Aggregation.sort(Sort.Direction.DESC, "street")
        );

        AggregationResults<Address> output2 =
                mongoTemplate.aggregate(aggregation2, "myaddresses", Address.class);

        return new HashSet<Address>(output2.getMappedResults());
//
//        return mongoTemplate.aggregate(aggregation2, mongoTemplate.getCollectionName(ShopList.class), Set.class)
//                .map(result -> Long.valueOf(result.get("count").toString()))
//                .next()
    }

    @Override
    public List<Address> findByIdAndAddressSubstr3(String id, String search) {
        Query query = new Query();
        query.addCriteria(getCriteria3(id, search));
        query.fields().include("id").position("addresses", 1);
        List<Address> addresses = mongoTemplate.find(query, Address.class);

        return addresses;
    }
    @Override
    public List<Address> findByIdAndAddressSubstr32(String id, String search) {
        Query query = new Query();
        query.addCriteria(getCriteria3(id, search));
        query.fields().include("addresses").position("addresses", 1);
        List<Address> addresses = mongoTemplate.find(query, Address.class);

        return addresses;
    }
    @Override
    public List<ShopList> findByIdAndAddressSubstr4(String id, String search) {

        Query query = new Query();
        query.addCriteria(getCriteria(id));
        query.fields().include("addresses")
                .elemMatch("addresses",Criteria.where("street").is(search))
        ;
//                .elemMatch("addresses", getCriteria2(id, search) );
        List<ShopList> res = mongoTemplate.find(query, ShopList.class);
        return res;
    }

    private Criteria getCriteria2(String search){
        Criteria criteria =
                new Criteria().orOperator(
                        Criteria.where("city").regex(search),
                        Criteria.where("street").regex(search),
                        Criteria.where("zip").regex(search)

        );
        return criteria;
    }
    private Criteria getCriteria(String id){
        Criteria criteria = Criteria.where("id").is(id);
        return criteria;
    }

    private Criteria getCriteria3(String id, String search){
        Criteria criteria = Criteria.where("id").is(id)
                .and("addresses").elemMatch(
                new Criteria().orOperator(
                        Criteria.where("city").regex(search),
                        Criteria.where("street").regex(search),
                        Criteria.where("zip").regex(search)
                ))
                ;
        return criteria;
    }
}

