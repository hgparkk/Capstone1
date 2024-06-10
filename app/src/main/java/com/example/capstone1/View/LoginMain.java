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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.ApplicationManager;
import com.example.capstone1.R;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.LoginInfo;
import com.example.capstone1.UserInfo.UserInfo;
import com.example.capstone1.UserInfo.UserInfoResponse;
import com.example.capstone1.UserInfo.UserInfoService;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMain extends AppCompatActivity {

    //서버
    UserInfoService userInfoService;

    //로그인 정보 저장하기
    private void saveLoginInfo(UserInfo userInfo, String token) {
        SharedPreferences sharedPreferences = ApplicationManager.getAppContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userInfo);
        editor.putString("userInfo", json);
        editor.putString("accessToken", token);
        editor.apply();
    }

    //네트워크 연결 여부
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        //등장 애니메이션
        overridePendingTransition(R.anim.from_bottom_enter, R.anim.none);

        //내비게이션 바 뒤로가기 버튼
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                if (isFinishing()) {
                    overridePendingTransition(R.anim.none, R.anim.to_bottom_exit);
                }
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        //EditText 변수에 할당하기
        EditText loginID = findViewById(R.id.loginID);
        EditText loginPW = findViewById(R.id.loginPW);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.loginBackButton);
        Button sign = findViewById(R.id.sign);
        Button findIDPW = findViewById(R.id.findIDPW);
        Button login = findViewById(R.id.loginButton);

        //Intent 변수에 할당하기
        Intent signIntent = new Intent(getApplicationContext(), Sign.class);
        Intent findIDPWIntent = new Intent(getApplicationContext(), FindIDAndPW.class);

        //서버 할당
        userInfoService = ServiceUtils.getUserInfoService();

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            if (isFinishing()) {
                overridePendingTransition(R.anim.none, R.anim.to_bottom_exit);
            }
        });

        //회원가입 버튼
        sign.setOnClickListener(v -> startActivity(signIntent));

        //ID/PW찾기 버튼
        findIDPW.setOnClickListener(v -> startActivity(findIDPWIntent));

        //로그인 버튼
        login.setOnClickListener(v -> {
            String userID = loginID.getText().toString().trim();
            String userPW = loginPW.getText().toString().trim();

            if (userID.isEmpty() || userPW.isEmpty()) {
                Toast.makeText(LoginMain.this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginInfo loginInfo = new LoginInfo(userID, userPW);

            userInfoService.login(loginInfo).enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(@NotNull Call<UserInfoResponse> call, @NotNull Response<UserInfoResponse> response) {
                    if (response.isSuccessful()) {
                        UserInfoResponse userInfoResponse = response.body();
                        if (userInfoResponse != null) {
                            saveLoginInfo(userInfoResponse.getUserInfo(), userInfoResponse.getAccressToken());
                            setResult(Activity.RESULT_OK);
                            finish();
                            if (isFinishing()) {
                                overridePendingTransition(R.anim.none, R.anim.to_bottom_exit);
                            }
                        }
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(LoginMain.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(LoginMain.this, "존재하지 않는 유저입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginMain.this, "로그인에 실패하였습니다." + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<UserInfoResponse> call, @NotNull Throwable t) {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(LoginMain.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginMain.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}
