package com.example.capstone1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MyInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.backButton);
        Button loginAccountButton = findViewById(R.id.loginAccontButton);
        Button loginInfoButton = findViewById(R.id.loginInfoButton);

        //Intent 변수에 할당하기
        Intent loginMainIntent = new Intent(getApplicationContext(), LoginMain.class);

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> finish());

        //프로필사진 버튼
        loginAccountButton.setOnClickListener(v -> startActivity(loginMainIntent));

        //프로필정보 버튼
        loginInfoButton.setOnClickListener(v -> startActivity(loginMainIntent));
    }
}
