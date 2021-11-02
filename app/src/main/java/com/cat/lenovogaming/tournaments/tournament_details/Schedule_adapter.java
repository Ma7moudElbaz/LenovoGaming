package com.cat.lenovogaming.tournaments.tournament_details;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Schedule_adapter extends RecyclerView.Adapter<Schedule_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private final List<Schedule_item> items;

    private final Context mContext;

    public Schedule_adapter(Context context, ArrayList<Schedule_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Schedule_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        Schedule_adapter.ViewHolder holder = new Schedule_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Schedule_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext).load(items.get(position).getImageUrl()).placeholder(R.drawable.image_loading).into(holder.image);



        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(mContext, TournamentDetails.class);
//                i.putExtra("id",items.get(position).getId());
//                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            image = itemView.findViewById(R.id.img);
        }
    }
}