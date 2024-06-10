package com.example.capstone1.Review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindReviewInfo {
    private String userID;
    private String code;
    public FindReviewInfo(String userID, String code){
        this.userID = userID;
        this.code = code;
    }
}
