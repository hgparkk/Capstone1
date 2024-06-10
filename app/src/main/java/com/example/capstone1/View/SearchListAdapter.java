package com.example.capstone1.View;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.R;
import com.example.capstone1.RestInfo.RestInfo;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private List<RestInfo> dataList;
    private final OnItemClickListener listener;

    public SearchListAdapter(List<RestInfo> dataList, OnItemClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_list_view, parent, false));
    }

    //항목들 정보에 맞게 세팅하기
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        RestInfo data = dataList.get(position);
        holder.titleTextView.setText(data.getRestName());
        String visibleCat = "# "+data.getCat();
        holder.catTextView.setText(visibleCat);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
        String formattedValue = decimalFormat.format(data.getAvgRepu());
        holder.repuTextView.setText(formattedValue);

        //항목이 클릭되었을 때
        holder.body.setOnClickListener(v -> listener.onItemClick(data));
    }

    public interface OnItemClickListener {
        void onItemClick(RestInfo item);
    }

    //데이터가 바뀌었음을 알리기
    @SuppressLint("NotifyDataSetChanged")
    public void updateDataList(List<RestInfo> restInfoList){
        this.dataList = restInfoList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //TextView 변수 선언
        TextView titleTextView;
        TextView catTextView;
        TextView repuTextView;
        LinearLayout body;

        public ViewHolder(@NotNull View view){
            super(view);

            //TextView 변수에 할당하기
            titleTextView = view.findViewById(R.id.title);
            catTextView = view.findViewById(R.id.cat);
            repuTextView = view.findViewById(R.id.repu);
            body = view.findViewById(R.id.body);
        }
    }
}
