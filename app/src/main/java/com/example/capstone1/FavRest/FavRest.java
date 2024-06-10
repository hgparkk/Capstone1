package com.example.capstone1.FavRest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavRest {
    private int favRestID;
    private String userID;
    private String code;
    public FavRest (int favRestID, String userID, String code){
        this.favRestID = favRestID;
        this.userID = userID;
        this.code = code;
    }
}
