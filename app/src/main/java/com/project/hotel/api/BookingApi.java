package com.project.hotel.api;

import com.project.hotel.Model.Entity.Booking;
import com.project.hotel.Model.Entity.BookingRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookingApi {

    @POST("booking")
    Call<Booking> newBooking(@Body BookingRequest booking);
    @GET("booking")
    Call<List<Booking>> getAllBookings();

    @GET("booking/check")
    Call<Boolean> check(@Query("id") Long id);

    @PUT("booking/{id}")
    Call<Booking> updateBooking(@Path("id") Long id,  @Body BookingRequest booking);

    @DELETE("booking/delete/{id}")
    Call<Void> deleteBooking(@Path("id") Long id);

    @GET("booking/all/{id}")
    Call<List<Booking>> getAllBookingsByClients(@Path("id") Long id);

    @GET("booking/one/{id}")
    Call<Booking> getBooking(@Path("id") Long id);

}
