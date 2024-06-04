package com.example.capstone1.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.ApplicationManager;
import com.example.capstone1.R;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.ChangePWInfo;
import com.example.capstone1.UserInfo.UserInfo;
import com.example.capstone1.UserInfo.UserInfoResponse;
import com.example.capstone1.UserInfo.UserInfoService;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInfo extends AppCompatActivity {

    //로그인 여부와 로그인 정보 저장
    UserInfo loginedUser;
    String token;
    String loginedName;

    //서버
    UserInfoService userInfoService;

    //버튼 활성화, 비활성화 함수
    private void setButtonEnabled(Button button, boolean state){
        if(state){
            button.setVisibility(View.VISIBLE);
            button.setEnabled(true);
        }
        else{
            button.setVisibility(View.GONE);
            button.setEnabled(false);
        }
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
    private void getLoginInfo(Button loginInfoButton, Button changePWButton, Button resingButton, Button logoutButton){
        loginedUser = getUserInfo();
        token = getToken();
        if (loginedUser != null)
        {
            loginedName = loginedUser.getUserName() + "님!";
            loginInfoButton.setEnabled(false);
            loginInfoButton.setText(loginedName);
            loginInfoButton.setTextColor(Color.parseColor("#000000"));
            setButtonEnabled(changePWButton, true);
            setButtonEnabled(resingButton, true);
            setButtonEnabled(logoutButton, true);
        }
        else
        {
            loginInfoButton.setEnabled(true);
            loginInfoButton.setText(R.string.needLogin);
            loginInfoButton.setTextColor(Color.parseColor("#A0A0A0"));
            setButtonEnabled(changePWButton, false);
            setButtonEnabled(resingButton, false);
            setButtonEnabled(logoutButton, false);
        }
    }

    //로그아웃하기
    private void logout(){
        SharedPreferences sharedPreferences = ApplicationManager.getAppContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userInfo");
        editor.remove("token");
        editor.apply();
    }

    //회원탈퇴하기
    private void resignUserInfo() {
        UserInfo userInfo = getUserInfo();
        if (userInfo != null) {
            userInfoService.resignUser(userInfo).enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(@NotNull Call<UserInfoResponse> call, @NotNull Response<UserInfoResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MyInfo.this, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyInfo.this, "회원 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(@NotNull Call<UserInfoResponse> call, @NotNull Throwable t) {
                    Toast.makeText(MyInfo.this, "에러: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info);

        // 등장 애니메이션
        overridePendingTransition(R.anim.from_right_enter, R.anim.none);

        // 내비게이션 바 뒤로가기 버튼
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.none, R.anim.to_right_exit);
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        // 버튼 변수에 할당하기
        Button backButton = findViewById(R.id.backButton);
        Button loginInfoButton = findViewById(R.id.loginInfoButton);
        Button changePWButton = findViewById(R.id.changePWButton);
        Button resignButton = findViewById(R.id.resignButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        // Intent 변수에 할당하기
        Intent loginMainIntent = new Intent(getApplicationContext(), LoginMain.class);
        Intent changePWIntent = new Intent(getApplicationContext(), ChangePW.class);

        //서버할당
        userInfoService = ServiceUtils.getUserInfoService();

        // 뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.none, R.anim.to_right_exit);
        });

        // 로그인 정보 불러오기
        getLoginInfo(loginInfoButton,changePWButton,resignButton,logoutButton);

        //로그인 후 다시 돌아왔을 때 로그인 정보 불러오기
        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        getLoginInfo(loginInfoButton,changePWButton,resignButton,logoutButton);
                    }
                });

        //로그인 버튼
        loginInfoButton.setOnClickListener(v -> startActivityResult.launch(loginMainIntent));

        //비밀번호 변경 버튼
        changePWButton.setOnClickListener(v -> {
            ChangePWInfo changePWInfo = new ChangePWInfo(loginedUser.getUserID(),loginedUser.getUserName(),loginedUser.getBirth());
            changePWIntent.putExtra("changePWUser",changePWInfo);
            startActivity(changePWIntent);
        });

        //회원 탈퇴 버튼
        resignButton.setOnClickListener(v -> {
            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(MyInfo.this)
                    .setMessage("정말로 탈퇴하시겠습니까?")
                    .setPositiveButton("예", (dialogInterface, i) -> new AlertDialog.Builder(MyInfo.this)
                            .setMessage("탈퇴 후 복구가 불가능합니다. 계속하시겠습니까?")
                            .setPositiveButton("아니오", (dialog, which) -> {
                            })
                            .setNegativeButton("예", (dialog, which) -> {
                                resignUserInfo();
                                logout();
                                getLoginInfo(loginInfoButton,changePWButton,resignButton,logoutButton);
                            })
                            .show())
                    .setNegativeButton("아니오", (dialogInterface, i) -> {
                    });
            AlertDialog deleteDialog = deleteBuilder.create();
            deleteDialog.show();
        });

        //로그아웃 버튼
        logoutButton.setOnClickListener(v -> {
            AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(MyInfo.this)
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("예", (dialogInterface, i) -> {
                        logout();
                        getLoginInfo(loginInfoButton,changePWButton,resignButton,logoutButton);
                    })
                    .setNegativeButton("아니오", (dialogInterface, i) -> {
                    });
            AlertDialog logoutDialog = logoutBuilder.create();
            logoutDialog.show();
        });
    }
}