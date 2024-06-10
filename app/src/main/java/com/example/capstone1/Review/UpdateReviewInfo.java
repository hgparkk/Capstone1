package com.example.capstone1.Review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReviewInfo {
    private int reviewID;
    private int repu;
    private String usersReview;

    public UpdateReviewInfo(int reviewID, int repu, String usersReview) {
        this.reviewID = reviewID;
        this.repu = repu;
        this.usersReview = usersReview;
    }
}
