package com.cat.lenovogaming.tournaments.tournament_details;

import androidx.core.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.home.teams_and_players.Teams_and_players_item;
import com.cat.lenovogaming.network_interface.ContentWebservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TournamentDetails extends BaseActivity {

    private ProgressDialog dialog;

    ArrayList<Games_item> games_list;
    ArrayList<Activities_item> activities_list;
    ArrayList<Teams_and_players_item> team_players_list;
    ArrayList<Schedule_item> schedule_list;

    Games_adapter games_adapter;
    Activities_adapter activities_adapter;
    Schedule_adapter schedule_adapter;
    Teams_and_players_adapter_tournament teamsandplayers_adapter;

    RecyclerView contentRecycler;

    int tournamentId;

    ImageView back;

    TextView gamesTab, activitiesTab, scheduleTab,proTeamsTab,influencersTab;
    TextView title,content;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_details);

        tournamentId = getIntent().getIntExtra("id", 0);

        contentRecycler = findViewById(R.id.content_recycler);
        gamesTab = findViewById(R.id.games_tab);
        activitiesTab = findViewById(R.id.activities_tab);
        scheduleTab = findViewById(R.id.schedule_tab);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);
        proTeamsTab = findViewById(R.id.pro_teams_tab);
        influencersTab = findViewById(R.id.influencers_tab);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        games_list = new ArrayList<>();
        activities_list = new ArrayList<>();
        schedule_list = new ArrayList<>();
        team_players_list = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.wait));
        dialog.setCancelable(false);

        gamesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTabsColors();
                gamesTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_blue));
                initGamesRecyclerView();
            }
        });

        activitiesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTabsColors();
                activitiesTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_blue));
                initActivitiesRecyclerView();
            }
        });

        proTeamsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTabsColors();
                proTeamsTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_blue));
                initTeamsPlayersRecyclerView();
            }
        });

        influencersTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        scheduleTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                resetTabsColors();
//                scheduleTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_blue));
//                initScheduleRecyclerView();
            }
        });

        loadTournamentDetailsData(tournamentId);
    }

    public void resetTabsColors() {
        gamesTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        activitiesTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        proTeamsTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
    }

    public void loadTournamentDetailsData(int tournamentId) {
        dialog.show();
        ContentWebservice.getInstance().getApi().getTournamentDetails(tournamentId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONObject responseData = responseObject.getJSONObject("data");
                    JSONObject TournamentDetailsArray = responseData.getJSONObject("tournament");
                    setTournamentDetails(TournamentDetailsArray);

                    JSONArray gamesArray = responseData.getJSONArray("games");
                    setTournamentGames(gamesArray);

                    JSONArray activitiesArray = responseData.getJSONArray("activities");
                    setTournamentActivities(activitiesArray);

                    JSONArray scheduleArray = responseData.getJSONArray("schedule");
                    setTournamentschedule(scheduleArray);

                    JSONArray teamsPlayersArray = responseData.getJSONArray("proteams");
                    setTeamsPlayers(teamsPlayersArray);

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

    public void setTournamentDetails(JSONObject details) {
        try {

            title.setText(details.getString("title"));
            content.setText(details.getString("content"));
            Glide.with(this).load(details.getString("image")).placeholder(R.drawable.image_loading).into(image);

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initTeamsPlayersRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contentRecycler.setLayoutManager(layoutManager);
        teamsandplayers_adapter = new Teams_and_players_adapter_tournament(this, team_players_list);
        contentRecycler.setAdapter(teamsandplayers_adapter);

    }

    public void setTournamentGames(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int gameId = currentobject.getInt("id");
                final String title = currentobject.getString("title");
                final String date = currentobject.getString("date");
                final String gameName = currentobject.getString("game");
                final String prize = currentobject.getString("total_prize");
                final String imageUrl = currentobject.getString("image");
                final String thumbURL = currentobject.getString("thumbnail");


                games_list.add(new Games_item(gameId,tournamentId, title, date, gameName, prize, imageUrl, thumbURL));

            }
            initGamesRecyclerView();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initGamesRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contentRecycler.setLayoutManager(layoutManager);
        games_adapter = new Games_adapter(this, games_list);
        contentRecycler.setAdapter(games_adapter);

    }

    public void setTournamentActivities(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int activityId = currentobject.getInt("id");
                final String title = currentobject.getString("title");
                final String content = currentobject.getString("content");
                final String imageUrl = currentobject.getString("thumbnail");
                final int hasFields = currentobject.getInt("has_fields");


                activities_list.add(new Activities_item(activityId, title, content, imageUrl,hasFields));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initActivitiesRecyclerView() {
        Log.d("activity", activities_list.toString());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contentRecycler.setLayoutManager(layoutManager);
        activities_adapter = new Activities_adapter(this, activities_list,tournamentId);
        contentRecycler.setAdapter(activities_adapter);

    }

    public void setTournamentschedule(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final String imageUrl = currentobject.getString("image");


                schedule_list.add(new Schedule_item(1, imageUrl));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initScheduleRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        contentRecycler.setLayoutManager(layoutManager);
        schedule_adapter = new Schedule_adapter(this, schedule_list);
        contentRecycler.setAdapter(schedule_adapter);

    }
}