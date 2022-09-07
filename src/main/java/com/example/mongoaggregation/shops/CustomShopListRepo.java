package com.example.mongoaggregation.shops;

import com.example.mongoaggregation.shops.model.Address;
import com.example.mongoaggregation.shops.model.ShopList;

import java.util.Collection;
import java.util.List;

public interface CustomShopListRepo {
    Collection<ShopList> findByIdAndAddressSubstr1(String id, String search);
    Collection<Address> findByIdAndAddressSubstr2(String id, String search);
    Collection<Address> findByIdAndAddressSubstr3(String id, String search);
    List<Address> findByIdAndAddressSubstr32(String id, String search);
    Collection<ShopList> findByIdAndAddressSubstr4(String id, String search);
}
