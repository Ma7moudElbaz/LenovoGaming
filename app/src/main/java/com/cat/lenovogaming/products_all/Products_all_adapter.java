package com.cat.lenovogaming.products_all;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.WebBrowser;
import com.cat.lenovogaming.home.products.Products_item;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Products_all_adapter extends RecyclerView.Adapter<Products_all_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private final List<Products_item> items;

    private final Context mContext;

    public Products_all_adapter(Context context, ArrayList<Products_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Products_all_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products_all, parent, false);
        Products_all_adapter.ViewHolder holder = new Products_all_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Products_all_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(items.get(position).getTitle());
        holder.content.setText(items.get(position).getContent());
        Glide.with(mContext).load(items.get(position).getImgUrl()).placeholder(R.drawable.image_loading).into(holder.image);



        holder.learnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items.get(position).getIsAccessory() == 0){
                    Intent i = new Intent(mContext, WebBrowser.class);
                    i.putExtra("url","https://legion-zone.lenovomeaevents.com/products/launch/mobile");
                    mContext.startActivity(i);
                }
                else {
                    Intent i = new Intent(mContext, ProductDetails.class);
                    i.putExtra("id", String.valueOf(items.get(position).getId()));
                    i.putExtra("title", items.get(position).getTitle());
                    i.putExtra("content", items.get(position).getContent());
                    i.putExtra("image", items.get(position).getImgUrl());
                    mContext.startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView title,content,learnMore;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            learnMore = itemView.findViewById(R.id.learn_more);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.img);
        }
    }
}