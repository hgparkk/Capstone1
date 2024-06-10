package com.example.capstone1.FavRest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchFavRestInfo {
    private String params;
    private String userID;
    public SearchFavRestInfo(String params, String userID){
        this.params = params;
        this.userID = userID;
    }
}
