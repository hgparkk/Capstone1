package com.example.capstone1.RestInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRestInfo {
    private String params;
    private String cat;
    public SearchRestInfo(String params, String cat){
        this.params = params;
        this.cat = cat;
    }
}
