package com.example.capstone1.View;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.R;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.CompareIDInfo;
import com.example.capstone1.UserInfo.UserInfo;
import com.example.capstone1.UserInfo.UserInfoResponse;
import com.example.capstone1.UserInfo.UserInfoService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sign extends AppCompatActivity {

    //서버
    UserInfoService userInfoService;

    //아이디 중복확인 여부
    boolean idCompare = false;

    //비밀번호 체크 여부
    boolean pwCheck = false;

    //네트워크 연결 여부
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
            return false;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        //등장 애니메이션
        overridePendingTransition(R.anim.from_bottom_enter, R.anim.none);

        //내비게이션 바 뒤로가기 버튼
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                if (isFinishing()) {
                    overridePendingTransition(R.anim.none, R.anim.to_bottom_exit);
                }
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        //TextView 변수에 할당하기
        TextView idCompareResult = findViewById(R.id.idCompareResult);
        TextView pwCheckResult = findViewById(R.id.pwCheckResult);

        //Button 변수에 할당하기
        Button backButton = findViewById(R.id.signBackButton);
        Button idCompareButton = findViewById(R.id.compareIDButton);
        Button pwCheckButton = findViewById(R.id.pwCheckButton);
        Button signButton = findViewById(R.id.signButton);

        //EditTest 변수에 할당하기
        EditText signID = findViewById(R.id.signID);
        EditText signPW = findViewById(R.id.signPW);
        EditText signPW2 = findViewById(R.id.signPW2);
        EditText signName = findViewById(R.id.signName);
        EditText signBirth = findViewById(R.id.signBirth);

        //서버 할당
        userInfoService = ServiceUtils.getUserInfoService();

        //뒤로가기 버튼
        backButton.setOnClickListener(v -> {
            finish();
            if (isFinishing()) {
                overridePendingTransition(R.anim.none, R.anim.to_bottom_exit);
            }
        });

        //아이디 입력시
        signID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idCompareResult.setText("");
                idCompare = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //아이디 중복확인
        idCompareButton.setOnClickListener(v -> {
            String userID = signID.getText().toString();
            CompareIDInfo compareIDInfo = new CompareIDInfo(userID);
            if (userID.length() > 4) {
                userInfoService.compareID(compareIDInfo).enqueue(new Callback<UserInfoResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<UserInfoResponse> call, @NotNull Response<UserInfoResponse> response) {
                        if (response.isSuccessful()) {
                            idCompareResult.setTextColor(Color.parseColor("#00FF7F"));
                            idCompareResult.setText(R.string.idDifferent);
                            idCompare = true;
                        } else {
                            idCompareResult.setTextColor(Color.parseColor("#FF0000"));
                            idCompareResult.setText(R.string.idSame);
                            idCompare = false;
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<UserInfoResponse> call, @NotNull Throwable t) {
                        Toast.makeText(Sign.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Sign", "Error: ", t);
                    }
                });
            } else {
                Toast.makeText(Sign.this, "아이디가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //비밀번호 입력시
        signPW.addTextChangedListener(new TextWatcher() {
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
            if (signPW.getText().toString().length() > 7) {
                if (signPW.getText().toString().equals(signPW2.getText().toString())) {
                    pwCheckResult.setTextColor(Color.parseColor("#00FF7F"));
                    pwCheckResult.setText(R.string.pwSame);
                    pwCheck = true;
                } else {
                    pwCheckResult.setTextColor(Color.parseColor("#FF0000"));
                    pwCheckResult.setText(R.string.pwDifferent);
                }
            } else {
                Toast.makeText(Sign.this, "비밀번호가 너무 짧습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //회원가입 버튼
        signButton.setOnClickListener(v -> {

            if (!idCompare) {
                Toast.makeText(Sign.this, "생년월일을 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pwCheck) {
                Toast.makeText(Sign.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userInfoID = signID.getText().toString().trim();
            String userInfoPW = signPW.getText().toString().trim();
            String userInfoName = signName.getText().toString().trim();
            String userInfoBirth = signBirth.getText().toString().trim();

            if (userInfoName.isEmpty()) {
                Toast.makeText(Sign.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if (userInfoBirth.length() != 8) {
                Toast.makeText(Sign.this, "생년월일을 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            //객체 생성
            UserInfo userInfo = new UserInfo(userInfoID, userInfoPW, userInfoName, userInfoBirth);

            userInfoService.register(userInfo).enqueue(new Callback<UserInfoResponse>() {
                public void onResponse(@NonNull Call<UserInfoResponse> call, @NonNull Response<UserInfoResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Sign.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Sign.this, "회원가입에 실패하였습니다" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserInfoResponse> call, @NonNull Throwable t) {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(Sign.this, "인터넷 상태가 불안정합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Sign.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}