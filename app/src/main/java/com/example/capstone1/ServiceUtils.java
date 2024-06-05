package com.example.capstone1;

import com.example.capstone1.RestInfo.RestInfoService;
import com.example.capstone1.UserInfo.UserInfoService;

public class ServiceUtils {
    public static UserInfoService getUserInfoService() {
        return RetrofitClient.getClient().create(UserInfoService.class);
    }

    public static RestInfoService getRestInfoService(){
        return RetrofitClient.getClient().create(RestInfoService.class);
    }
}
