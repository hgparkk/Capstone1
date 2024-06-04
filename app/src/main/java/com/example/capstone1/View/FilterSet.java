package com.example.capstone1.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.capstone1.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterSet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.filter_set,container,false);

        //ListView 변수에 할당하기
        ListView listView = view.findViewById(R.id.filterSetListView);

        //Button 변수에 할당하기
        Button filterSetButton = view.findViewById(R.id.setFilterButton);

        //카테고리가 들어갈 items 문자열 변수 할당하기
        String[] items = getResources().getStringArray(R.array.cat);

        //필터 설정 버튼
        filterSetButton.setOnClickListener(v -> dismiss());

        return view;
    }
}