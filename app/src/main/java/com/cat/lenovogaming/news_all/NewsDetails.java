package com.cat.lenovogaming.news_all;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;

public class NewsDetails extends BaseActivity {

    ImageView back,image;
    TextView title,content;

    String imageUrl,titletxt,contenttxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        back = findViewById(R.id.back);
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        Intent i = getIntent();
        imageUrl = i.getStringExtra("image");
        titletxt = i.getStringExtra("title");
        contenttxt = i.getStringExtra("content");

        Glide.with(this).load(imageUrl).placeholder(R.drawable.image_loading).into(image);
        title.setText(titletxt);
        content.setText(contenttxt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}