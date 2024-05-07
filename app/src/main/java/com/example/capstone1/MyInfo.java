package com.example.capstone1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

public class MyInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info);

        //등장 애니메이션
        overridePendingTransition(R.anim.from_right_enter,R.anim.none);

        //내비게이션 바 뒤로가기 버튼
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                if(isFinishing()){
                    overridePendingTransition(R.anim.none,R.anim.to_right_exit);
                }
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.backButton);
        Button loginAccountButton = findViewById(R.id.loginAccontButton);
        Button loginInfoButton = findViewById(R.id.loginInfoButton);

        //Intent 변수에 할당하기
        Intent loginMainIntent = new Intent(getApplicationContext(), LoginMain.class);

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            if(isFinishing()){
                overridePendingTransition(R.anim.none,R.anim.to_right_exit);
            }
        });

        //프로필사진 버튼
        loginAccountButton.setOnClickListener(v -> startActivity(loginMainIntent));

        //프로필정보 버튼
        loginInfoButton.setOnClickListener(v -> startActivity(loginMainIntent));
    }
}
