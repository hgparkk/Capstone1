package com.example.capstone1.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.ApplicationManager;
import com.example.capstone1.FavRest.ClassForResponse;
import com.example.capstone1.FavRest.FavRestListResponse;
import com.example.capstone1.FavRest.FavRestService;
import com.example.capstone1.FavRest.SearchFavRestInfo;
import com.example.capstone1.R;
import com.example.capstone1.ServiceUtils;
import com.example.capstone1.UserInfo.UserInfo;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavRestFragment extends Fragment {

    //로그인 여부와 로그인 정보 저장
    UserInfo loginedUser;
    String token;

    //보낼 파라미터
    String params = null;

    //서버
    FavRestService favRestService;

    //네트워크 상태 확인
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    //검색결과 어댑터
    FavRestListAdapter favRestListAdapter;

    //검색결과
    List<ClassForResponse> searchedList = new ArrayList<>();

    //TextView 변수선언
    TextView emptyTextView;

    //RecyclerView 변수선언
    RecyclerView favRestListView;

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

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    searchFavRestInfo(params,loginedUser);
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fav_rest_fragment, container, false);

        //TextView 변수에 할당하기
        emptyTextView = view.findViewById(R.id.empty);

        //SearchView 변수에 할당하기
        SearchView searchView = view.findViewById(R.id.search);

        //RecyclerView 변수에 할당하기
        favRestListView = view.findViewById(R.id.favRestListView);

        //SearchListAdapter 변수에 할당하기
        favRestListAdapter = new FavRestListAdapter(searchedList,item -> {
            Intent intent = new Intent(getContext(), RestInfoView.class);
            intent.putExtra("restInfo",new Gson().toJson(item));
            activityResultLauncher.launch(intent);
        });
        favRestListView.setAdapter(favRestListAdapter);
        favRestListView.setLayoutManager(new LinearLayoutManager(getContext()));

        //서버할당
        favRestService = ServiceUtils.getFavRestService();

        getLoginInfo();

        //앱 시작시 검색결과 불러오기
        searchFavRestInfo(params,loginedUser);

        //서치뷰
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                params = query.trim();
                searchFavRestInfo(params,loginedUser);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        registerNetworkCallback();

        return view;
    }

    //네트워크가 연결 상태에 따른 함수
    public void onDestroyView(){
        super.onDestroyView();
        if(connectivityManager != null && networkCallback != null){
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    private void registerNetworkCallback(){
        if(connectivityManager != null){
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();

            networkCallback = new ConnectivityManager.NetworkCallback(){
                //연결이 되었을때
                @Override
                public void onAvailable(@NonNull Network network){
                    super.onAvailable(network);
                    searchFavRestInfo(params,loginedUser);
                }

                //연결이 끊어졌을때
                public void onLost(@NonNull Network network){
                    super.onLost(network);
                    searchFavRestInfo(params,loginedUser);
                }
            };

            connectivityManager.registerNetworkCallback(networkRequest,networkCallback);
        }
    }

    //네트워크 연결 확인
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    //데이터가 비어있는지 확인하기
    private void checkIfEmpty() {
        if (searchedList.isEmpty()) {
            showMessage("검색 결과가 없습니다");
        } else {
            favRestListView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    //현재 상태에 따른 메시지로 텍스트뷰 변환
    private void showMessage(String message) {
        favRestListView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText(message);
    }

    //조건에 부합하는 가게 검색
    private void searchFavRestInfo(String params, UserInfo logined) {
        if(logined != null) {
            SearchFavRestInfo searchFavRestInfo = new SearchFavRestInfo(params, logined.getUserID());
            favRestService.searchFavRest(searchFavRestInfo).enqueue(new Callback<FavRestListResponse>() {
                @Override
                public void onResponse(@NotNull Call<FavRestListResponse> call, @NotNull Response<FavRestListResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ClassForResponse> favRestInfoList = response.body().getFavRestList() != null ? response.body().getFavRestList() : new ArrayList<>();
                        searchedList.clear();
                        searchedList.addAll(favRestInfoList);
                        checkIfEmpty();
                        favRestListAdapter.updateDataList(searchedList);
                    } else {
                        Toast.makeText(getActivity(), "검색에 실패하였습니다. " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FavRestListResponse> call, @NotNull Throwable t) {
                    if (!isNetworkAvailable()) {
                        showMessage("인터넷 상태가 불안정 합니다");
                    } else {
                        showMessage("데이터를 불러오는 데 실패했습니다.");
                    }
                }
            });
        }
        else{
            showMessage("로그인 후 사용해 주세요.");
        }
    }
}