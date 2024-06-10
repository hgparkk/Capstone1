package com.example.capstone1.RestInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindRestInfo {
    private String code;
    public FindRestInfo(String code){
        this.code = code;
    }
}
