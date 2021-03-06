package com.kibzdev.globalfarm.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.models.requests.RegisterRequest;
import com.kibzdev.globalfarm.models.response.BaseResponse;
import com.kibzdev.globalfarm.models.response.rest.ApiInterface;
import com.kibzdev.globalfarm.models.response.rest.RestAdapter;
import com.kibzdev.globalfarm.utils.AppUtils;
import com.kibzdev.globalfarm.utils.TransparentProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private EditText first_name;
    private EditText phone_number;
    private EditText password;
    private EditText email;
    private TransparentProgressDialog transparentProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = RegisterActivity.this;
        first_name = findViewById(R.id.first_name);
        phone_number = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        TextView back_to_login = findViewById(R.id.back_to_login);
        email = findViewById(R.id.email);
        back_to_login.setOnClickListener(this);
        Button reg_btn = findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(this);
        transparentProgressDialog = new TransparentProgressDialog(RegisterActivity.this, "", R.drawable.ic_processing);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.reg_btn:
                validateInput();
                break;
            case R.id.back_to_login:
                finish();
                break;
        }

    }

    public void validateInput() {
        String firstName = first_name.getText().toString().trim();
        String phoneNumber = phone_number.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Email = email.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            first_name.setError(context.getResources().getString(R.string.first_name_required));
            first_name.requestFocus();
            return;
        }


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

        if (TextUtils.isEmpty(Email)) {
            email.setError(context.getResources().getString(R.string.enter_year_of_birth));
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Password)) {
            password.setError(context.getResources().getString(R.string.password_required));
            password.requestFocus();
            return;
        }

        registerUser(firstName, phoneNumber, Password, Email);

    }

    public void registerUser(String firstName, String phoneNumber, String Password, String Email) {

        transparentProgressDialog.show();
        RegisterRequest request = new RegisterRequest();
        request.setName(firstName);
        request.setEmail(Email);
        request.setPhoneNumber(phoneNumber);
        request.setPassword(Password);


        ApiInterface apiInterface = RestAdapter.createAPI();
        Call<BaseResponse> call = apiInterface.registerUser(request);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                transparentProgressDialog.dismiss();

                if (null == response.body()) {
                    Toast.makeText(context, "Error occurred while processing your request", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!response.body().getStatus().equalsIgnoreCase("00")) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                transparentProgressDialog.dismiss();


            }
        });

    }
}