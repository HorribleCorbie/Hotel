package com.project.hotel.Model;
public class Room {
    public int number;
    public float price;
    public int capacity; // вмещаемость
    public String comfort;
    public float area; //площадь
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



    public Room(int number, float price, int capacity, String comfort, float area){
        this.number = number;
        this.price = price;
        this.capacity = capacity;
        this.comfort=comfort;
        this.area = area;
    }

    public String[] RoomtoString(){
        String[] list = new String[5];
        list[0] = String.valueOf(number);
        list[1] = String.valueOf(price);
        list[2] = String.valueOf(capacity);
        list[3] = comfort;
        list[4] = String.valueOf(area);
        return list;
    }

}
