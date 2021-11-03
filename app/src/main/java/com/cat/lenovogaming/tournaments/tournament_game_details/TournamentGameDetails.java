package com.cat.lenovogaming.tournaments.tournament_game_details;

import androidx.core.content.ContextCompat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.WebBrowser;
import com.cat.lenovogaming.network_interface.ContentWebservice;
import com.cat.lenovogaming.network_interface.Webservice;

import org.json.JSONObject;


public class TournamentGameDetails extends BaseActivity {

    private ProgressDialog dialog;
    int gameId, tournamentId;
    TextView title, date, format, mode, rulesTab, discordTab, rulesTxt, gameRegister;
    WebView discordWebView;
    ImageView back, image, info;
    int playersNo, subsNo;

    DetailsDialog infoDialog;

    SharedPreferences userPreferences;
    String api_token;
    int loggedIn;

    String gameUrl;
    Boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_game_details);

        userPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

        api_token = userPreferences.getString("accessToken", "");
        loggedIn = userPreferences.getInt("loggedIn", 0);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.wait));
        dialog.setCancelable(false);

        title = findViewById(R.id.title);
        date = findViewById(R.id.datetxt);
        format = findViewById(R.id.formattxt);
        mode = findViewById(R.id.modetxt);
        rulesTab = findViewById(R.id.rules_tab);
        discordTab = findViewById(R.id.discord_tab);
        rulesTxt = findViewById(R.id.rulestxt);
        back = findViewById(R.id.back);
        image = findViewById(R.id.image);
        info = findViewById(R.id.info);
        discordWebView = findViewById(R.id.discordWebView);
        gameRegister = findViewById(R.id.game_register);

        gameId = getIntent().getIntExtra("gameId", 0);
        tournamentId = getIntent().getIntExtra("tournamentId", 0);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        validateToken();
        loadTournamentGameData(tournamentId, gameId);

        gameRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gameUrl = getResources().getString(R.string.domain_name) + "tournaments/"
                        + tournamentId + "/game/" + gameId + "/mobile";

                Intent i = new Intent(getBaseContext(), WebBrowser.class);
                i.putExtra("url", gameUrl);
                startActivity(i);


                //my activity regusteration
//                Intent i = new Intent(getBaseContext(), GameRegisteration.class);
//                        i.putExtra("playersNo", playersNo);
//                        i.putExtra("subsNo", subsNo);
//                        i.putExtra("gameId", gameId);
//                        startActivity(i);

                //check if logged in
//                if (loggedIn == 1) {
//                    if (isValid){
//                        Intent i = new Intent(getBaseContext(), GameRegisteration.class);
//                        i.putExtra("playersNo", playersNo);
//                        i.putExtra("subsNo", subsNo);
//                        i.putExtra("gameId", gameId);
//                        startActivity(i);
//                    }
//                    else {
//                        new AlertDialog.Builder(TournamentGameDetails.this)
//                                .setTitle("Please Login First")
////                    .setMessage("You have to login first")
//                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
//                                        startActivity(i);
//                                    }
//                                })
//                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
////                                        onBackPressed();
//                                    }
//                                })
//                                .setCancelable(true)
//                                .show();
//                    }
//
//                }else {
//                        new AlertDialog.Builder(TournamentGameDetails.this)
//                                .setTitle("Please Login First")
////                    .setMessage("You have to login first")
//                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
//                                        startActivity(i);
//                                    }
//                                })
//                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
////                                        onBackPressed();
//                                    }
//                                })
//                                .setCancelable(true)
//                                .show();
//
//                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        rulesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rulesTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_blue));
                discordTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
                rulesTxt.setVisibility(View.VISIBLE);
                discordWebView.setVisibility(View.GONE);
            }
        });

        discordTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rulesTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
                discordTab.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.light_blue));
                rulesTxt.setVisibility(View.GONE);
                discordWebView.setVisibility(View.VISIBLE);
            }
        });

    }


    public void loadTournamentGameData(int tournamentId, int gameId) {
        dialog.show();
        ContentWebservice.getInstance().getApi().getTournamentGameDetails(tournamentId, gameId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONObject gameData = responseObject.getJSONObject("data");
                    setTournamentGameDetails(gameData);
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

    public void setTournamentGameDetails(JSONObject details) {
        try {

            title.setText(details.getString("title"));
            date.setText(details.getString("date_month"));
            format.setText(details.getString("format"));
            mode.setText(details.getString("mode"));
            rulesTxt.setText(Html.fromHtml(details.getString("rule")));
            Glide.with(this).load(details.getString("image")).placeholder(R.drawable.image_loading).into(image);
            loadDiscordWebView(details.getString("discord_url"));
            discordWebView.loadUrl(details.getString("discord_url"));

            playersNo = details.getInt("total_players");
            subsNo = details.getInt("total_substitutions");


            infoDialog = new DetailsDialog(details.getString("date"), details.getString("game"), details.getString("organizer")
                    , details.getString("type"), details.getString("total_prize"), details.getString("quick_rules"));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadDiscordWebView(String url) {

        discordWebView.getSettings().setJavaScriptEnabled(true);
        discordWebView.getSettings().setLoadWithOverviewMode(true);
        discordWebView.getSettings().setUseWideViewPort(true);

        discordWebView.getSettings().setDomStorageEnabled(true);
        discordWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        discordWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // DOT CALL SUPER METHOD
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progDailog.show();
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {

//                progDailog.dismiss();
            }
        });

        discordWebView.loadUrl(url);

    }


    public void validateToken() {
        Webservice.getInstance().getApi().getProfile(api_token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200 || response.code() == 201) {
                    isValid = true;
                } else if (response.code() == 401) {
                    isValid = false;
                } else {
                    isValid = false;
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });
    }
}