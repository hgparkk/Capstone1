package com.example.capstone1.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.capstone1.ApplicationManager;
import com.example.capstone1.R;
import com.example.capstone1.UserInfo.UserInfo;
import com.google.gson.Gson;

public class Main extends AppCompatActivity {

    //로그인 여부
    UserInfo loginedUser;
    String token;

    //자체변수 할당하기
    boolean fragmentSwitch = true;
    FragmentManager manager;
    FragmentTransaction ft;

    //프레그먼트 바꾸기 함수
    private void fragmentReplace(Fragment fragment, String tag, Button searchButton, Button favRestButton)
    {
        ft=manager.beginTransaction();
        ft.replace(R.id.mainFrameContainer, fragment, tag);
        ft.commit();

        searchButton.setBackgroundColor(Color.parseColor(fragmentSwitch ? "#A0A0A0" : "#FF7F00"));
        searchButton.setTextColor(Color.parseColor(fragmentSwitch ? "#000000" : "#FFFFFF"));
        favRestButton.setBackgroundColor(Color.parseColor(fragmentSwitch ? "#FF7F00" : "#A0A0A0"));
        favRestButton.setTextColor(Color.parseColor(fragmentSwitch ? "#FFFFFF" : "#000000"));
    }

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

    Button searchButton;
    Button favRestButton;

    //로그인 후 다시 돌아왔을 때
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    getLoginInfo();
                    fragmentReplace(new FavRestFragment(),"two",searchButton,favRestButton);
                    fragmentSwitch=false;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //시작할때부터 프래그먼트 띄운상태로 실행하기
        manager = getSupportFragmentManager();
        ft = manager.beginTransaction();
        ft.replace(R.id.mainFrameContainer, new SearchFragment());
        ft.commit();

        //EditText 변수에 할당하기

        //Button 변수에 할당하기
        Button myInfoButton = findViewById(R.id.myInfoButton);
        searchButton = findViewById(R.id.searchButton);
        favRestButton = findViewById(R.id.favRestButton);

        //TextView 변수에 할당하기

        //Intent 변수에 할당하기
        Intent myInfoIntent = new Intent(getApplicationContext(), MyInfo.class);

        //내정보 버튼
        myInfoButton.setOnClickListener(v -> startActivity(myInfoIntent));

        //가게 검색 버튼
        searchButton.setOnClickListener(v ->{
                if(!fragmentSwitch)
                {
                    fragmentReplace(new SearchFragment(),"one",searchButton,favRestButton);
                    fragmentSwitch=true;
                }
        });

        //맛집 리스트 버튼
        favRestButton.setOnClickListener(v ->{
                if(fragmentSwitch)
                {
                    getLoginInfo();
                    if (loginedUser==null){
                        Intent intent = new Intent(getApplicationContext(),LoginMain.class);
                        startActivityResult.launch(intent);
                    }
                    else{
                        fragmentReplace(new FavRestFragment(),"two",searchButton,favRestButton);
                        fragmentSwitch=false;
                    }
                }
        });
    }
}