package com.project.hotel.Model;

public interface OnRoomFoundListener {
    void onRoomFound(Room room);
    void onRoomDeleted();
    void selectRoom(Room room);
}
