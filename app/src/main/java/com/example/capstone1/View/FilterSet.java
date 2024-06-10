package com.example.capstone1.View;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.capstone1.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;

import lombok.Setter;

public class FilterSet extends BottomSheetDialogFragment {

    @Override
    public void onStart(){
        super.onStart();
        if(getDialog()!=null){
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if(bottomSheet!=null){
                BottomSheetBehavior.from(bottomSheet).setPeekHeight(getResources().getDisplayMetrics().heightPixels);
            }
        }
    }

    public FilterSet() {
    }

    public interface OnDismissListener {
        void onDismiss(String data);
    }

    @Setter
    private OnDismissListener onDismissListener;
    private String selectedItem = null;
    @Setter
    private String initialSelectedItem;

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(selectedItem);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_set, container, false);

        //ListView 변수에 할당하기
        ListView listView = view.findViewById(R.id.filterSetListView);

        //Button 변수에 할당하기
        Button filterSetButton = view.findViewById(R.id.setFilterButton);

        //카테고리가 들어갈 items 문자열 변수 할당하기
        String[] items = getResources().getStringArray(R.array.cat);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_single_choice, items);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (initialSelectedItem != null) {
            int initialPosition = Arrays.asList(items).indexOf(initialSelectedItem);
            if (initialPosition >= 0) {
                listView.setItemChecked(initialPosition, true);
                selectedItem = initialSelectedItem;
            }
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                setCancelable(firstVisibleItem == 0 && visibleItemCount > 0);
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            CheckedTextView checkedTextView = view1.findViewById(android.R.id.text1);
            checkedTextView.setChecked(true);
            selectedItem = items[position];
        });

        //필터 설정 버튼
        filterSetButton.setOnClickListener(v -> dismiss());

        return view;
    }
}