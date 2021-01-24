package com.cat.lenovogaming.tournaments.tournament_game_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.network_interface.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameRegisteration extends BaseActivity {


    final int GALLERY_REQUEST_CODE = 123;
    String selectedImage, api_token;
    Boolean isImageSelected = false;

    ImageView back, teamImg;


    int playersNo, subsNo;
    int currentPlayer;
    int currentSub = 0;
    int gameId;
    String userEmail, teamNametxt;
    boolean lastPlayerLoaded;

    // player type 1 for player 2 for sub
    int playerType = 1;

    TextView playerTitle, nextBtn, registerTeamBtn, addSubBtn, teamLogoPick, countryCode;
    EditText fname, lname, gamerTag, discordTag, age, phone, email, confirmEmail, teamName;
    Spinner gender, countryResidence;


    SharedPreferences userPreferences;

    LinearLayout teamDetailsContainer, playersContainer, registerTeamContainer;

    private ProgressDialog dialog;

    JSONArray playersArr = new JSONArray();
    JSONArray subsArr = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_registeration);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);

        playerTitle = findViewById(R.id.player_title);
        fname = findViewById(R.id.player_fname);
        lname = findViewById(R.id.player_lname);
        gamerTag = findViewById(R.id.player_gamer_tag);
        discordTag = findViewById(R.id.player_discord_tag);
        age = findViewById(R.id.player_age);
        gender = findViewById(R.id.player_gender);
        countryResidence = findViewById(R.id.player_country_residence);
        countryCode = findViewById(R.id.player_country_code);
        phone = findViewById(R.id.player_phone);
        email = findViewById(R.id.player_email);
        confirmEmail = findViewById(R.id.player_confirm_email);
        teamDetailsContainer = findViewById(R.id.team_details_container);
        playersContainer = findViewById(R.id.players_container);
        registerTeamContainer = findViewById(R.id.register_team_layout);
        back = findViewById(R.id.back);
        teamImg = findViewById(R.id.teamImg);
        teamName = findViewById(R.id.team_name);
        teamLogoPick = findViewById(R.id.team_logo_pick);

        nextBtn = findViewById(R.id.next_btn);
        registerTeamBtn = findViewById(R.id.register_team_btn);
        addSubBtn = findViewById(R.id.add_sub_btn);
        Intent i = getIntent();

        userPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        playersNo = i.getIntExtra("playersNo", 0);
        subsNo = i.getIntExtra("subsNo", 0);
        gameId = i.getIntExtra("gameId", 0);
        userEmail = userPreferences.getString("loginName", "");
        api_token = userPreferences.getString("accessToken", "");

        setFirstForm();

        countryResidence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] countriesCodeArr = getResources().getStringArray(R.array.countriesCode);
                countryCode.setText(countriesCodeArr[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        teamLogoPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nextPlayer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        registerTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loadLastPlayer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (lastPlayerLoaded) {
                    if (playersNo == 1) {
                        try {
                            registerTeamPlayers(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        saveTeamData(selectedImage, teamName.getText().toString());
                    }
                }

            }
        });

        addSubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addSub();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setFirstForm() {
        if (playersNo == 1) {
            teamDetailsContainer.setVisibility(View.GONE);
            playersContainer.setVisibility(View.VISIBLE);

            nextBtn.setVisibility(View.GONE);
            registerTeamContainer.setVisibility(View.VISIBLE);
            setSubBtn();
            currentPlayer = 1;
        } else {
            teamDetailsContainer.setVisibility(View.VISIBLE);
            playersContainer.setVisibility(View.GONE);
            currentPlayer = 0;
        }
        setFormTitle();
    }

    private void addSub() throws JSONException {
        if (currentSub == 0) {
            if (checkFillFields()) {
                currentSub++;
                playerType = 2;
                emptyFields();
                setSubTitle();
                setSubBtn();
            }
        } else if (currentSub < subsNo) {
            String fnametxt = fname.getText().toString();
            String lnametxt = lname.getText().toString();
            String gamerTagtxt = gamerTag.getText().toString();
            String discordTagtxt = discordTag.getText().toString();
            String ageTxt = age.getText().toString();
            String countryResidenceTxt = countryResidence.getSelectedItem().toString();
            String countryCodetxt = countryCode.getText().toString();
            String phonetxt = phone.getText().toString();
            String emailtxt = email.getText().toString();
            String confirmEmailtxt = confirmEmail.getText().toString();
            String gendertxt = gender.getSelectedItem().toString();

            if (checkFillFields()) {
                emptyFields();
                currentSub++;
                setSubTitle();

                JSONObject sub = new JSONObject();
                sub.put("firstName", fnametxt);
                sub.put("lastName", lnametxt);
                sub.put("originName", gamerTagtxt);
                sub.put("discordTag", discordTagtxt);
                sub.put("age", ageTxt);
                sub.put("residenceCountry", countryResidenceTxt);
                sub.put("countryCode", countryCodetxt);
                sub.put("phone", phonetxt);
                sub.put("email", emailtxt);
                sub.put("confirmEmail", confirmEmailtxt);
                if (gendertxt.equals("Male")) {
                    sub.put("gender", "1");
                } else if (gendertxt.equals("Female")) {
                    sub.put("gender", "0");
                }

                subsArr.put(sub);
                setSubBtn();
            }
        }
    }

    private void nextPlayer() throws JSONException {
        if (currentPlayer == 0) {
            teamNametxt = teamName.getText().toString();
//            if (teamNametxt.length() == 0 || !isImageSelected) {
            if (teamNametxt.length() == 0 ) {
                Toast.makeText(getBaseContext(), R.string.fill_fields, Toast.LENGTH_SHORT).show();
            } else {
                currentPlayer++;
                teamDetailsContainer.setVisibility(View.GONE);
                playersContainer.setVisibility(View.VISIBLE);
                setFormTitle();
            }
        } else if (currentPlayer < playersNo) {
            String fnametxt = fname.getText().toString();
            String lnametxt = lname.getText().toString();
            String gamerTagtxt = gamerTag.getText().toString();
            String discordTagtxt = discordTag.getText().toString();
            String ageTxt = age.getText().toString();
            String countryResidenceTxt = countryResidence.getSelectedItem().toString();
            String countryCodetxt = countryCode.getText().toString();
            String phonetxt = phone.getText().toString();
            String emailtxt = email.getText().toString();
            String confirmEmailtxt = confirmEmail.getText().toString();
            String gendertxt = gender.getSelectedItem().toString();


            if (checkFillFields()) {
                emptyFields();
                currentPlayer++;
                setFormTitle();
                setResiterBtn();

                JSONObject player = new JSONObject();
                player.put("firstName", fnametxt);
                player.put("lastName", lnametxt);
                player.put("originName", gamerTagtxt);
                player.put("discordTag", discordTagtxt);
                player.put("age", ageTxt);
                player.put("residenceCountry", countryResidenceTxt);
                player.put("countryCode", countryCodetxt);
                player.put("phone", phonetxt);
                player.put("email", emailtxt);
                player.put("confirmEmail", confirmEmailtxt);
                if (gendertxt.equals("Male")) {
                    player.put("gender", "1");
                } else if (gendertxt.equals("Female")) {
                    player.put("gender", "0");
                }

                playersArr.put(player);
            }
        }

    }

    private boolean checkFillFields() {
        String fnametxt = fname.getText().toString();
        String lnametxt = lname.getText().toString();
        String gamerTagtxt = gamerTag.getText().toString();
        String discordTagtxt = discordTag.getText().toString();
        String ageTxt = age.getText().toString();
        String countryResidenceTxt = countryResidence.getSelectedItem().toString();
        String countryCodetxt = countryCode.getText().toString();
        String phonetxt = phone.getText().toString();
        String emailtxt = email.getText().toString();
        String confirmEmailtxt = confirmEmail.getText().toString();
        String gendertxt = gender.getSelectedItem().toString();

        if (fnametxt.length() == 0 || lnametxt.length() == 0 || gamerTagtxt.length() == 0
                || discordTagtxt.length() == 0 || ageTxt.length() == 0
                || countryCodetxt.length() == 0 || phonetxt.length() == 0 || emailtxt.length() == 0
                || confirmEmailtxt.length() == 0 || gendertxt.equals("Gender") || countryResidenceTxt.equals("Country Of Residence")) {
            Toast.makeText(this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void emptyFields() {
        fname.setText("");
        lname.setText("");
        gamerTag.setText("");
        discordTag.setText("");
        age.setText("");
        countryCode.setText("");
        phone.setText("");
        email.setText("");
        confirmEmail.setText("");
        gender.setSelection(0);
        countryResidence.setSelection(0);
    }

    private void setFormTitle() {
        if (currentPlayer == 0) {
            playerTitle.setText(R.string.team_details);
        } else if (currentPlayer == 1) {
            playerTitle.setText(R.string.captain);
        } else {
            playerTitle.setText(getString(R.string.player)+" "+ currentPlayer);
        }


    }

    private void setSubTitle() {
        if (currentSub > 0) {
            playerTitle.setText(getString(R.string.subistitution)+" "+ currentSub);
        }
    }

    private void setResiterBtn() {
        if (currentPlayer == playersNo) {
            nextBtn.setVisibility(View.GONE);
            registerTeamContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setSubBtn() {
        if (currentSub == subsNo) {
            addSubBtn.setVisibility(View.GONE);
        }
    }

    private void loadLastPlayer() throws JSONException {
        if (!lastPlayerLoaded) {
            if (checkFillFields()) {
                String fnametxt = fname.getText().toString();
                String lnametxt = lname.getText().toString();
                String gamerTagtxt = gamerTag.getText().toString();
                String discordTagtxt = discordTag.getText().toString();
                String ageTxt = age.getText().toString();
                String countryResidenceTxt = countryResidence.getSelectedItem().toString();
                String countryCodetxt = countryCode.getText().toString();
                String phonetxt = phone.getText().toString();
                String emailtxt = email.getText().toString();
                String confirmEmailtxt = confirmEmail.getText().toString();
                String gendertxt = gender.getSelectedItem().toString();


                JSONObject player = new JSONObject();
                player.put("firstName", fnametxt);
                player.put("lastName", lnametxt);
                player.put("originName", gamerTagtxt);
                player.put("discordTag", discordTagtxt);
                player.put("age", ageTxt);
                player.put("residenceCountry", countryResidenceTxt);
                player.put("countryCode", countryCodetxt);
                player.put("phone", phonetxt);
                player.put("email", emailtxt);
                player.put("confirmEmail", confirmEmailtxt);
                if (gendertxt.equals("Male")) {
                    player.put("gender", "1");
                } else if (gendertxt.equals("Female")) {
                    player.put("gender", "0");
                }

                if (playerType == 1) {
                    playersArr.put(player);
                } else {
                    subsArr.put(player);
                }
                lastPlayerLoaded = true;
            }
        }

    }

    private boolean isPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        } else {
            return true;
        }
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        if (isPermissionGranted()) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            // Sets the type as image/*. This ensures only components of type image are selected
            intent.setType("image/*");
            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            // Launching the Intent
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } else {
            Toast.makeText(this, "You Must grant Storage Permission", Toast.LENGTH_SHORT).show();
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImageUri = data.getData();
                    selectedImage = getPath(this.getApplicationContext(), selectedImageUri);
                    Log.d("Path", selectedImage);
                    teamImg.setImageURI(selectedImageUri);
                    isImageSelected = true;
                    break;
            }
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }


    public void saveTeamData(String filePath, final String teamName) {
        dialog.show();
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody teamNameBody = RequestBody.create(MediaType.parse("text/plain"), teamName);

        Webservice.getInstance().getApi().saveTeamData(part, teamNameBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        JSONObject res = new JSONObject(response.body().string());
                        int teamId = res.getInt("team_id");
                        registerTeamPlayers(teamId);

                    } else {
                        Toast.makeText(getBaseContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                Log.d("Request failurr", call.toString() + " , " + t.getMessage());
                dialog.dismiss();
            }
        });
    }

    public void registerTeamPlayers(int teamId) throws JSONException {
        JSONObject mapObj = new JSONObject();
        mapObj.put("game_id", gameId);
        mapObj.put("players", playersArr);
        mapObj.put("substitutions", subsArr);
        mapObj.put("team_id", teamId);
        mapObj.put("total_players", playersArr.length());
        mapObj.put("total_substitutions", subsArr.length());
        mapObj.put("user_email", "userEmail");

        Map<String, Object> map = new HashMap<>();
        map.put("data", mapObj);

        Log.d("Map", map.toString());
        dialog.show();
        Webservice.getInstance().getApi().registerTeamPlayers(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        JSONObject res = new JSONObject(response.body().string());
                        Toast.makeText(GameRegisteration.this, "Created Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    } else {
                        Toast.makeText(getBaseContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
