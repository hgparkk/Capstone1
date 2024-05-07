package com.example.capstone1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavRestListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> data;

    public FavRestListAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = layoutInflater.inflate(R.layout.list_view,null);
        TextView textView = view.findViewById(R.id.title);
        textView.setText(data.get(position));

        View bodyView = view.findViewById(R.id.body);

        bodyView.setOnClickListener(v ->Toast.makeText(context, "click list body", Toast.LENGTH_SHORT).show());

        return view;
    }
}