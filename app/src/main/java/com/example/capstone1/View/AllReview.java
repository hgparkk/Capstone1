package com.example.capstone1.View;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.R;
import com.example.capstone1.RestInfo.RestInfo;
import com.example.capstone1.Review.Review;
import com.example.capstone1.Review.ReviewListResponse;
import com.example.capstone1.Review.ReviewService;
import com.example.capstone1.Review.ViewAllReviewInfo;
import com.example.capstone1.ServiceUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllReview extends AppCompatActivity {

    //받을 파라미터
    RestInfo restInfo;

    //서버
    ReviewService reviewService;

    //검색결과 어댑터
    AllReviewAdapter allReviewAdapter;

    //검색결과
    List<Review> searchedList = new ArrayList<>();

    //TextView 변수선언
    TextView emptyTextView;

    //RecyclerView 변수선언
    RecyclerView reviewListView;

    //정렬 버튼
    Button fromRepuButton;
    Button fromDateButton;

    //네트워크 연결 확인
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
            return false;
        }
        return false;
    }

    //데이터가 비어있는지 확인하기
    private void checkIfEmpty() {
        if (searchedList.isEmpty()) {
            reviewListView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.noReview);
            fromDateButton.setVisibility(View.GONE);
            fromRepuButton.setVisibility(View.GONE);

        } else {
            reviewListView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
            fromDateButton.setVisibility(View.VISIBLE);
            fromRepuButton.setVisibility(View.VISIBLE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_review);

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
        }

        //TextView 변수에 할당하기
        TextView restNameTextView = findViewById(R.id.restName);
        emptyTextView = findViewById(R.id.empty);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.backButton);
        fromRepuButton = findViewById(R.id.fromRepu);
        fromDateButton = findViewById(R.id.fromDate);

        //RecyclerView 변수에 할당하기
        reviewListView = findViewById(R.id.reviewListView);

        //AllReviewAdapter 변수에 할당하기
        allReviewAdapter = new AllReviewAdapter(searchedList);
        reviewListView.setAdapter(allReviewAdapter);
        reviewListView.setLayoutManager(new LinearLayoutManager(this));

        //서버 할당
        reviewService = ServiceUtils.getReviewService();

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            if (isFinishing()) {
                overridePendingTransition(R.anim.none, R.anim.to_right_exit);
            }
        });

        //가게이름 설정
        restNameTextView.setText(restInfo.getRestName());

        //리뷰 검색
        ViewAllReviewInfo viewAllReviewInfo = new ViewAllReviewInfo(restInfo.getCode());
        reviewService.viewAllReview(viewAllReviewInfo).enqueue(new Callback<ReviewListResponse>() {
            @Override
            public void onResponse(@NotNull Call<ReviewListResponse> call, @NotNull Response<ReviewListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> reviewList = response.body().getReviewList() != null ? response.body().getReviewList() : new ArrayList<>();
                    searchedList.clear();
                    searchedList.addAll(reviewList);
                    checkIfEmpty();
                    allReviewAdapter.updateDataList(searchedList);
                } else {
                    if (response.code() == 404) {
                        checkIfEmpty();
                    } else {
                        Toast.makeText(AllReview.this, "검색에 실패하였습니다. " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ReviewListResponse> call, @NotNull Throwable t) {
                if (!isNetworkAvailable()) {
                    emptyTextView.setText("인터넷 상태가 불안정 합니다");
                } else {
                    emptyTextView.setText("데이터를 불러오는 데 실패했습니다.");
                }
            }
        });

        //평점순 버튼
        fromRepuButton.setOnClickListener(v-> {
            searchedList.sort((r1, r2) -> Integer.compare(r2.getRepu(), r1.getRepu()));
            allReviewAdapter.updateDataList(searchedList);
            fromRepuButton.setBackgroundResource(R.drawable.sort_button);
            fromDateButton.setBackgroundResource(R.drawable.filter_set_button);
        });

        //작성순 버튼
        fromDateButton.setOnClickListener(v-> {
            searchedList.sort((r1, r2) -> r2.getCreateAt().compareTo(r1.getCreateAt()));
            allReviewAdapter.updateDataList(searchedList);
            fromRepuButton.setBackgroundResource(R.drawable.filter_set_button);
            fromDateButton.setBackgroundResource(R.drawable.sort_button);
        });
    }
}
