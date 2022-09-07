package com.example.mongoaggregation.shops.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.Objects;
import java.util.Set;

// Id string in form of: LanguageTag
// followed by undescore and shopname, eg "en-DE_lidl"
@Getter
@AllArgsConstructor
public class ShopList {
    @Id
    private String id;
    private Set<Address> addresses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopList)) return false;
        ShopList shopList = (ShopList) o;
        return id.equals(shopList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
