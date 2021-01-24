package com.cat.lenovogaming.login_register_forgot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.lenovogaming.R;
import com.cat.lenovogaming.network_interface.ServiceInterface;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordDialog extends DialogFragment {


    Button reset, cancel;
    TextView email;

    String url;
    Retrofit retrofit;
    ServiceInterface myInterface;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reset_password, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);

        url = getResources().getString(R.string.services_url);

        retrofit = new Retrofit.Builder()
                .baseUrl(url).
                        addConverterFactory(GsonConverterFactory.create()).build();
        myInterface = retrofit.create(ServiceInterface.class);

        reset = view.findViewById(R.id.btnReset);
        cancel = view.findViewById(R.id.btnCancel);
        email = view.findViewById(R.id.reset_email);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }


    public void resetPassword() {
        Map<String, String> map = new HashMap<>();
        final String emailtxt = email.getText().toString();

        if (emailtxt.length() == 0) {
            Toast.makeText(getActivity(), "Enter Your Email", Toast.LENGTH_SHORT).show();
        } else {
            map.put("email", emailtxt);

            dialog.show();
            myInterface.forgotPassword(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String msg = response.body().string();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getActivity(),"Network Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });

        }

    }
}
