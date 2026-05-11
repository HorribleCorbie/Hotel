package com.project.hotel.api;

import com.project.hotel.Model.Entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

        @GET("user/{id}")
        Call<User> getUser(@Path("id") Long id);

        @GET("user/check")
        Call<Boolean> check(@Query("id")  Long id);
        @GET("user/by-login/{login}")
        Call<Boolean> checkLogin(@Path("login") String login);
        @GET("user/login/{login}")
        Call<User> getUserByLogin(@Path("login") String login);
        @POST("user")
        Call<User> createUser(@Body User user);
        @PUT("user/{login}")
        Call<User> updateUser( @Body User user, @Path("login") String login);

}
