package com.example.capstone1.UserInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInfo {
    private String userID;
    private String userPW;

    public LoginInfo(String userID,String userPW){
        this.userID = userID;
        this.userPW = userPW;
    }
}