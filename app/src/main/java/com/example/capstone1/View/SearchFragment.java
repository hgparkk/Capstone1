package com.example.capstone1.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.R;
import com.example.capstone1.RestInfo.RestInfo;
import com.example.capstone1.RestInfo.RestInfoListResponse;
import com.example.capstone1.RestInfo.RestInfoService;
import com.example.capstone1.RestInfo.SearchRestInfo;
import com.example.capstone1.ServiceUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements FilterSet.OnDismissListener {

    //보낼 파라미터
    String params = null;
    String cat = null;

    //서버
    RestInfoService restInfoService;

    //네트워크 상태 확인
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    //검색결과 어댑터
    SearchListAdapter searchListAdapter;

    //검색결과
    List<RestInfo> searchedList = new ArrayList<>();

    //TextView 변수선언
    TextView emptyTextView;

    //RecyclerView 변수선언
    RecyclerView searchListView;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    searchRestInfo(params,cat);
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        //TextView 변수에 할당하기
        emptyTextView = view.findViewById(R.id.empty);

        //SearchView 변수에 할당하기
        SearchView searchView = view.findViewById(R.id.search);

        //Button 변수에 할당하기
        Button filterButton = view.findViewById(R.id.filterButton);

        //RecyclerView 변수에 할당하기
        searchListView = view.findViewById(R.id.searchListView);

        //SearchListAdapter 변수에 할당하기
        searchListAdapter = new SearchListAdapter(searchedList,item -> {
            Intent intent = new Intent(getContext(), RestInfoView.class);
            intent.putExtra("restInfo",new Gson().toJson(item));
            activityResultLauncher.launch(intent);
        });
        searchListView.setAdapter(searchListAdapter);
        searchListView.setLayoutManager(new LinearLayoutManager(getContext()));

        //서버할당
        restInfoService = ServiceUtils.getRestInfoService();

        //앱 시작시 검색결과 불러오기
        searchRestInfo(params, cat);

        //필터 정보 가져오기
        FilterSet filterset = new FilterSet();
        filterset.setOnDismissListener(this);

        if (cat != null) {
            filterset.setInitialSelectedItem(cat);
        }

        //서치뷰
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                params = query.trim();
                searchRestInfo(params, cat);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //필터 버튼
        filterButton.setOnClickListener(v -> filterset.show(getChildFragmentManager(), filterset.getTag()));

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
                    searchRestInfo(params,cat);
                }

                //연결이 끊어졌을때
                public void onLost(@NonNull Network network){
                    super.onLost(network);
                    searchRestInfo(params,cat);
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
            searchListView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    //현재 상태에 따른 메시지로 텍스트뷰 변환
    private void showMessage(String message) {
        searchListView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText(message);
    }

    //조건에 부합하는 가게 검색
    private void searchRestInfo(String params, String cat) {
        SearchRestInfo searchRestInfo = new SearchRestInfo(params, cat);
        restInfoService.search(searchRestInfo).enqueue(new Callback<RestInfoListResponse>() {
            @Override
            public void onResponse(@NotNull Call<RestInfoListResponse> call, @NotNull Response<RestInfoListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RestInfo> restInfoList = response.body().getRestInfoList() != null ? response.body().getRestInfoList() : new ArrayList<>();
                    searchedList.clear();
                    searchedList.addAll(restInfoList);
                    checkIfEmpty();
                    searchListAdapter.updateDataList(searchedList);
                } else {
                    Toast.makeText(getActivity(), "검색에 실패하였습니다. " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestInfoListResponse> call, @NotNull Throwable t) {
                if (!isNetworkAvailable()) {
                    showMessage("인터넷 상태가 불안정 합니다");
                } else {
                    showMessage("데이터를 불러오는 데 실패했습니다.");
                }
            }
        });
    }

    //필터설정 창이 꺼졌을 때 실행되는 함수
    @Override
    public void onDismiss(String seleteditem) {
        this.cat = seleteditem;
        searchRestInfo(params,cat);
    }
}
