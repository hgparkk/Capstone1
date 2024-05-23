package com.example.capstone1.View;

import static com.example.capstone1.HashUtil.hashPassword;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.R;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.UserInfo;
import com.example.capstone1.UserInfo.UserInfoResponse;
import com.example.capstone1.UserInfo.UserInfoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sign extends AppCompatActivity {

    //서버
    UserInfoService userInfoService;

    //비밀번호 체크 여부
    boolean pwCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

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

        //TextView 변수에 할당하기
        TextView pwCheckResult = findViewById(R.id.pwCheckResult);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.signBackButton);
        Button pwCheckButton = findViewById(R.id.pwCheckButton);
        Button signButton = findViewById(R.id.signButton);

        //EditTest 변수에 할당하기
        EditText signID = findViewById(R.id.signID);
        EditText signPW = findViewById(R.id.signPW);
        EditText signPW2 = findViewById(R.id.signPW2);
        EditText signName = findViewById(R.id.signName);
        EditText signBirth = findViewById(R.id.signBirth);

        //Intent 변수에 할당하기
        Intent loginIntent = new Intent(getApplicationContext(), LoginMain.class);

        //서버 할당
        userInfoService = ServiceUtils.getUserInfoService();

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            if(isFinishing()){
                overridePendingTransition(R.anim.none,R.anim.to_bottom_exit);
            }
        });

        //비밀번호 확인 버튼
        pwCheckButton.setOnClickListener(v -> {
            if (signPW.getText().toString().equals(signPW2.getText().toString()))
            {
                pwCheckResult.setTextColor(Color.parseColor("#00FF7F"));
                pwCheckResult.setText(R.string.same);
                pwCheck = true;
            }
            else
            {
                pwCheckResult.setTextColor(Color.parseColor("#FF0000"));
                pwCheckResult.setText(R.string.defferent);
                pwCheck = false;
            }
        });

        //회원가입 버튼
        signButton.setOnClickListener(v -> {
            if(pwCheck)
            {
                String userInfoID = signID.getText().toString().trim();
                String userInfoPW = signPW.getText().toString().trim();
                String userInfoName = signName.getText().toString().trim();
                String userInfoBirth = signBirth.getText().toString().trim();

                //비밀번호 암호화
                String HashedPassword = hashPassword(userInfoPW);

                //객체 생성
                UserInfo userInfo = new UserInfo(userInfoID, HashedPassword, userInfoName, userInfoBirth, 0);

                userInfoService.registerUserInfo(userInfo).enqueue(new Callback<UserInfoResponse>() {
                    public void onResponse(@NonNull Call<UserInfoResponse> call, @NonNull Response<UserInfoResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Sign.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                            startActivity(loginIntent);
                        } else {
                            if (response.code() == 400){
                                Toast.makeText(Sign.this, "중복되는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Sign.this, "회원가입에 실패하였습니다 "+response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserInfoResponse> call, @NonNull Throwable t) {
                        Log.e("Sign", "유저정보 쓰기 실패", t);
                        Toast.makeText(Sign.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(Sign.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}