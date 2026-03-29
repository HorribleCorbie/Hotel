package com.project.hotel.api;

import com.project.hotel.Model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface HotelApi {

    @GET("rooms")
    Call<List<Room>> getAllRooms();

    @GET("rooms/{id}")
    Call<Room> getRoom(@Path("id") Long id);

    @POST("rooms")
    Call<Room> createRoom(@Body Room room);

    @PUT("rooms/{id}")
    Call<Room> updateRoom(@Path("id") Long id, @Body Room room);

    @DELETE("rooms/{id}")
    Call<Void> deleteRoom(@Path("id") Long id);
}