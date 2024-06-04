package com.example.capstone1.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserInfoService {
    String BASE_PATH = "/userInfo";

    @POST(BASE_PATH+"/compareID")
    Call<UserInfoResponse> compareID(@Body CompareIDInfo compareIDInfo);

    @POST(BASE_PATH+"/register")
    Call<UserInfoResponse> register(@Body UserInfo userInfo);

    @POST(BASE_PATH+"/login")
    Call<UserInfoResponse> login(@Body LoginInfo loginInfo);

    @POST(BASE_PATH+"/findID")
    Call<FindIDInfoResponse> findID(@Body FindIDInfo findIDInfo);

    @POST(BASE_PATH+"/checkChangePW")
    Call<UserInfoResponse> checkChangePW(@Body ChangePWInfo changePWInfo);

    @POST(BASE_PATH+"/changePW")
    Call<UserInfoResponse> changePW(@Body UserInfo userInfo);

    @POST(BASE_PATH+"/resign")
    Call<UserInfoResponse> resignUser(@Body UserInfo userInfo);
}
