package com.cat.lenovogaming.tournaments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.tournaments.tournament_details.TournamentDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Tournaments_adapter extends RecyclerView.Adapter<Tournaments_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private final List<Tournaments_item> items;

    private final Context mContext;

    public Tournaments_adapter(Context context, ArrayList<Tournaments_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Tournaments_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tournaments, parent, false);
        Tournaments_adapter.ViewHolder holder = new Tournaments_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Tournaments_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(items.get(position).getTitle());
        holder.content.setText(items.get(position).getContent());
        Glide.with(mContext).load(items.get(position).getImgUrl()).placeholder(R.drawable.image_loading).into(holder.image);



        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, TournamentDetails.class);
                i.putExtra("id",items.get(position).getId());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView title,content;
        ImageView image;
        LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.img);
        }
    }
}