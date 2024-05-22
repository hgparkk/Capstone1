package com.example.capstone1;

import com.example.capstone1.UserInfo.UserInfoService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private UserInfoService userInfoService;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000") // 서버 URL로 변경
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userInfoService = retrofit.create(UserInfoService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public UserInfoService getUserInfoService() {
        return userInfoService;
    }
}