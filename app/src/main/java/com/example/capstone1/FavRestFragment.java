package com.example.capstone1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FavRestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fav_rest_fragment,container,false);

        //ListView 변수에 할당하기
        ListView favRestListView = view.findViewById(R.id.favRestListView);

        //임시 ListView
        ArrayList<String> dataSample = new ArrayList<>();

        dataSample.add("Text1");
        dataSample.add("Text2");
        dataSample.add("Text3");

        FavRestListAdapter favRestListAdapter = new FavRestListAdapter(getActivity(),dataSample);

        favRestListView.setAdapter(favRestListAdapter);
        return view;
    }
}
