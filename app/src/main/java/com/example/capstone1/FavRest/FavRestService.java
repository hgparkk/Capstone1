package com.example.capstone1.FavRest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FavRestService {
    String BASE_PATH = "/favRest";

    @POST(BASE_PATH + "/registerFavRest")
    Call<FavRestResponse> registerFavRest(@Body RegisterFavRestInfo registerFavRestInfo);

    @POST(BASE_PATH + "/findFavRest")
    Call<FavRestResponse> findFavREST(@Body FindFavRestInfo findFavRestInfo);

    @POST(BASE_PATH + "/searchFavRest")
    Call<FavRestListResponse> searchFavRest(@Body SearchFavRestInfo searchFavRestInfo);

    @POST(BASE_PATH + "/deleteFavRest")
    Call<FavRestResponse> deleteFavRest(@Body FavRest favRest);
}
