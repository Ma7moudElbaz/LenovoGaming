package com.cat.lenovogaming.tournaments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.network_interface.ContentWebservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TournamentsActivity extends BaseActivity {

    LinearLayout tab1, tab2, tab3;
    TextView tabDetails;
    ImageView back;
    int clicked = 0;

    private ProgressDialog dialog;

    ArrayList<Tournaments_item> tournaments_list;
    RecyclerView tournamentsRecycler;
    Tournaments_adapter tournaments_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments);

        tournaments_list = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.wait));
        dialog.setCancelable(false);

        loadTournamentsData();

        tab1 = findViewById(R.id.tab_1);
        tab2 = findViewById(R.id.tab_2);
        tab3 = findViewById(R.id.tab_3);
        tabDetails = findViewById(R.id.tab_details);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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



    public void loadTournamentsData() {
        dialog.show();
        ContentWebservice.getInstance().getApi().getTournaments().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray tournamentsArray = responseObject.getJSONArray("data");
                    setTournaments(tournamentsArray);

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


    public void setTournaments(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String title = currentobject.getString("title");
                final String content = currentobject.getString("content");
                final String imageUrl = currentobject.getString("image");

                tournaments_list.add(new Tournaments_item(id,  title, content, imageUrl));

            }
            initTournamentsRecyclerView();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initTournamentsRecyclerView() {
        tournamentsRecycler = findViewById(R.id.content_recycler);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tournamentsRecycler.setLayoutManager(layoutManager);
        tournaments_adapter = new Tournaments_adapter(this, tournaments_list);
        tournamentsRecycler.setAdapter(tournaments_adapter);

    }
}