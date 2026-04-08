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

    @GET("rooms/number/{number}")
    Call<Room> getRoom(@Path("number") int number);

    @GET("rooms/check")
    Call<Boolean> check(@Query("number") int number);

    @POST("rooms")
    Call<Room> createRoom(@Body Room room);

    @PUT("rooms/{number}")
    Call<Boolean> updateRoom( @Body Room room, @Path("number") int number);

    @DELETE("rooms/{number}")
    Call<Void> deleteRoom(@Path("number") int number);
}