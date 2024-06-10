package com.example.capstone1.RestInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestInfoService {
    String BASE_PATH = "/restInfo";

    @POST(BASE_PATH+ "/find")
    Call<RestInfoResponse> find(@Body FindRestInfo findRestInfo);

    @POST(BASE_PATH + "/search")
    Call<RestInfoListResponse> search(@Body SearchRestInfo searchRestInfo);
}
