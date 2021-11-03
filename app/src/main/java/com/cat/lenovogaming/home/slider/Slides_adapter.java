package com.cat.lenovogaming.home.slider;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.WebBrowser;
import com.github.islamkhsh.CardSliderAdapter;

import java.util.ArrayList;

public class Slides_adapter extends CardSliderAdapter<Slides_item> {

    private final Context context;

    public Slides_adapter(Context context, ArrayList<Slides_item> items) {
        super(items);
        this.context = context;

    }

    @Override
    public void bindView(int i, View view, final Slides_item item) {

        ImageView image = view.findViewById(R.id.slides_img);
        RelativeLayout parent_layout = view.findViewById(R.id.parent_layout);

        Glide.with(context).asBitmap().load(item.getImageUrl()).into(image);

        parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.getExternalUrl().equals("#")){
                    Intent i = new Intent(context, WebBrowser.class);
                    i.putExtra("url",item.getExternalUrl());
                    context.startActivity(i);
                }
                else {

                }
            }
        });

    }

    @Override
    public int getItemContentLayout(int i) {
        return R.layout.item_slides;
    }
}
