package com.example.capstone1.View;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone1.R;
import com.example.capstone1.Review.Review;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AllReviewAdapter extends RecyclerView.Adapter<AllReviewAdapter.ViewHolder>{

    private List<Review> dataList;

    public AllReviewAdapter(List<Review> dataList) {
        this.dataList = dataList;
    }

    @NotNull
    @Override
    public AllReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllReviewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false));
    }

    //항목들 정보에 맞게 세팅하기
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Review data = dataList.get(position);
        holder.idTextView.setText(data.getUserID());

        int repu = data.getRepu();
        int taste = repu/1000;
        int price = (repu%1000) /100;
        int service = (repu%100) /10;
        int cleaness = repu%10;

        float avgRepu = (taste+price+service+cleaness)/4.0f;

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0");
        String formattedValue = decimalFormat.format(avgRepu);
        holder.repuTextView.setText(formattedValue);
        holder.reviewTextView.setText(data.getUsersReview());
        LocalDateTime localDateTime = data.getCreateAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        holder.createAtTextView.setText(localDateTime.format(formatter));
    }

    //데이터가 바뀌었음을 알리기
    @SuppressLint("NotifyDataSetChanged")
    public void updateDataList(List<Review> reviewList){
        this.dataList = reviewList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //TextView 변수 선언
        TextView idTextView;
        TextView repuTextView;
        TextView reviewTextView;
        TextView createAtTextView;

        public ViewHolder(@NotNull View view){
            super(view);

            //TextView 변수에 할당하기
            idTextView = view.findViewById(R.id.id);
            repuTextView = view.findViewById(R.id.repu);
            reviewTextView = view.findViewById(R.id.review);
            createAtTextView = view.findViewById(R.id.create_at);
        }
    }
}
