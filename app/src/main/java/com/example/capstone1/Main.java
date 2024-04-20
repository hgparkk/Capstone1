package com.example.capstone1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Main extends AppCompatActivity {

    //자체변수 할당하기
    boolean fragmentSwitch = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //EditText 변수에 할당하기

        //Button 변수에 할당하기
        Button myInfoButton = findViewById(R.id.myInfoButton);
        Button searchButton = findViewById(R.id.searchButton);
        Button favRestButton = findViewById(R.id.favRestButton);
        //TextView 변수에 할당하기

        //시작할때부터 프래그먼트 띄운상태로 실행하기
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.mainFrameContainer, new SearchFragment());
        ft.commit();

        //내정보 버튼
        myInfoButton.setOnClickListener(v ->{
                Intent myInfoIntent = new Intent(getApplicationContext(), MyInfo.class);
                startActivity(myInfoIntent);
        });

        //가게 검색 버튼
        searchButton.setOnClickListener(v ->{
                if(!fragmentSwitch)
                {
                    ft.replace(R.id.mainFrameContainer, new SearchFragment(), "one");
                    ft.commit();

                    searchButton.setBackgroundColor(Color.parseColor("#FF7F00"));
                    searchButton.setTextColor(Color.parseColor("#FFFFFF"));
                    favRestButton.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    favRestButton.setTextColor(Color.parseColor("#000000"));
                    fragmentSwitch=true;
                }
        });

        //맛집 리스트 버튼
        favRestButton.setOnClickListener(v ->{
                if(fragmentSwitch)
                {
                    ft.replace(R.id.mainFrameContainer, new FavRestFragment(), "two");
                    ft.commit();

                    searchButton.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    searchButton.setTextColor(Color.parseColor("#000000"));
                    favRestButton.setBackgroundColor(Color.parseColor("#FF7F00"));
                    favRestButton.setTextColor(Color.parseColor("#FFFFFF"));
                    fragmentSwitch=false;
                }
        });
    }
}