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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.WebBrowser;
import com.cat.lenovogaming.tournaments.Tournaments_item;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Activities_adapter extends RecyclerView.Adapter<Activities_adapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Activities_item> items;

    private Context mContext;
    int tournamentId;

    public Activities_adapter(Context context, ArrayList<Activities_item> items, int tournamentId) {

        this.mContext = context;
        this.items = items;
        this.tournamentId = tournamentId;
    }

    @NonNull
    @Override
    public Activities_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activities, parent, false);
        Activities_adapter.ViewHolder holder = new Activities_adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Activities_adapter.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(items.get(position).getTitle());
        holder.content.setText(items.get(position).getContent());
        Glide.with(mContext).load(items.get(position).getImageUrl()).placeholder(R.drawable.image_loading).into(holder.image);

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (items.get(position).getHasFields() == 1) {
                    String activityUrl = String.valueOf(mContext.getResources().getString(R.string.domain_name))
                            + "tournaments/" + String.valueOf(tournamentId) + "/activity/"
                            + String.valueOf(items.get(position).getId()) + "/mobile";

                    Intent i = new Intent(mContext, WebBrowser.class);
                    i.putExtra("url", activityUrl);
                    mContext.startActivity(i);
                }
                else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.no_form_for_activity), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView title, content;
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