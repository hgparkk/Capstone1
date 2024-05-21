package com.example.capstone1.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.capstone1.R;

public class Main extends AppCompatActivity {

    //자체변수 할당하기
    boolean fragmentSwitch = true;
    FragmentManager manager;
    FragmentTransaction ft;

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
        Button searchButton = findViewById(R.id.searchButton);
        Button favRestButton = findViewById(R.id.favRestButton);

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
                    fragmentReplace(new FavRestFragment(),"two",searchButton,favRestButton);
                    fragmentSwitch=false;
                }
        });
    }
}