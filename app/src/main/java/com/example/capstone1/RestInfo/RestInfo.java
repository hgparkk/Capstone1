package com.example.capstone1.RestInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestInfo {
    private String code;
    private String restName;
    private String address;
    private String cat;

    public RestInfo(String code, String restName, String address, String cat){
        this.code = code;
        this.restName = restName;
        this.address = address;
        this.cat = cat;
    }
}
