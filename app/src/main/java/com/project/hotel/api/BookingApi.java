package com.project.hotel.api;

import com.project.hotel.Model.Booking;
import com.project.hotel.Model.BookingRequest;
import com.project.hotel.Model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookingApi {

    @GET("booking")
    Call<List<Booking>> getAllBookings();

    @GET("booking/all/{id}")
    Call<List<Booking>> getAllBookingsByClients(@Path("id") Long id);

    @GET("booking/one/{id}")
    Call<Booking> getBooking(@Path("id") Long id);

    @POST("booking")
    Call<Booking> newBooking(@Body BookingRequest booking);

    @PUT("booking/{id}")
    Call<Booking> updateBooking( @Body Booking booking, @Path("number") Long id);

    @DELETE("booking/{id}")
    Call<Void> deleteBooking(@Path("number") Long id);


}
