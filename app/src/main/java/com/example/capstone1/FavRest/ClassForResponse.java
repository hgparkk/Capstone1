package com.example.capstone1.FavRest;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassForResponse {
    private int favRestID;
    private String userID;
    private String code;
    private String restName;
    private String address;
    private String cat;
    private int reviewCount;
    private BigDecimal avgRepu;
    public ClassForResponse (int favRestID, String userID, String code, String restName, String address, String cat, int reviewCount, BigDecimal avgRepu){
        this.favRestID = favRestID;
        this.userID = userID;
        this.code = code;
        this.restName = restName;
        this.address = address;
        this.cat = cat;
        this.reviewCount = reviewCount;
        this.avgRepu = avgRepu;
    }
}
