package com.project.hotel.Model;

public interface OnBookingFoundListener {
    void onBookingFound(Booking booking);

    void onBookingDeleted();

    void selectBooking(Booking booking);
}
