package com.example.capstone1.Review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewAllReviewInfo {
    private String code;
    public ViewAllReviewInfo(String code){
        this.code = code;
    }
}
