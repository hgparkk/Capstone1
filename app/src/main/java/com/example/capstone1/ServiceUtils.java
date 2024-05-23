package com.example.capstone1;

import com.example.capstone1.UserInfo.UserInfoService;

public class ServiceUtils {
    public static UserInfoService getUserInfoService() {
        return RetrofitClient.getClient().create(UserInfoService.class);
    }
}
