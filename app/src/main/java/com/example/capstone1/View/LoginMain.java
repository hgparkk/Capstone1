package com.example.capstone1.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.HashUtil;
import com.example.capstone1.R;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.UserInfo;
import com.example.capstone1.UserInfo.UserInfoResponse;
import com.example.capstone1.UserInfo.UserInfoService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMain extends AppCompatActivity {

    //서버
    UserInfoService userInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        //등장 애니메이션
        overridePendingTransition(R.anim.from_bottom_enter,R.anim.none);

        //내비게이션 바 뒤로가기 버튼
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                if(isFinishing()){
                    overridePendingTransition(R.anim.none,R.anim.to_bottom_exit);
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
            if(isFinishing()){
                overridePendingTransition(R.anim.none,R.anim.to_bottom_exit);
            }
        });

        //회원가입 버튼
        sign.setOnClickListener(v -> startActivity(signIntent));

        //ID/PW찾기 버튼
        findIDPW.setOnClickListener(v -> startActivity(findIDPWIntent));

        //로그인 버튼
        login.setOnClickListener(v -> {
            String userID = loginID.getText().toString();
            String userPW = loginPW.getText().toString();

            String hashedPW = HashUtil.hashPassword(userPW);

            UserInfo userInfo = new UserInfo(userID, hashedPW, null, null, 0);

            userInfoService.loginUserInfo(userInfo).enqueue(new Callback<UserInfoResponse>(){
                @Override
                public void onResponse(@NotNull Call<UserInfoResponse> call, @NotNull Response<UserInfoResponse> response){
                    if(response.isSuccessful()){
                        Toast.makeText(LoginMain.this,"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginMain.this, "로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NotNull Call<UserInfoResponse> call, @NotNull Throwable t) {
                    Log.e("Login", "Error: ", t);
                    Toast.makeText(LoginMain.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
