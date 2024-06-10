package com.example.capstone1;

import com.example.capstone1.FavRest.FavRestService;
import com.example.capstone1.RestInfo.RestInfoService;
import com.example.capstone1.Review.ReviewService;
import com.example.capstone1.UserInfo.UserInfoService;

public class ServiceUtils {
    public static UserInfoService getUserInfoService() {
        return RetrofitClient.getClient().create(UserInfoService.class);
    }

    public static RestInfoService getRestInfoService(){
        return RetrofitClient.getClient().create(RestInfoService.class);
    }

    public static ReviewService getReviewService(){
        return RetrofitClient.getClient().create(ReviewService.class);
    }

    public static FavRestService getFavRestService(){
        return RetrofitClient.getClient().create(FavRestService.class);
    }
}
