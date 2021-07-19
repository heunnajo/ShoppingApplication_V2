package jpabook_v2.jpashop_v2.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

//JPA의 내장타입이기 때문에 어딘가에 내장될 수 있다는
@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address(){

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
