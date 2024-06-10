package com.example.capstone1.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.ApplicationManager;
import com.example.capstone1.FavRest.FavRest;
import com.example.capstone1.FavRest.FavRestResponse;
import com.example.capstone1.FavRest.FavRestService;
import com.example.capstone1.FavRest.FindFavRestInfo;
import com.example.capstone1.FavRest.RegisterFavRestInfo;
import com.example.capstone1.R;
import com.example.capstone1.RestInfo.FindRestInfo;
import com.example.capstone1.RestInfo.RestInfo;
import com.example.capstone1.RestInfo.RestInfoResponse;
import com.example.capstone1.RestInfo.RestInfoService;
import com.example.capstone1.Review.FindReviewInfo;
import com.example.capstone1.Review.Review;
import com.example.capstone1.Review.ReviewResponse;
import com.example.capstone1.Review.ReviewService;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.UserInfo;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestInfoView extends AppCompatActivity {

    //정보를 볼 가게
    RestInfo restInfo;

    //로그인 여부와 로그인 정보 저장
    UserInfo loginedUser;
    String token;

    //서버
    RestInfoService restInfoService;
    ReviewService reviewService;
    FavRestService favRestService;

    //등록된 리뷰
    Review review;

    //맛집 등록 여부
    FavRest favRest;

    //TestView 변수에 할당하기
    TextView noReviewTextView;
    TextView reviewTextView;
    TextView avgRepuTextView;

    //LinearLayout 변수에 할당하기
    LinearLayout reviewLayout;

    //RatingBar 변수에 할당하기
    RatingBar tasteRating;
    RatingBar priceRating;
    RatingBar serviceRating;
    RatingBar cleanessRating;

    //버튼 할당
    Button makeReviewButton;
    Button makeFavRestButton;
    Button deleteReviewButton;

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

    //다시 돌아왔을 때 로그인 정보 불러오기
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    getLoginInfo();
                    getReviewInfo(loginedUser);
                    getFavRestInfo(loginedUser);
                    updateRestInfo();
                }
            });

    //버튼 변경
    private void favRestButtonSet(Button button, boolean exist){
        if(exist){
            button.setText(R.string.deleteFavRest);
            button.setOnClickListener(v -> makeFavRestButtonAct(true,loginedUser));
        }else{
            button.setText(R.string.makeFavRest);
            button.setOnClickListener(v -> makeFavRestButtonAct(false,loginedUser));
        }
    }

    private void reviewButtonSet(Button button, boolean exist){
        if(exist){
            button.setText(R.string.reviewReset);
            button.setOnClickListener(v-> makeReviewButtonAct(true,loginedUser));
        }else{
            button.setText(R.string.makeReview);
            button.setOnClickListener(v-> makeReviewButtonAct(false,loginedUser));
        }
    }

    //가게 정보 업데이트
    private void updateRestInfo(){
        FindRestInfo findRestInfo = new FindRestInfo(restInfo.getCode());
        restInfoService.find(findRestInfo).enqueue(new Callback<RestInfoResponse>() {
            @Override
            public void onResponse(@NonNull Call<RestInfoResponse> call, @NonNull Response<RestInfoResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    restInfo = response.body().getRestInfo();
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
                    String formattedValue = decimalFormat.format(restInfo.getAvgRepu());
                    avgRepuTextView.setText(formattedValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestInfoResponse> call, @NonNull Throwable t) {
                if (isNetworkAvailable()) {
                    Toast.makeText(RestInfoView.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RestInfoView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //맛집 여부 불러오기
    private void getFavRestInfo(UserInfo logined) {
        if(logined != null) {
            FindFavRestInfo findFavRestInfo = new FindFavRestInfo(logined.getUserID(), restInfo.getCode());
            favRestService.findFavREST(findFavRestInfo).enqueue(new Callback<FavRestResponse>() {
                @Override
                public void onResponse(@NonNull Call<FavRestResponse> call, @NonNull Response<FavRestResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        favRest = response.body().getFavRest() != null ? response.body().getFavRest() : new FavRest(0, null, null);
                        favRestButtonSet(makeFavRestButton, favRest.getFavRestID() != 0);
                    }
                    else{
                        favRestButtonSet(makeFavRestButton, false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FavRestResponse> call, @NonNull Throwable t) {
                    if (isNetworkAvailable()) {
                        Toast.makeText(RestInfoView.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RestInfoView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            favRestButtonSet(makeFavRestButton, false);
        }
    }

    //등록된 리뷰 확인하기
    private void getReviewInfo(UserInfo logined){
        if(logined!=null) {
            FindReviewInfo findReviewInfo = new FindReviewInfo(logined.getUserID(), restInfo.getCode());
            reviewService.findReview(findReviewInfo).enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        review = response.body().getReview() != null ? response.body().getReview() : new Review(0, 0, null, null, null,null);
                        if (review.getReviewID() != 0) {
                            noReviewTextView.setVisibility(View.GONE);
                            reviewLayout.setVisibility(View.VISIBLE);
                            tasteRating.setRating((float) (review.getRepu() / 1000));
                            priceRating.setRating((float) ((review.getRepu() % 1000) / 100));
                            serviceRating.setRating((float) ((review.getRepu() % 100) / 10));
                            cleanessRating.setRating((float) (review.getRepu() % 10));
                            reviewTextView.setText(review.getUsersReview());
                            reviewButtonSet(makeReviewButton, true);
                            deleteReviewButton.setVisibility(View.VISIBLE);
                        } else {
                            noReviewTextView.setVisibility(View.VISIBLE);
                            reviewLayout.setVisibility(View.GONE);
                            reviewButtonSet(makeReviewButton, false);
                            deleteReviewButton.setVisibility(View.GONE);
                        }
                    } else {
                        noReviewTextView.setVisibility(View.VISIBLE);
                        reviewLayout.setVisibility(View.GONE);
                        reviewButtonSet(makeReviewButton, false);
                        deleteReviewButton.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                    if (isNetworkAvailable()) {
                        Toast.makeText(RestInfoView.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RestInfoView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            noReviewTextView.setText("로그인 후 사용해주세요.");
            noReviewTextView.setVisibility(View.VISIBLE);
            reviewLayout.setVisibility(View.GONE);
            reviewButtonSet(makeReviewButton, false);
        }
    }

    //리뷰 등록 버튼 액트
    private void makeReviewButtonAct(Boolean exist, UserInfo logined) {
        if(logined!=null) {
            Intent registerReviewIntent = new Intent(getApplicationContext(), RegisterReview.class);
            if (exist) {
                registerReviewIntent.putExtra("review", new Gson().toJson(review));
            }
            registerReviewIntent.putExtra("restInfo", new Gson().toJson(restInfo));
            startActivityResult.launch(registerReviewIntent);
        }else{
            Intent loginIntent = new Intent(getApplicationContext(),LoginMain.class);
            startActivityResult.launch(loginIntent);
        }
    }

    //맛집 등록 버튼 액트
    private void makeFavRestButtonAct(Boolean exist, UserInfo logined) {
        if(logined!=null) {
            if (exist) {
                AlertDialog.Builder deleteFavRestBuilder = new AlertDialog.Builder(RestInfoView.this)
                        .setMessage("맛집리스트에서 삭제하시겠습니까?")
                        .setPositiveButton("예", (dialogInterface, i) -> favRestService.deleteFavRest(favRest).enqueue(new Callback<FavRestResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<FavRestResponse> call, @NonNull Response<FavRestResponse> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(RestInfoView.this, "맛집에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    favRestButtonSet(makeFavRestButton, false);
                                } else {
                                    Toast.makeText(RestInfoView.this, "맛집에서 삭제에 실패하였습니다." + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<FavRestResponse> call, @NonNull Throwable t) {
                                if (isNetworkAvailable()) {
                                    Toast.makeText(RestInfoView.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RestInfoView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }))
                        .setNegativeButton("아니오", (dialogInterface, i) -> {
                        });
                AlertDialog deleteFavRestDialog = deleteFavRestBuilder.create();
                deleteFavRestDialog.show();
            } else {
                AlertDialog.Builder makeFavRestBuilder = new AlertDialog.Builder(RestInfoView.this)
                        .setMessage("맛집리스트에 등록하시겠습니까?")
                        .setPositiveButton("예", (dialogInterface, i) -> {
                            RegisterFavRestInfo registerFavRestInfo = new RegisterFavRestInfo(logined.getUserID(), restInfo.getCode());
                            favRestService.registerFavRest(registerFavRestInfo).enqueue(new Callback<FavRestResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<FavRestResponse> call, @NonNull Response<FavRestResponse> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(RestInfoView.this, "맛집에 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                        favRestButtonSet(makeFavRestButton, true);
                                    } else {
                                        Toast.makeText(RestInfoView.this, "맛집 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<FavRestResponse> call, @NonNull Throwable t) {
                                    if (isNetworkAvailable()) {
                                        Toast.makeText(RestInfoView.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RestInfoView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        })
                        .setNegativeButton("아니오", (dialogInterface, i) -> {
                        });
                AlertDialog makeFavRestDialog = makeFavRestBuilder.create();
                makeFavRestDialog.show();
            }
        }else{
            Intent loginIntent = new Intent(getApplicationContext(),LoginMain.class);
            startActivityResult.launch(loginIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_info);

        //등장 애니메이션
        overridePendingTransition(R.anim.from_right_enter, R.anim.none);

        //내비게이션 바 뒤로가기 버튼
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setResult(Activity.RESULT_OK);
                finish();
                if (isFinishing()) {
                    overridePendingTransition(R.anim.none, R.anim.to_right_exit);
                }
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        //전 액티비티에서 보낸 데이터 할당
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("restInfo")) {
            String restInfoJson = intent.getStringExtra("restInfo");

            restInfo = new Gson().fromJson(restInfoJson, RestInfo.class);
        }

        //TestView 변수에 할당하기
        noReviewTextView = findViewById(R.id.noReview);
        TextView restNameTextView = findViewById(R.id.restName);
        TextView addressTextView = findViewById(R.id.addressInfo);
        avgRepuTextView = findViewById(R.id.avgRepuInfo);
        reviewTextView = findViewById(R.id.myReview);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.backButton);
        Button viewAllReviewButton = findViewById(R.id.viewAllReview);
        makeReviewButton = findViewById(R.id.manageReview);
        makeFavRestButton = findViewById(R.id.manageFavRest);
        deleteReviewButton = findViewById(R.id.deleteReview);

        //LinearLayout 변수에 할당하기
        reviewLayout = findViewById(R.id.reviewLayout);

        //RatingBar 변수에 할당하기
        tasteRating = findViewById(R.id.tasteRating);
        priceRating = findViewById(R.id.priceRating);
        serviceRating = findViewById(R.id.serviceRating);
        cleanessRating = findViewById(R.id.cleanessRating);

        //서버할당
        reviewService = ServiceUtils.getReviewService();
        favRestService = ServiceUtils.getFavRestService();
        restInfoService = ServiceUtils.getRestInfoService();

        //TextView 텍스트 변환
        restNameTextView.setText(restInfo.getRestName());
        addressTextView.setText(restInfo.getAddress());
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
        String formattedValue = decimalFormat.format(restInfo.getAvgRepu());
        avgRepuTextView.setText(formattedValue);

        //로그인 정보 불러오기
        getLoginInfo();

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
            if (isFinishing()) {
                overridePendingTransition(R.anim.none, R.anim.to_right_exit);
            }
        });

        //리뷰전체보기 버튼
        viewAllReviewButton.setOnClickListener(v -> {
            Intent allReivewIntent = new Intent(getApplicationContext(),AllReview.class);
            allReivewIntent.putExtra("restInfo", new Gson().toJson(restInfo));
            startActivity(allReivewIntent);
        });

        //맛집 등록 여부 확인하기
        getFavRestInfo(loginedUser);
        getReviewInfo(loginedUser);

        deleteReviewButton.setOnClickListener(v -> {
                    AlertDialog.Builder deleteReviewBuilder = new AlertDialog.Builder(RestInfoView.this)
                            .setMessage("리뷰를 삭제하시겠습니까?")
                            .setPositiveButton("예", (dialogInterface, i) -> reviewService.deleteReview(review).enqueue(new Callback<ReviewResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(RestInfoView.this, "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        getLoginInfo();
                                        getFavRestInfo(loginedUser);
                                        getReviewInfo(loginedUser);
                                        updateRestInfo();

                                    } else {
                                        Toast.makeText(RestInfoView.this, "리뷰 삭제에 실패하였습니다." + response.code(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                                    if (isNetworkAvailable()) {
                                        Toast.makeText(RestInfoView.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RestInfoView.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }))
                            .setNegativeButton("아니오", (dialogInterface, i) -> {
                            });
                    AlertDialog deleteReviewDialog = deleteReviewBuilder.create();
                    deleteReviewDialog.show();
                }
        );
    }
}
