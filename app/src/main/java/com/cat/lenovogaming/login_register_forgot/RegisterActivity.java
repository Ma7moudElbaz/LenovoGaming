package com.cat.lenovogaming.login_register_forgot;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cat.lenovogaming.BaseActivity;
import com.cat.lenovogaming.R;
import com.cat.lenovogaming.home.MainActivity;
import com.cat.lenovogaming.network_interface.Webservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    EditText name, company, title, number, address, email, password, confirmPassword;
    Button register, back;
    CheckBox checkUpdated;

    private ProgressDialog dialog;


    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.register_name);
        company = findViewById(R.id.register_company);
        title = findViewById(R.id.register_title);
        number = findViewById(R.id.register_number);
        address = findViewById(R.id.register_address);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_confirm_password);
        checkUpdated = findViewById(R.id.chckupdated);
        register = findViewById(R.id.register_btn);
        back = findViewById(R.id.register_back_btn);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);


        shared = getSharedPreferences("userData", Context.MODE_PRIVATE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void createAccount() {
        Map<String, String> map = new HashMap<>();

        final String nametxt = name.getText().toString();
        final String companytxt = company.getText().toString();
        final String titletxt = title.getText().toString();
        final String numbertxt = number.getText().toString();
        final String addresstxt = address.getText().toString();
        final String emailtxt = email.getText().toString();
        final String passwordtxt = password.getText().toString();
        final String confirmPasswordtxt = confirmPassword.getText().toString();

        if (emailtxt.length() == 0 || passwordtxt.length() == 0 || confirmPasswordtxt.length() == 0) {
            Toast.makeText(getBaseContext(), R.string.fill_fields, Toast.LENGTH_SHORT).show();
        } else if (!passwordtxt.equals(confirmPasswordtxt)) {
            Toast.makeText(this, R.string.password_not_matched, Toast.LENGTH_SHORT).show();
        } else {
//            map.put("name", nametxt);
            map.put("name", nametxt);
            map.put("email", emailtxt);
            map.put("password", passwordtxt);
            map.put("confirm_password", confirmPasswordtxt);
//            map.put("mobile_number", numbertxt);
//            map.put("title", titletxt);
//            map.put("company", companytxt);
//            map.put("address", addresstxt);
//            if (checkUpdated.isChecked()) {
//                map.put("keep_updated", "1");
//            } else {
//                map.put("keep_updated", "0");
//            }
            Log.d("Map", map.toString());
            dialog.show();
            Webservice.getInstance().getApi().register(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            JSONObject res = new JSONObject(response.body().string());
                            SharedPreferences.Editor myEdit = shared.edit();
                            myEdit.putString("loginName", emailtxt);

                            myEdit.commit();
                            Toast.makeText(RegisterActivity.this, R.string.account_created, Toast.LENGTH_SHORT).show();

                            onBackPressed();

                        }else {
                            Toast.makeText(RegisterActivity.this,  response.errorBody().string(), Toast.LENGTH_SHORT).show();
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

}