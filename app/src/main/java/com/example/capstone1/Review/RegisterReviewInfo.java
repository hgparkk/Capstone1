package com.example.capstone1.Review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterReviewInfo {
    private int repu;
    private String usersReview;
    private String userID;
    private String code;
    public RegisterReviewInfo(int repu, String usersReview, String userID, String code){
        this.repu = repu;
        this.usersReview = usersReview;
        this.userID = userID;
        this.code = code;
    }
}
