package com.project.hotel.Model.Interface;

import com.project.hotel.Model.Entity.Booking;

public interface OnBookingFoundListener {
    void onBookingFound(Booking booking);

    void onBookingDeleted();

    void selectBooking(Booking booking);
}
