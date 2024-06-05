package com.example.capstone1.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone1.R;
import com.example.capstone1.RestInfo.RestInfo;

import java.util.List;

public class SearchListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<RestInfo> data;

    public SearchListAdapter(Context context, List<RestInfo> data) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount()
    {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.list_view, null);

        // TextView 변수에 할당하기
        TextView titleTextView = view.findViewById(R.id.title);
        TextView catTextView = view.findViewById(R.id.cat);
        TextView addressTextView = view.findViewById(R.id.address);

        RestInfo restInfo = data.get(position);
        titleTextView.setText(restInfo.getRestName());
        catTextView.setText(restInfo.getCat());
        addressTextView.setText(restInfo.getAddress());

        view.setOnClickListener(v -> Toast.makeText(context, "가게 이름은 " + restInfo.getRestName() + "입니다.", Toast.LENGTH_SHORT).show());

        return view;
    }
}
