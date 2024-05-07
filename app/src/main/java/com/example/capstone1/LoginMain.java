package com.example.capstone1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

public class LoginMain extends AppCompatActivity {
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

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.loginBackButton);
        Button sign = findViewById(R.id.sign);
        Button findIDPW = findViewById(R.id.findIDPW);

        //Intent 변수에 할당하기
        Intent signIntent = new Intent(getApplicationContext(), Sign.class);
        Intent findIDPWIntent = new Intent(getApplicationContext(), FindIDAndPW.class);

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
    }
}
