package com.example.capstone1.RestInfo;

import com.example.capstone1.UserInfo.CompareIDInfo;
import com.example.capstone1.UserInfo.UserInfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestInfoService {
    String BASE_PATH = "/restInfo";

    @POST(BASE_PATH+"/search")
    Call<RestInfoResponse> search(@Body SearchRestInfo searchRestInfo);
}
