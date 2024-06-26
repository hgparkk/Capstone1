package com.example.capstone1.UserInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private String userID;
    private String userPW;
    private String userName;
    private String birth;

    public UserInfo(String userID, String userPW, String userName, String birth){
        this.userID = userID;
        this.userPW = userPW;
        this.userName = userName;
        this.birth = birth;
    }
}
