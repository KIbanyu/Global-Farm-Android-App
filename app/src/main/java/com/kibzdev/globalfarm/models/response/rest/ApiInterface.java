package com.kibzdev.globalfarm.models.response.rest;

import com.kibzdev.globalfarm.models.requests.LoginRequest;
import com.kibzdev.globalfarm.models.requests.PostDateRequest;
import com.kibzdev.globalfarm.models.requests.RegisterRequest;
import com.kibzdev.globalfarm.models.response.BaseResponse;
import com.kibzdev.globalfarm.models.response.LoginResponse;
import com.kibzdev.globalfarm.models.response.models.response.GetPostResponse;

import java.math.BigDecimal;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {

    @POST("login-user")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("register-user")
    Call<BaseResponse> registerUser(@Body RegisterRequest request);

    @Multipart
    @POST("add-post")
    Call<BaseResponse> addPost(@Part MultipartBody.Part file, @Query("name") String name,
                               @Query("category") String category, @Query("price") BigDecimal price,
                               @Query("quantity") String quantity, @Query("description") String description,
                               @Query("location") String location, @Query("phone") String phone);

    @GET("get-posts")
    Call<GetPostResponse> getPosts(@Query("type") String type);


}
