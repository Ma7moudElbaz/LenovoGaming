package com.cat.lenovogaming.news_all;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.home.news.News_adapter;
import com.cat.lenovogaming.home.news.News_item;
import com.cat.lenovogaming.network_interface.ContentWebservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends BaseActivity {

    private ProgressDialog dialog;


    ArrayList<News_item> news_list;
    RecyclerView newsRecycler;
    News_adapter news_adapter;

    ProgressBar loading;
    int currentPageNum = 1;
    int lastPageNum;
    boolean mHasReachedBottomOnce = false;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        loading = findViewById(R.id.loading);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        news_list = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.wait));
        dialog.setCancelable(false);

        initNewsRecyclerView();
        loadNewsData(currentPageNum);

    }

    public void loadNewsData(int pageNum) {
//        dialog.show();
        loading.setVisibility(View.VISIBLE);

        ContentWebservice.getInstance().getApi().getNews(pageNum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray newsArray = responseObject.getJSONArray("data");
                    setNews(newsArray);
                    JSONObject metaObject = responseObject.getJSONObject("meta");
                    lastPageNum = metaObject.getInt("last_page");

                    loading.setVisibility(View.GONE);
//                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(getBaseContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
                loading.setVisibility(View.GONE);
            }
        });
    }


    public void setNews(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String title = currentobject.getString("title");
                final String content = currentobject.getString("content");
                final String imageUrl = currentobject.getString("image");

                news_list.add(new News_item(id, title, content, imageUrl));

            }

            news_adapter.notifyDataSetChanged();
            mHasReachedBottomOnce = false;
            currentPageNum++;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initNewsRecyclerView() {
        newsRecycler = findViewById(R.id.content_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        newsRecycler.setLayoutManager(layoutManager);
        news_adapter = new News_adapter(this, news_list);
        newsRecycler.setAdapter(news_adapter);

        newsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !mHasReachedBottomOnce) {
                    mHasReachedBottomOnce = true;

                    if (currentPageNum <= lastPageNum)
                        loadNewsData(currentPageNum);

                }
            }
        });


    }
}