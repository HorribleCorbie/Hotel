package com.project.hotel.Model;
public class Room {
    public Long id;
    public int number;
    public float price;
    public int capacity; // вмещаемость
    public String comfort;
    public float area; //площадь

    public Room(int number, float price, int capacity, String comfort, float area){
        this.number = number;
        this.price = price;
        this.capacity = capacity;
        this.comfort=comfort;
        this.area = area;
    }

}
