package com.cat.lenovogaming.login_register_forgot;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.network_interface.Webservice;
import com.cat.lenovogaming.home.MainActivity;
import com.cat.lenovogaming.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    EditText email, password;
    Button login, register, skip;
    CheckBox checkRemeber;
    TextView forgotPassowrd;
    SharedPreferences shared;

    ImageView back;


    private ProgressDialog dialog;

    SharedPreferences.Editor myEdit;

    public void showResetPasswordDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new ForgotPasswordDialog();
        dialogFragment.show(ft, "dialog");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        back = findViewById(R.id.back);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);
        skip = findViewById(R.id.skip_btn);
        checkRemeber = findViewById(R.id.chckRemember);
        forgotPassowrd = findViewById(R.id.login_forgotpass);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);


        shared = getSharedPreferences("userData", Context.MODE_PRIVATE);
        myEdit = shared.edit();
        myEdit.putString("accessToken", "");
        myEdit.putInt("loggedIn", 0);

        myEdit.apply();

        email.setText(shared.getString("loginName", ""));
        password.setText(shared.getString("password", ""));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });

        forgotPassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResetPasswordDialog();
            }
        });

    }


    public void login() {
        Map<String, String> map = new HashMap<>();
        final String emailtxt = email.getText().toString();
        final String passwordtxt = password.getText().toString();

        if (emailtxt.length() == 0 || passwordtxt.length() == 0) {
            Toast.makeText(getBaseContext(), R.string.fill_fields, Toast.LENGTH_SHORT).show();
        } else {
            map.put("email", emailtxt);
            map.put("password", passwordtxt);
            dialog.show();
            Webservice.getInstance().getApi().login(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            JSONObject res = new JSONObject(response.body().string());
                            JSONObject userData = res.getJSONObject("user");

                            myEdit.putString("accessToken", "Bearer " + res.getString("access_token"));
                            myEdit.putInt("loggedIn", 1);
                            myEdit.putString("loginName", emailtxt);
                            if (checkRemeber.isChecked()) {
                                myEdit.putString("password", passwordtxt);
                            } else {
                                myEdit.putString("password", "");
                            }
                            myEdit.apply();

                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}