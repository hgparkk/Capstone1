package com.example.capstone1.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.capstone1.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterSet extends BottomSheetDialogFragment {

    public FilterSet(){}

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),android.R.layout.simple_list_item_single_choice, items);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            CheckedTextView checkedTextView = view1.findViewById(android.R.id.text1);
            checkedTextView.setChecked(true);
        });

        //필터 설정 버튼
        filterSetButton.setOnClickListener(v -> dismiss());

        return view;
    }
}