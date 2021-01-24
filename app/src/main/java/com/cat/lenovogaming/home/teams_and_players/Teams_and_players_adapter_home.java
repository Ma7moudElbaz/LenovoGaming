package com.cat.lenovogaming.home.teams_and_players;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.R;

import java.util.ArrayList;
import java.util.List;

public class Teams_and_players_adapter_home extends RecyclerView.Adapter<Teams_and_players_adapter_home.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private List<Teams_and_players_item> items;

    private Context mContext;

    public Teams_and_players_adapter_home(Context context, ArrayList<Teams_and_players_item> items) {

        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Teams_and_players_adapter_home.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teams_and_players_home, parent, false);
        Teams_and_players_adapter_home.ViewHolder holder = new Teams_and_players_adapter_home.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Teams_and_players_adapter_home.ViewHolder holder, final int position) {
        //to log which item is failed
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(items.get(position).getTitle());
        Glide.with(mContext).load(items.get(position).getImgUrl()).placeholder(R.drawable.image_loading).into(holder.image);

//        int imgDrawable = mContext.getResources().getIdentifier(items.get(position).getImgUrl(), "drawable", mContext.getPackageName());
//        holder.image.setImageResource(imgDrawable);

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String twitterTxt = items.get(position).getTwitterUrl();
                if (twitterTxt.length()==0){
                    Toast.makeText(mContext, R.string.no_data, Toast.LENGTH_SHORT).show();
                }else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getTwitterUrl()));
                    mContext.startActivity(browserIntent);
                }
            }
        });


        if (items.get(position).getTwichUrl().length() == 0) {
            holder.twitchimg.setVisibility(View.GONE);
        } else {
            holder.twitchimg.setVisibility(View.VISIBLE);
        }

        if (items.get(position).getYoutubeUrl().length() == 0) {
            holder.youtubeimg.setVisibility(View.GONE);
        }else {
            holder.youtubeimg.setVisibility(View.VISIBLE);
        }

        if (items.get(position).getTwitterUrl().length() == 0) {
            holder.twitterimg.setVisibility(View.GONE);
        }else {
            holder.twitterimg.setVisibility(View.VISIBLE);
        }

        if (items.get(position).getFacebookUrl().length() == 0) {
            holder.facebookimg.setVisibility(View.GONE);
        }else {
            holder.facebookimg.setVisibility(View.VISIBLE);
        }

        if (items.get(position).getInstaUrl().length() == 0) {
            holder.instagramimg.setVisibility(View.GONE);
        }else {
            holder.instagramimg.setVisibility(View.VISIBLE);
        }

        holder.twitchimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getTwichUrl()));
                mContext.startActivity(browserIntent);
            }
        });

        holder.youtubeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getYoutubeUrl()));
                mContext.startActivity(browserIntent);
            }
        });

        holder.twitterimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getTwitterUrl()));
                mContext.startActivity(browserIntent);
            }
        });

        holder.facebookimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getFacebookUrl()));
                mContext.startActivity(browserIntent);
            }
        });

        holder.instagramimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(position).getInstaUrl()));
                mContext.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image, twitchimg, youtubeimg, twitterimg, facebookimg, instagramimg;
        LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.img);
            twitchimg = itemView.findViewById(R.id.twich);
            youtubeimg = itemView.findViewById(R.id.youtube);
            twitterimg = itemView.findViewById(R.id.twitter);
            facebookimg = itemView.findViewById(R.id.facebook);
            instagramimg = itemView.findViewById(R.id.instagram);
        }
    }
}