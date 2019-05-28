package com.example.emre.a14011009_digitalnotepadd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
    private List<String> titles = new ArrayList<String>();
    private LayoutInflater mInflater;
    private Context mContext;
    private MainActivity a;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView titleTV;
        public LinearLayout parentLayout;

        public MyViewHolder(View v) {
            super(v);
            titleTV = v.findViewById(R.id.titleTV);
            parentLayout = v.findViewById(R.id.parent_layout);
            view = v;
        }
    }

    public MyAdapter(Context context, List<String> myDataset, MainActivity activity) {
        mInflater = LayoutInflater.from(context);
        titles = myDataset;
        mContext = context;
        a = activity;
    }


    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = (View) mInflater
                .inflate(R.layout.notepad, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.titleTV.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


}