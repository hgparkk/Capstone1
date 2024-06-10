package com.example.capstone1.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.ApplicationManager;
import com.example.capstone1.R;
import com.example.capstone1.RestInfo.RestInfo;
import com.example.capstone1.Review.RegisterReviewInfo;
import com.example.capstone1.Review.Review;
import com.example.capstone1.Review.ReviewResponse;
import com.example.capstone1.Review.ReviewService;
import com.example.capstone1.Review.UpdateReviewInfo;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.UserInfo;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterReview extends AppCompatActivity {

    //정보를 볼 가게
    RestInfo restInfo;

    //리뷰 수정시 등록된 리뷰
    Review review;

    //로그인 여부와 로그인 정보 저장
    UserInfo loginedUser;
    String token;

    //서버
    ReviewService reviewService;

    //유저 정보 불러오기
    private UserInfo getUserInfo() {
        SharedPreferences sharedPreferences = ApplicationManager.getAppContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("userInfo", null);
        return gson.fromJson(json, UserInfo.class);
    }

    //토큰 불러오기
    private String getToken() {
        SharedPreferences sharedPreferences = ApplicationManager.getAppContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null);
    }

    //로그인한 유저 정보 불러오기
    private void getLoginInfo() {
        loginedUser = getUserInfo();
        token = getToken();
    }

    //네트워크 연결 여부
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities == null ||
                        !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
            return true;
        }
        return true;
    }

    //버튼 변경
    private void buttonSet(boolean exist) {
        if (!exist) {
            if (review.getRepu() / 1000 == 0 || (review.getRepu() % 1000) / 100 == 0 || (review.getRepu() % 100) / 10 == 0 || review.getRepu() % 10 == 0) {
                Toast.makeText(RegisterReview.this, "평점을 모두 등록해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (review.getUsersReview().isEmpty()) {
                Toast.makeText(RegisterReview.this, "리뷰를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterReviewInfo regiterReviewInfo = new RegisterReviewInfo(review.getRepu(), review.getUsersReview(), review.getUserID(), review.getCode());
            reviewService.registerReview(regiterReviewInfo).enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(@NotNull Call<ReviewResponse> call, @NotNull Response<ReviewResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterReview.this, "리뷰가 등록되었습니다", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                        if (isFinishing()) {
                            overridePendingTransition(R.anim.none, R.anim.to_right_exit);
                        }
                    } else {
                        Toast.makeText(RegisterReview.this, "리뷰 등록에 실패하였습니다" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ReviewResponse> call, @NotNull Throwable t) {
                    if (isNetworkAvailable()) {
                        Toast.makeText(RegisterReview.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterReview.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            if (review.getRepu() / 1000 == 0 || (review.getRepu() % 1000) / 100 == 0 || (review.getRepu() % 100) / 10 == 0 || review.getRepu() % 10 == 0) {
                Toast.makeText(RegisterReview.this, "평점을 모두 등록해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (review.getUsersReview().isEmpty()) {
                Toast.makeText(RegisterReview.this, "리뷰를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            UpdateReviewInfo updateReviewInfo = new UpdateReviewInfo(review.getReviewID(), review.getRepu(), review.getUsersReview());
            reviewService.updateReview(updateReviewInfo).enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(@NotNull Call<ReviewResponse> call, @NotNull Response<ReviewResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterReview.this, "리뷰가 수정되었습니다", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                        if (isFinishing()) {
                            overridePendingTransition(R.anim.none, R.anim.to_right_exit);
                        }
                    } else {
                        Toast.makeText(RegisterReview.this, "리뷰 수정에 실패하였습니다" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ReviewResponse> call, @NotNull Throwable t) {
                    if (isNetworkAvailable()) {
                        Toast.makeText(RegisterReview.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterReview.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_review);

        //등장 애니메이션
        overridePendingTransition(R.anim.from_right_enter, R.anim.none);

        //내비게이션 바 뒤로가기 버튼
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                if (isFinishing()) {
                    overridePendingTransition(R.anim.none, R.anim.to_right_exit);
                }
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        //전 액티비티에서 보낸 변수 할당
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("restInfo")) {
                String restInfoJson = intent.getStringExtra("restInfo");
                restInfo = new Gson().fromJson(restInfoJson, RestInfo.class);
            }
            if (intent.hasExtra("review")) {
                String reviewJson = intent.getStringExtra("review");
                if (reviewJson != null) {
                    review = new Gson().fromJson(reviewJson, Review.class);
                } else {
                    review = new Review(0, 0, null, null, null,null);
                }
            } else {
                review = new Review(0, 0, null, null, null,null);
            }
        }

        //TextView 변수에 할당하기
        TextView restNameTextView = findViewById(R.id.restName);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.backButton);
        Button sendReviewButton = findViewById(R.id.sendReview);

        //EditText 변수에 할당하기
        EditText reviewEditText = findViewById(R.id.myReview);

        //RatingBar 변수에 할당하기
        RatingBar tasteRating = findViewById(R.id.tasteRating);
        RatingBar priceRating = findViewById(R.id.priceRating);
        RatingBar serviceRating = findViewById(R.id.serviceRating);
        RatingBar cleanessRating = findViewById(R.id.cleanessRating);

        //서버할당
        reviewService = ServiceUtils.getReviewService();

        //로그인 정보 불러오기
        getLoginInfo();

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            if (isFinishing()) {
                overridePendingTransition(R.anim.none, R.anim.to_right_exit);
            }
        });

        //가게이름 등록
        restNameTextView.setText(restInfo.getRestName());

        //리뷰 수정시 그 전 리뷰 자동 세팅
        if (review.getReviewID() != 0) {
            tasteRating.setRating((float) (review.getRepu() / 1000));
            priceRating.setRating((float) ((review.getRepu() % 1000) / 100));
            serviceRating.setRating((float) ((review.getRepu() % 100) / 10));
            cleanessRating.setRating((float) (review.getRepu() % 10));
            reviewEditText.setText(review.getUsersReview());

            sendReviewButton.setText(R.string.updateReview);
        } else {
            sendReviewButton.setText(R.string.registReview);
        }

        sendReviewButton.setOnClickListener(v -> {
            int repu = ((int) tasteRating.getRating()) * 1000 + ((int) priceRating.getRating()) * 100 + ((int) serviceRating.getRating() * 10) + ((int) cleanessRating.getRating());
            review.setRepu(repu);
            review.setUsersReview(reviewEditText.getText().toString().trim());
            if (review.getReviewID() == 0) {
                review.setUserID(loginedUser.getUserID());
                review.setCode(restInfo.getCode());
            }
            buttonSet(review.getReviewID() != 0);
        });
    }
}
