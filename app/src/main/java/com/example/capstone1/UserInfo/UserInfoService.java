package com.example.capstone1.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserInfoService {
    String BASE_PATH = "/userInfo";

    @GET(BASE_PATH+"/idCompare")
    Call<UserInfoResponse> idCompare(@Query("userID") String userID);

    @POST(BASE_PATH+"/register")
    Call<UserInfoResponse> registerUserInfo(@Body UserInfo userInfo);

    @POST(BASE_PATH+"/login")
    Call<UserInfoResponse> loginUserInfo(@Body UserInfo userInfo);
//
//    @FormUrlEncoded
//    @PUT("/put/{id}")
//    Call<UserInfoResponse> putUserInfo(@Path("id") String id, @Field("data") String data);
//
//    @DELETE("/delete/{id}")
//    Call<UserInfoResponse> deleteUserInfo(@Path("id") String id);
}
