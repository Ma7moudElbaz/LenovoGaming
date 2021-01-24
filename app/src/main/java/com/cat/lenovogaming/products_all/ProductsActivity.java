package com.cat.lenovogaming.products_all;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.cat.lenovogaming.home.products.Products_item;
import com.cat.lenovogaming.network_interface.ContentWebservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsActivity extends BaseActivity {

    private ProgressDialog dialog;

    ArrayList<Products_item> products_list;
    RecyclerView productsRecycler;
    Products_all_adapter products_all_adapter;

    ProgressBar loading;
    int currentPageNum = 1;
    int lastPageNum;
    boolean mHasReachedBottomOnce = false;

    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        loading = findViewById(R.id.loading);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        products_list = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.wait));
        dialog.setCancelable(false);

        initProductsRecyclerView();
        loadProductsData(currentPageNum);
    }

    public void loadProductsData(int pageNum) {
//        dialog.show();
        loading.setVisibility(View.VISIBLE);
        ContentWebservice.getInstance().getApi().getProducts(pageNum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray productsArray = responseObject.getJSONArray("data");
                    setProducts(productsArray);
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

                loading.setVisibility(View.GONE);
//                dialog.dismiss();
            }
        });
    }


    public void setProducts(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String title = currentobject.getString("title");
                final String content = currentobject.getString("content");
                final String imageUrl = currentobject.getString("image");
                final int isAccessory = currentobject.getInt("is_accessory");

                products_list.add(new Products_item(id, title, content, imageUrl, isAccessory));

            }

            products_all_adapter.notifyDataSetChanged();
            mHasReachedBottomOnce = false;
            currentPageNum++;


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initProductsRecyclerView() {
        productsRecycler = findViewById(R.id.content_recycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        productsRecycler.setLayoutManager(layoutManager);
        products_all_adapter = new Products_all_adapter(this, products_list);
        productsRecycler.setAdapter(products_all_adapter);

        productsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !mHasReachedBottomOnce) {
                    mHasReachedBottomOnce = true;

                    if (currentPageNum <= lastPageNum)
                        loadProductsData(currentPageNum);

                }
            }
        });

    }
}