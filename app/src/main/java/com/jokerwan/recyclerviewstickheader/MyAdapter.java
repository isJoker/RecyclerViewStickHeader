package com.jokerwan.recyclerviewstickheader;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by JokerWan on 2019/2/21.
 * Function:
 */

public class MyAdapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == StickyHeaderLayout.TYPE_STICKY_LAYOUT) {
            return new TopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, parent, false));
        } else {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == StickyHeaderLayout.TYPE_STICKY_LAYOUT) {
            TopViewHolder vhTop = (TopViewHolder) holder;
        } else {
            MyViewHolder vh = (MyViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return 40;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 5) {
            return StickyHeaderLayout.TYPE_STICKY_LAYOUT;
        } else {
            return super.getItemViewType(position);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(textView.getContext(), "item点击", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static class TopViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(textView.getContext(), "头部点击", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
