package com.example.capstone1.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserInfoService {
    String BASE_PATH = "/userInfo";

    @POST(BASE_PATH+"/register")
    Call<UserInfoResponse> registerUserInfo(@Body UserInfo userInfo);

//    @GET("/get/{id}")
//    Call<UserInfoResponse> getUserInfo(@Query("data") String data);
//
//    @FormUrlEncoded
//    @PUT("/put/{id}")
//    Call<UserInfoResponse> putUserInfo(@Path("id") String id, @Field("data") String data);
//
//    @DELETE("/delete/{id}")
//    Call<UserInfoResponse> deleteUserInfo(@Path("id") String id);
}
