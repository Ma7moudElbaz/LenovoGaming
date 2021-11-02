package com.cat.lenovogaming.home;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.SideNavigationHandler;
import com.cat.lenovogaming.home.gallery.Gallery_adapter;
import com.cat.lenovogaming.home.gallery.Gallery_item;
import com.cat.lenovogaming.home.teams_and_players.Teams_and_players_adapter_home;
import com.cat.lenovogaming.home.teams_and_players.Teams_and_players_item;
import com.cat.lenovogaming.home.news.News_adapter;
import com.cat.lenovogaming.home.news.News_item;
import com.cat.lenovogaming.home.products.Products_home_adapter;
import com.cat.lenovogaming.home.products.Products_item;
import com.cat.lenovogaming.home.slider.Slides_adapter;
import com.cat.lenovogaming.home.slider.Slides_item;
import com.cat.lenovogaming.network_interface.ContentWebservice;
import com.cat.lenovogaming.news_all.NewsActivity;
import com.cat.lenovogaming.products_all.ProductsActivity;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private ProgressDialog dialog;

    ArrayList<Slides_item> slides_list;
    ArrayList<News_item> news_list;
    ArrayList<Products_item> products_list;
    ArrayList<Teams_and_players_item> team_players_list;
    ArrayList<Gallery_item> gallery_list;


    CardSliderViewPager slider;
    RecyclerView newsRecycler;
    RecyclerView productsRecycler;
    RecyclerView teamsPlayersRecycler;
    RecyclerView galleryRecycler;

    Slides_adapter slides_adapter;
    News_adapter news_adapter;
    Products_home_adapter products_home_adapter;
    Teams_and_players_adapter_home teamsandplayers_adapter;
    Gallery_adapter gallery_adapter;

    TextView news_viewAll, products_viewAll;

    ImageView menuIcon;
    NavigationView navigationView;
    DrawerLayout drawer;


    SharedPreferences shared;
    String api_token;
    int loggedIn;

    Menu nav_Menu;

    LinearLayout tab1, tab2, tab3;
    TextView tabDetails;
    int clicked = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab1 = findViewById(R.id.tab_1);
        tab2 = findViewById(R.id.tab_2);
        tab3 = findViewById(R.id.tab_3);
        tabDetails = findViewById(R.id.tab_details);


        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1_click();
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab2_click();
            }
        });

        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab3_click();
            }
        });


        shared = getSharedPreferences("userData", Context.MODE_PRIVATE);
        api_token = shared.getString("api_token", "0");
        loggedIn = shared.getInt("loggedIn", 0);


        menuIcon = findViewById(R.id.menu_icon);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        nav_Menu = navigationView.getMenu();
        setLoginLogoutDrawerText();

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.wait));
        dialog.setCancelable(false);

        slider = findViewById(R.id.homeSlider);
        news_viewAll = findViewById(R.id.news_view_all);
        products_viewAll = findViewById(R.id.products_view_all);

        news_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(i);
            }
        });

        products_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });


        slides_list = new ArrayList<>();
        news_list = new ArrayList<>();
        products_list = new ArrayList<>();
        team_players_list = new ArrayList<>();
        gallery_list = new ArrayList<>();


        loadHomeData();

    }

    public void loadHomeData() {
        dialog.show();
        ContentWebservice.getInstance().getApi().getHomeData().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONObject responseObjectData = responseObject.getJSONObject("data");

                    JSONArray sliderArray = responseObjectData.getJSONArray("slides");
                    setSlider(sliderArray);

                    JSONArray newsArray = responseObjectData.getJSONArray("news");
                    setNews(newsArray);

                    JSONArray productsArray = responseObjectData.getJSONArray("products");
                    setProducts(productsArray);

                    JSONArray gamesArray = responseObjectData.getJSONArray("proteams");
                    setTeamsPlayers(gamesArray);

                    JSONArray galleryArray = responseObjectData.getJSONArray("gallery");
                    setGallery(galleryArray);


                    dialog.dismiss();


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
                dialog.dismiss();
            }
        });
    }

    public void setSlider(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String imageUrl = currentobject.getString("image");
                final String title = currentobject.getString("title");
                final String content = currentobject.getString("content");
                final String externalUrl = currentobject.getString("external_link");
                final int hasContentInt = currentobject.getInt("has_content");
                final boolean hasContent;
                hasContent = hasContentInt != 0;
                slides_list.add(new Slides_item(id, imageUrl, title, content, externalUrl, hasContent));

            }

            slides_adapter = new Slides_adapter(this, slides_list);
            slider.setAdapter(slides_adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            initNewsRecyclerView();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initNewsRecyclerView() {
        newsRecycler = findViewById(R.id.news_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        newsRecycler.setLayoutManager(layoutManager);
        news_adapter = new News_adapter(this, news_list);
        newsRecycler.setAdapter(news_adapter);

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

                products_list.add(new Products_item(id, title, content, imageUrl,isAccessory));

            }
            initProductsRecyclerView();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initProductsRecyclerView() {
        productsRecycler = findViewById(R.id.products_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        productsRecycler.setLayoutManager(layoutManager);
        products_home_adapter = new Products_home_adapter(this, products_list);
        productsRecycler.setAdapter(products_home_adapter);

    }

    private void getTeamsPlayerData() throws IOException, JSONException {
        InputStream is = getAssets().open("data.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String data = new String(buffer, StandardCharsets.UTF_8);
        JSONArray teamsPlayersArray = new JSONArray(data);
        setTeamsPlayers(teamsPlayersArray);
    }


    public void setTeamsPlayers(JSONArray list) {
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = 1;
                final String title = currentobject.getString("title");
                final String imageUrl = currentobject.getString("image");
                final String twichUrl = currentobject.getString("twitch");
                final String youtubeUrl = currentobject.getString("youtube");
                final String twitterUrl = currentobject.getString("twitter");
                final String facebookUrl = currentobject.getString("facebook");
                final String instaUrl = currentobject.getString("instagram");

                team_players_list.add(new Teams_and_players_item(id, title, imageUrl, twichUrl, youtubeUrl, twitterUrl, facebookUrl, instaUrl));

            }
            initTeamsPlayersRecyclerView();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initTeamsPlayersRecyclerView() {
        teamsPlayersRecycler = findViewById(R.id.teams_players_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        teamsPlayersRecycler.setLayoutManager(layoutManager);
        teamsandplayers_adapter = new Teams_and_players_adapter_home(this, team_players_list);
        teamsPlayersRecycler.setAdapter(teamsandplayers_adapter);

    }


    public void setGallery(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String imageUrl = currentobject.getString("image");
                final String externalUrl = currentobject.getString("external_link");

                gallery_list.add(new Gallery_item(id, imageUrl, externalUrl));

            }
            initgalleryRecyclerView();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initgalleryRecyclerView() {
        galleryRecycler = findViewById(R.id.gallery_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        galleryRecycler.setLayoutManager(layoutManager);
        gallery_adapter = new Gallery_adapter(this, gallery_list);
        galleryRecycler.setAdapter(gallery_adapter);

    }

    private void tab1_click() {
        reset_tabs();
        if (clicked == 1) {
            tabDetails.setVisibility(View.GONE);
            clicked = 0;
        } else {
            tabDetails.setVisibility(View.VISIBLE);
            tabDetails.setText(R.string.what_legion_zone_txt);
            tab1.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.dark_blue));
            clicked = 1;
        }
    }

    private void tab2_click() {
        reset_tabs();
        if (clicked == 2) {
            tabDetails.setVisibility(View.GONE);
            clicked = 0;
        } else {
            tabDetails.setVisibility(View.VISIBLE);
            tabDetails.setText(R.string.how_to_participate_txt);
            tab2.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.dark_blue));
            clicked = 2;
        }
    }

    private void tab3_click() {
        reset_tabs();
        if (clicked == 3) {
            tabDetails.setVisibility(View.GONE);
            clicked = 0;
        } else {
            tabDetails.setVisibility(View.VISIBLE);
            tabDetails.setText(R.string.tournament_rewards_txt);
            tab3.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.dark_blue));
            clicked = 3;
        }
    }

    private void reset_tabs() {
        tab1.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        tab2.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        tab3.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
    }

    public void setLoginLogoutDrawerText() {
        if (loggedIn == 1) {
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            nav_Menu.findItem(R.id.nav_profile).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.nav_login).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();


        SideNavigationHandler handler = new SideNavigationHandler(MainActivity.this, id);
        handler.navigate();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
    }


}