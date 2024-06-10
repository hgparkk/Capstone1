package com.example.capstone1.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {
    private int reviewID;
    private int repu;
    private String usersReview;
    private String userID;
    private String code;
    private String created_at;
    public Review(int reviewID, int repu, String usersReview, String userID, String code, String created_at){
        this.reviewID = reviewID;
        this.repu = repu;
        this.usersReview = usersReview;
        this.userID = userID;
        this.code = code;
        this.created_at = created_at;
    }

    public LocalDateTime getCreateAt(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return LocalDateTime.parse(created_at, formatter);
    }
}
