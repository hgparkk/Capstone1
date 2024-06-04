package com.example.capstone1.View;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.R;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.ChangePWInfo;
import com.example.capstone1.UserInfo.FindIDInfo;
import com.example.capstone1.UserInfo.FindIDInfoResponse;
import com.example.capstone1.UserInfo.UserInfo;
import com.example.capstone1.UserInfo.UserInfoResponse;
import com.example.capstone1.UserInfo.UserInfoService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindIDAndPW extends AppCompatActivity {

    //서버
    UserInfoService userInfoService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id_and_pw);

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
        EditText findIDName = findViewById(R.id.findIDName);
        EditText findIDBirth = findViewById(R.id.findIDBirth);
        EditText resetPWID = findViewById(R.id.resetPWID);
        EditText resetPWName = findViewById(R.id.resetPWName);
        EditText resetPWBirth = findViewById(R.id.resetPWBirth);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.signBackButton);
        Button findIDButton = findViewById(R.id.findIDButton);
        Button resetPWButton = findViewById(R.id.resetPWButton);

        //Intent 변수에 할당하기
        Intent changePWIntent = new Intent(getApplicationContext(),ChangePW.class);

        //서버 할당
        userInfoService = ServiceUtils.getUserInfoService();

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            if(isFinishing()){
                overridePendingTransition(R.anim.none,R.anim.to_bottom_exit);
            }
        });

        //아이디 찾기 버튼
        findIDButton.setOnClickListener(v -> {
            String userName = findIDName.getText().toString();
            String birth = findIDBirth.getText().toString();
            FindIDInfo findIDInfo = new FindIDInfo(userName,birth);
            userInfoService.findID(findIDInfo).enqueue(new Callback<FindIDInfoResponse>() {
                @Override
                public void onResponse(@NotNull Call<FindIDInfoResponse> call, @NotNull Response<FindIDInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String userID = response.body().getUserID();
                        AlertDialog.Builder findIDBuilder = new AlertDialog.Builder(FindIDAndPW.this)
                                .setMessage("아이디는 "+userID+" 입니다.")
                                .setPositiveButton("확인", (dialogInterface, i) -> {
                                });
                        AlertDialog logoutDialog = findIDBuilder.create();
                        logoutDialog.show();
                    } else {
                        Toast.makeText(FindIDAndPW.this, "입력한 정보가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FindIDInfoResponse> call, @NotNull Throwable t) {
                    Toast.makeText(FindIDAndPW.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        //비밀번호 초기화 버튼
        resetPWButton.setOnClickListener(v -> {
            String userID = resetPWID.getText().toString();
            String userName = resetPWName.getText().toString();
            String birth = resetPWBirth.getText().toString();

            ChangePWInfo changePWInfo = new ChangePWInfo(userID,userName,birth);

            userInfoService.checkChangePW(changePWInfo).enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(@NotNull Call<UserInfoResponse> call, @NotNull Response<UserInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        changePWIntent.putExtra("changePWUser",changePWInfo);
                        startActivity(changePWIntent);
                    } else {
                        Toast.makeText(FindIDAndPW.this, "입력한 정보가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<UserInfoResponse> call, @NotNull Throwable t) {
                    Toast.makeText(FindIDAndPW.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FindID", "Error: ", t);
                }
            });
        });
    }
}