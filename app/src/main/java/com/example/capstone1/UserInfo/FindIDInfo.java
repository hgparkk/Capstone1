package com.example.capstone1.UserInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindIDInfo {
    private String userName;
    private String birth;
    public FindIDInfo(String userName, String birth){
        this.userName = userName;
        this.birth = birth;
    }
}
