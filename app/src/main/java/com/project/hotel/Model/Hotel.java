package com.project.hotel.Model;
import java.util.ArrayList;
public class Hotel {
    private static ArrayList<Room> list = new ArrayList<>();

    public static StringBuilder AddRoom(Room room) {
        Room found = find(room.getNumber());
        StringBuilder str = new StringBuilder();
        if (found == null) {
            list.add(room);
            str.append("Комната ").append(room.getNumber()).append(" успешно добавлена в номерный фонд.");
        } else {
            str.append("Комната ").append(room.getNumber()).append(" уже создана.");
        }
        return str;
    }


    public static boolean delete(int number) {
        Room found = find(number);
        if (found != null) {
            list.remove(found);
            return true;
        } else {
            return false;
        }
    }

    public static Room find(int number) {
        for (Room room : list) {
            if (room.getNumber()==number) {
                return room;
            }
        }
        return null;
    }

}
