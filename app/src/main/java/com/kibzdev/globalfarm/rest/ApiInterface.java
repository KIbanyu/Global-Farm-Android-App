package com.kibzdev.globalfarm.rest;

import com.kibzdev.globalfarm.models.requests.LoginRequest;
import com.kibzdev.globalfarm.models.requests.RegisterRequest;
import com.kibzdev.globalfarm.models.response.BaseResponse;
import com.kibzdev.globalfarm.models.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiInterface {

    @POST("login-user")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("register-user")
    Call<BaseResponse> registerUser(@Body RegisterRequest request);

}
