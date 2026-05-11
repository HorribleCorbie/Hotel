package com.project.hotel.Model.Interface;

import com.project.hotel.Model.Entity.Room;

public interface OnRoomFoundListener {
    void onRoomFound(Room room);
    void onRoomDeleted();
    void selectRoom(Room room);
}
