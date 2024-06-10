package com.example.capstone1.FavRest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindFavRestInfo {
    private String userID;
    private String code;
    public FindFavRestInfo(String userID, String code){
        this.userID = userID;
        this.code = code;
    }
}
