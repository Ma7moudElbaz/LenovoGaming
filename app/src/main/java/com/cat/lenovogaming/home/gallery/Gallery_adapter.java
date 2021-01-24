package com.cat.lenovogaming.home.gallery;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.WebBrowser;

import java.util.ArrayList;
import java.util.List;

public class Gallery_adapter extends RecyclerView.Adapter<Gallery_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Gallery_item> items;

    private Context mContext;

    public Gallery_adapter(Context context, ArrayList<Gallery_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Gallery_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        Gallery_adapter.ViewHolder holder = new Gallery_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Gallery_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

//        holder.title.setText(items.get(position).getTitle());
        Glide.with(mContext).load(items.get(position).getImgUrl()).placeholder(R.drawable.image_loading).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, WebBrowser.class);
                i.putExtra("url",items.get(position).getExternalUrl());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


//        TextView title;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.img);
        }
    }
}