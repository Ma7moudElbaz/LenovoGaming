package com.cat.lenovogaming.profile;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.login_register_forgot.LoginActivity;
import com.cat.lenovogaming.network_interface.ServiceInterface;
import com.cat.lenovogaming.network_interface.Webservice;
import com.cat.lenovogaming.products_all.ProductsActivity;
import com.cat.lenovogaming.tournaments.tournament_game_details.TournamentGameDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity {

    EditText name, company, address, title, number, email, oldPassword, newPassword, confirmPassword;
    ImageView back;
    Button saveData, changePassword;

    CheckBox keepUpdated;

    private ProgressDialog dialog;

    String accessToken;
    SharedPreferences loginPreferences;

    String url;
    Retrofit retrofit;
    ServiceInterface myInterface;

    TextView emailtxt;


    Boolean isValid= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        url = getResources().getString(R.string.services_url);

        retrofit = new Retrofit.Builder()
                .baseUrl(url).
                        addConverterFactory(GsonConverterFactory.create()).build();
        myInterface = retrofit.create(ServiceInterface.class);

        name = findViewById(R.id.input_name);
        company = findViewById(R.id.input_Company);
        address = findViewById(R.id.input_Address);
        title = findViewById(R.id.input_Title);
        number = findViewById(R.id.input_Number);
        email = findViewById(R.id.input_CorporateEmail);
        oldPassword = findViewById(R.id.input_old_password);
        newPassword = findViewById(R.id.input_new_password);
        confirmPassword = findViewById(R.id.input_confirm_password);
        back = findViewById(R.id.back);
        saveData = findViewById(R.id.save_data);
        changePassword = findViewById(R.id.change_password);
        keepUpdated = findViewById(R.id.chckKeepUpdated);
        emailtxt = findViewById(R.id.emailtxt);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword();
            }
        });


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        loginPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

        accessToken = loginPreferences.getString("accessToken", "");
        emailtxt.setText(loginPreferences.getString("loginName",""));
        Log.d("token", accessToken);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setUserData();
    }

    public void setUserData() {
        dialog.show();

        myInterface.getProfile(accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200 || response.code() == 201) {
                } else if (response.code() == 401){
                    isValid = false;
                }else {
                    new AlertDialog.Builder(ProfileActivity.this)
                            .setTitle("Please Login First")
//                    .setMessage("You have to login first")
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                        onBackPressed();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }


    String keepUpdatedText = "";

    public void updateUserData() {
        dialog.show();
        Map<String, String> map = new HashMap<>();

        map.put("name", name.getText().toString());
        map.put("email", email.getText().toString());
        map.put("company", company.getText().toString());
        map.put("title", title.getText().toString());
        map.put("phone", number.getText().toString());
        map.put("address", address.getText().toString());

        if (keepUpdated.isChecked()) {
            keepUpdatedText = "1";
        } else {
            keepUpdatedText = "0";
        }
        map.put("keep_updated", keepUpdatedText);


        myInterface.updateProfile(map, accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {

                        JSONObject res = new JSONObject(response.body().string());

                        Toast.makeText(ProfileActivity.this, res.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject res = new JSONObject(response.errorBody().string());

                        Toast.makeText(ProfileActivity.this, res.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void ChangePassword() {
        String oldPasswordText = oldPassword.getText().toString();
        String newPasswordText = newPassword.getText().toString();
        String cofirmPasswordText = confirmPassword.getText().toString();

        if (oldPasswordText.length() == 0 || newPasswordText.length() == 0 || cofirmPasswordText.length() == 0) {
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
        } else if (newPasswordText == cofirmPasswordText) {
            Toast.makeText(this, "Password And Confirm Password not matched", Toast.LENGTH_SHORT).show();
        } else {

            dialog.show();
            Map<String, String> map = new HashMap<>();
            map.put("old_password", oldPasswordText);
            map.put("password", newPasswordText);
            map.put("confirm_password", cofirmPasswordText);


            myInterface.updatePassword(map, accessToken).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200||response.code() == 201) {
                            JSONObject res = new JSONObject(response.body().string());

                            Toast.makeText(ProfileActivity.this, res.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            JSONObject res = new JSONObject(response.errorBody().string());

                            Toast.makeText(ProfileActivity.this, res.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(ProfileActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}