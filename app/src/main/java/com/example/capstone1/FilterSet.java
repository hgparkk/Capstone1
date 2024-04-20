package com.example.capstone1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterSet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.filter_set,container,false);

        //Button 변수에 할당하기
        Button filterSetButton = view.findViewById(R.id.setFilterButton);

        //필터 설정 버튼
        filterSetButton.setOnClickListener(v -> dismiss());

        return view;
    }
}