package com.example.mongoaggregation.shops;

import com.example.mongoaggregation.shops.model.Address;
import com.example.mongoaggregation.shops.model.ShopList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ShopListRepoTest {
    private Set<Address> addresses = Set.of(
            new Address("Street 2", "12345", "City", null),
            new Address("Street 2", "12345", "Rity", null),
            new Address("Street 37", "12345", "Rity", null),
            new Address("Some Str 33", "12375", "City", null));
    @Autowired
    private ShopListRepo shopListRepo;

//    @DirtiesContext
//    @Test
    void findByIdIgnoreCaseContaining() {
        String id1 = "de-DE_aldi";
        shopListRepo.save(new ShopList(id1, addresses));
        String id2 = "de-DE_edeka";
        shopListRepo.save(new ShopList(id2, addresses));
        Set<ShopList> found = shopListRepo.findByIdIgnoreCaseContaining("-de");
        Assertions.assertEquals(2,found.size());

        Set<ShopList> found2 = shopListRepo.findByIdIgnoreCaseContaining("-DE");
        Assertions.assertEquals(2,found2.size());

        Set<ShopList> found3 = shopListRepo.findByIdIgnoreCaseContaining("edEK");
        Assertions.assertEquals(1,found3.size());
        System.out.println("Annotation Interface Query========================================");
    }


//    @DirtiesContext
//    @Test
    void findByIdAndAddressFilter() {
        String id1 = "de-DE_aldi";
        shopListRepo.save(new ShopList(id1, addresses));
        String id2 = "de-DE_edeka";
        shopListRepo.save(new ShopList(id2, addresses));

        Collection<ShopList> found = shopListRepo.findByIdAndAddressFilter(id1,"rity");
        Assertions.assertEquals(1,found.size());
        Assertions.assertEquals(4,found.stream().toList().get(0).getAddresses().size());
        System.out.println("Annotation Interface Query========================================");
    }

    @DirtiesContext
    @Test
    void findByIdAndAddressSubstr1() {
        String id1 = "de-DE_aldi";
        shopListRepo.save(new ShopList(id1, addresses));
        String id2 = "de-DE_edeka";
        shopListRepo.save(new ShopList(id2, addresses));

        Collection<ShopList> found = shopListRepo.findByIdAndAddressSubstr1(id1,"eet");
        System.out.println("found ========"+found.size());
        System.out.println("1========================================");
        Assertions.assertEquals(3,found.size());
    }

    @DirtiesContext
    @Test
    void findByIdAndAddressSubstr2() {
        String id1 = "de-DE_aldi";
        shopListRepo.save(new ShopList(id1, addresses));
        String id2 = "de-DE_edeka";
        shopListRepo.save(new ShopList(id2, addresses));

        Collection<Address> found = shopListRepo.findByIdAndAddressSubstr2(id1,"eet");
        System.out.println("2========================================");
        Assertions.assertEquals(3,found.size());
    }

    @DirtiesContext
    @Test
    void findByIdAndAddressSubstr3() {
        String id1 = "de-DE_aldi";
        shopListRepo.save(new ShopList(id1, addresses));
        String id2 = "de-DE_edeka";
        shopListRepo.save(new ShopList(id2, addresses));

        Collection<Address> found = shopListRepo.findByIdAndAddressSubstr3(id1,"eet");
        System.out.println("3========================================");
        Assertions.assertEquals(3,found.size());
    }

    @DirtiesContext
    @Test
    void findByIdAndAddressSubstr32() {
        String id1 = "de-DE_aldi";
        shopListRepo.save(new ShopList(id1, addresses));
        String id2 = "de-DE_edeka";
        shopListRepo.save(new ShopList(id2, addresses));

        Collection<Address> found = shopListRepo.findByIdAndAddressSubstr32(id1,"eet");
        System.out.println("32========================================");
        Assertions.assertEquals(3,found.size());
    }

    @DirtiesContext
    @Test
    void findByIdAndAddressSubstr4() {
        String id1 = "de-DE_aldi";
        shopListRepo.save(new ShopList(id1, addresses));
        String id2 = "de-DE_edeka";
        shopListRepo.save(new ShopList(id2, addresses));

        Collection<ShopList> found = shopListRepo.findByIdAndAddressSubstr4(id1,"Street 2");
        System.out.println("4========================================");
        Assertions.assertEquals(1,found.size());
        Assertions.assertEquals(2,found.stream().toList().get(0).getAddresses().size());
    }

}
