package com.example.capstone1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.search_fragment,container,false);
        //Button 변수에 할당하기
        Button filterButton = view.findViewById(R.id.filterButton);

        //필터 버튼
        filterButton.setOnClickListener(v ->{
                FilterSet filterset = new FilterSet();
                filterset.show(getChildFragmentManager(), filterset.getTag());
        });
        return view;
    }
}
