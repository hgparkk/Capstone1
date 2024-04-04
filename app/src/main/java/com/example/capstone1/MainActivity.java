package com.example.capstone1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);

        //EditText 변수에 할당하기

        //Button 변수에 할당하기
        Button filterButton = (Button)findViewById(R.id.filterButton);
        //TextView 변수에 할당하기
        TextView textView = (TextView)findViewById(R.id.textView);
        //필터 버튼
        filterButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                FilterSet filterset = new FilterSet();
                filterset.show(getSupportFragmentManager(),filterset.getTag());
            }
        });
    }
}