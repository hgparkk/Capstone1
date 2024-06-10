package com.example.capstone1.Review;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReviewService {
    String BASE_PATH = "/review";

    @POST(BASE_PATH + "/registerReview")
    Call<ReviewResponse> registerReview(@Body RegisterReviewInfo registerReviewInfo);

    @POST(BASE_PATH + "/findReview")
    Call<ReviewResponse> findReview(@Body FindReviewInfo findReviewInfo);

    @POST(BASE_PATH + "/viewAllReview")
    Call<ReviewListResponse> viewAllReview(@Body ViewAllReviewInfo viewAllReviewInfo);

    @POST(BASE_PATH + "/updateReview")
    Call<ReviewResponse> updateReview(@Body UpdateReviewInfo updateReviewInfo);

    @POST(BASE_PATH + "/deleteReview")
    Call<ReviewResponse> deleteReview(@Body Review review);
}
