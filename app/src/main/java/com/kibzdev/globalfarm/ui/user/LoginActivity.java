package com.kibzdev.globalfarm.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.models.requests.LoginRequest;
import com.kibzdev.globalfarm.models.response.LoginResponse;
import com.kibzdev.globalfarm.models.response.rest.ApiInterface;
import com.kibzdev.globalfarm.models.response.rest.RestAdapter;
import com.kibzdev.globalfarm.ui.MainActivity;
import com.kibzdev.globalfarm.utils.AppUtils;
import com.kibzdev.globalfarm.utils.Constants;
import com.kibzdev.globalfarm.utils.TransparentProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private EditText phone_number;
    private EditText password;
    private SharedPreferences userPreference;
    private SharedPreferences.Editor editor;
    private TransparentProgressDialog transparentProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        context = LoginActivity.this;
        phone_number = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        TextView forgot_password = findViewById(R.id.forgot_password);
        TextView register_btn = findViewById(R.id.register_btn);
        Button login_btn = findViewById(R.id.login_btn);
        transparentProgressDialog = new TransparentProgressDialog(LoginActivity.this, "", R.drawable.ic_processing);
        userPreference = context.getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE);

        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_btn:

                loginUser();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(context, ForgotPasswordActivity.class));
                break;
            case R.id.register_btn:
                startActivity(new Intent(context, RegisterActivity.class));
                break;
        }

    }

    public void loginUser() {
        String phoneNumber = phone_number.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            phone_number.setError(context.getResources().getString(R.string.phone_required));
            phone_number.requestFocus();
            return;
        }

        if (!AppUtils.isPhoneValid(phoneNumber)) {
            phone_number.setError(context.getResources().getString(R.string.invalid_phone));
            phone_number.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Password)) {
            password.setError(context.getResources().getString(R.string.password_required));
            password.requestFocus();
            return;
        }

        tryLogin(phoneNumber, Password);
    }

    public void tryLogin(String phoneNumber, String Password) {

        transparentProgressDialog.show();
        LoginRequest request = new LoginRequest();
        request.setPassword(Password);
        request.setPhoneNumber(phoneNumber);


        ApiInterface apiInterface = RestAdapter.createAPI();
        Call<LoginResponse> call = apiInterface.loginUser(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                transparentProgressDialog.dismiss();

                if (null == response.body()) {
                    Toast.makeText(context, "Error occurred while processing your request", Toast.LENGTH_LONG).show();
                    return;
                }


                if (!response.body().getStatus().equalsIgnoreCase("00")) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                editor = userPreference.edit();

                editor.putString(Constants.NAME, response.body().getData().getName());
                editor.putString(Constants.EMAIL, response.body().getData().getEmail());
                editor.putString(Constants.PHONE_NUMBER, response.body().getData().getPhoneNumber());
                editor.putLong(Constants.USER_ID, response.body().getData().getUserId());
                editor.putBoolean(Constants.IS_LOGGED_IN, true);
                editor.apply();

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                transparentProgressDialog.dismiss();


            }
        });

    }
}