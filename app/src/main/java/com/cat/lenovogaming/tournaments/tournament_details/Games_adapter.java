package com.cat.lenovogaming.tournaments.tournament_details;

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
import com.cat.lenovogaming.tournaments.Tournaments_item;
import com.cat.lenovogaming.tournaments.tournament_game_details.TournamentGameDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Games_adapter extends RecyclerView.Adapter<Games_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Games_item> items;

    private Context mContext;

    public Games_adapter(Context context, ArrayList<Games_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Games_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_games, parent, false);
        Games_adapter.ViewHolder holder = new Games_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Games_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(items.get(position).getTitle());
        Glide.with(mContext).load(items.get(position).getThumbURL()).placeholder(R.drawable.image_loading).into(holder.image);



        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, TournamentGameDetails.class);
                i.putExtra("gameId",items.get(position).getGameId());
                i.putExtra("tournamentId",items.get(position).getTournamentId());
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
            parent_layout = itemView.findViewById(R.id.parent_layout);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.img);
        }
    }
}