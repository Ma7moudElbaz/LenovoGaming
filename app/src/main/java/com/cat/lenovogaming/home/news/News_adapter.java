package com.cat.lenovogaming.home.news;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.news_all.NewsDetails;

import java.util.ArrayList;
import java.util.List;

public class News_adapter extends RecyclerView.Adapter<News_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private final List<News_item> items;

    private final Context mContext;

    public News_adapter(Context context, ArrayList<News_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public News_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        News_adapter.ViewHolder holder = new News_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull News_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(items.get(position).getTitle());
        Glide.with(mContext).load(items.get(position).getImgUrl()).placeholder(R.drawable.image_loading).into(holder.image);



        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, NewsDetails.class);
                i.putExtra("id", String.valueOf(items.get(position).getId()));
                i.putExtra("title", items.get(position).getTitle());
                i.putExtra("content", items.get(position).getContent());
                i.putExtra("image", items.get(position).getImgUrl());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        ImageView image;
        LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.img);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}