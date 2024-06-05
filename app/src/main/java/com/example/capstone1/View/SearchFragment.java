package com.example.capstone1.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.capstone1.R;
import com.example.capstone1.RestInfo.RestInfo;
import com.example.capstone1.RestInfo.RestInfoResponse;
import com.example.capstone1.RestInfo.RestInfoService;
import com.example.capstone1.RestInfo.SearchRestInfo;
import com.example.capstone1.ServiceUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    //보낼 파라미터
    String cat = null;

    //서버
    RestInfoService restInfoService;

    SearchListAdapter searchListAdapter;

    //검색결과
    List<RestInfo> searchedList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.search_fragment,container,false);

        //SearchView 변수에 할당하기
        SearchView searchView = view.findViewById(R.id.search);

        //Button 변수에 할당하기
        Button filterButton = view.findViewById(R.id.filterButton);

        //ListView 변수에 할당하기
        ListView searchListView = view.findViewById(R.id.searchListView);

        //SearchListAdapter 변수에 할당하기
        searchListAdapter = new SearchListAdapter(getContext(),searchedList);
        searchListView.setAdapter(searchListAdapter);

        //서버할당
         restInfoService = ServiceUtils.getRestInfoService();

        //앱 시작시 검색결과 불러오기

        //서치뷰
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRestInfo(query,cat);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //필터 버튼
        filterButton.setOnClickListener(v ->{
                FilterSet filterset = new FilterSet();
                filterset.show(getChildFragmentManager(), filterset.getTag());
        });

        return view;
    }

    private void searchRestInfo(String params, String cat){
        SearchRestInfo searchRestInfo = new SearchRestInfo(params,cat);
        restInfoService.search(searchRestInfo).enqueue(new Callback<RestInfoResponse>() {
            @Override
            public void onResponse(@NotNull Call<RestInfoResponse> call, @NotNull Response<RestInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RestInfo> restInfoList = response.body().getRestInfoList() != null ? response.body().getRestInfoList() : new ArrayList<>();
                    updateSearchResults(restInfoList);
                } else {
                    Toast.makeText(getActivity(), "검색에 실패하였습니다. " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestInfoResponse> call, @NotNull Throwable t) {
                Toast.makeText(getActivity(), "통신에 실패하였습니다 " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSearchResults(List<RestInfo> restInfoList) {
        searchedList.clear();
        searchedList.addAll(restInfoList);
        searchListAdapter.notifyDataSetChanged();
    }
}
