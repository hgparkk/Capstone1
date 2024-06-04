package com.example.capstone1.UserInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompareIDInfo {
    private String userID;
    public CompareIDInfo(String userID){
        this.userID = userID;
    }
}
