package com.example.capstone1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.search_fragment,container,false);

        //Button 변수에 할당하기
        Button filterButton = view.findViewById(R.id.filterButton);

        //ListView 변수에 할당하기
        ListView searchListView = view.findViewById(R.id.searchListView);

        //필터 버튼
        filterButton.setOnClickListener(v ->{
                FilterSet filterset = new FilterSet();
                filterset.show(getChildFragmentManager(), filterset.getTag());
        });

        //임시 ListView
        ArrayList<String> dataSample = new ArrayList<>();

        dataSample.add("Text1");
        dataSample.add("Text2");
        dataSample.add("Text3");

        SearchListAdapter searchListAdapter = new SearchListAdapter(getActivity(),dataSample);

        searchListView.setAdapter(searchListAdapter);
        return view;
    }
}
