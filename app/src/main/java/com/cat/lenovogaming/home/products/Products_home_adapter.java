package com.cat.lenovogaming.home.products;

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
import com.cat.lenovogaming.WebBrowser;
import com.cat.lenovogaming.news_all.NewsDetails;
import com.cat.lenovogaming.products_all.ProductDetails;

import java.util.ArrayList;
import java.util.List;

public class Products_home_adapter extends RecyclerView.Adapter<Products_home_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Products_item> items;

    private Context mContext;

    public Products_home_adapter(Context context, ArrayList<Products_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Products_home_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products_home, parent, false);
        Products_home_adapter.ViewHolder holder = new Products_home_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Products_home_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(items.get(position).getTitle());
        Glide.with(mContext).load(items.get(position).getImgUrl()).placeholder(R.drawable.image_loading).into(holder.image);



        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
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