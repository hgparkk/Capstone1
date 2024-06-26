package com.example.capstone1.RestInfo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestInfo {
    private String code;
    private String restName;
    private String address;
    private String cat;
    private int reviewCount;
    private BigDecimal avgRepu;

    public RestInfo(String code, String restName, String address, String cat, int reviewCount, BigDecimal avgRepu ){
        this.code = code;
        this.restName = restName;
        this.address = address;
        this.cat = cat;
        this.reviewCount = reviewCount;
        this.avgRepu = avgRepu;
    }
}
