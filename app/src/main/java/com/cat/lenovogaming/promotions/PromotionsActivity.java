package com.cat.lenovogaming.promotions;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;

public class PromotionsActivity extends BaseActivity {

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}