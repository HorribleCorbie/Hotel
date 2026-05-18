package com.project.hotel.Model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room {
    private int number;
    private float price;
    private int capacity;
    private String comfort;
    private float area;

    public int getNumber() {
        return number;
    }

    public float getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getComfort() {
        return comfort;
    }

    public float getArea() {
        return area;
    }


    public Room(int number, float price, int capacity, String comfort, float area) {
        this.number = number;
        this.price = price;
        this.capacity = capacity;
        this.comfort = comfort;
        this.area = area;
    }

    static public String[] RoomtoString(Room room) {
        String[] list = new String[5];
        list[0] = String.valueOf(room.getNumber());
        list[1] = String.valueOf(room.getPrice());
        list[2] = String.valueOf(room.getCapacity());
        list[3] = room.getComfort();
        list[4] = String.valueOf(room.getArea());
        return list;
    }

    static public List<Integer> RoomstoList(List<Room> rooms) {
        List<Integer> numbers = new ArrayList<>();
        for (Room room : rooms) {
            numbers.add(room.getNumber());
        }
        return numbers;
    }

    static public float GetRoomPrice(List<Room> rooms, int number) {
        for (Room room : rooms) {
            if (number == room.getNumber())
                return room.getPrice();
        }
        throw new NullPointerException();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return number == room.number && Float.compare(price, room.price) == 0 && capacity == room.capacity && Float.compare(area, room.area) == 0 && Objects.equals(comfort, room.comfort);
    }
}