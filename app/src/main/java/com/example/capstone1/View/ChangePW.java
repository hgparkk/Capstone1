package com.example.capstone1.View;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.R;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.ChangePWInfo;
import com.example.capstone1.UserInfo.UserInfo;
import com.example.capstone1.UserInfo.UserInfoResponse;
import com.example.capstone1.UserInfo.UserInfoService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePW extends AppCompatActivity {

    //서버
    UserInfoService userInfoService;

    //비밀번호 체크 여부
    private boolean pwCheck = false;

    //로그인된 유저
    private ChangePWInfo changePWInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pw);

        changePWInfo = getIntent().getParcelableExtra("changePWUser");

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

        //EditText 변수에 할당하기
        EditText changePW = findViewById(R.id.changePW);
        EditText changePW2 = findViewById(R.id.changePW2);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.changePWBackButton);
        Button pwCheckButton = findViewById(R.id.pwCheckButton);
        Button changePWButton = findViewById(R.id.changePWButton);

        //서버 할당
        userInfoService = ServiceUtils.getUserInfoService();

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            if(isFinishing()){
                overridePendingTransition(R.anim.none,R.anim.to_bottom_exit);
            }
        });

        //비밀번호 입력시
        changePW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pwCheckResult.setText("");
                pwCheck = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //비밀번호 확인 버튼
        pwCheckButton.setOnClickListener(v -> {
            if (changePW.getText().toString().length() > 7) {
                if (changePW.getText().toString().equals(changePW2.getText().toString())) {
                    pwCheckResult.setTextColor(Color.parseColor("#00FF7F"));
                    pwCheckResult.setText(R.string.pwSame);
                    pwCheck = true;
                } else {
                    pwCheckResult.setTextColor(Color.parseColor("#FF0000"));
                    pwCheckResult.setText(R.string.pwDifferent);
                }
            } else {
                Toast.makeText(ChangePW.this, "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //비밀번호 변경 버튼
        changePWButton.setOnClickListener(v ->{
            if (!pwCheck) {
                Toast.makeText(ChangePW.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userInfoPW = changePW.getText().toString().trim();

            UserInfo userInfo = new UserInfo(changePWInfo.getUserID(), userInfoPW, changePWInfo.getUserName(), changePWInfo.getBirth());

            userInfoService.changePW(userInfo).enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(@NotNull Call<UserInfoResponse> call, @NotNull Response<UserInfoResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePW.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                        if(isFinishing()){
                            overridePendingTransition(R.anim.none,R.anim.to_bottom_exit);
                        }
                    } else {
                        Toast.makeText(ChangePW.this, "비밀번호 변경에 실패하였습니다." + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<UserInfoResponse> call, @NotNull Throwable t) {
                    Log.e("ChangePW", "비밀번호 변경 실패", t);
                    Toast.makeText(ChangePW.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}