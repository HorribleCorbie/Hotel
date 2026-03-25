package com.project.hotel.Model;

enum ComfortClass{
    economy,
    standard,
    luxe,
}
public class Room {
    private int id;
    private int number;
    private float price;
    private int capacity; // вмещаемость
    private ComfortClass comfort;
    private float area; //площадь
    private int beds;

    public Room(int number, float price, int capacity, ComfortClass comfort, float area, int beds){
        this.number=number;
        this.price=price;
        this.capacity=capacity;
        this.area=area;
        this.beds=beds;
    }

    public ComfortClass getComfort() {
        return comfort;
    }

    public float getArea() {
        return area;
    }

    public float getPrice() {
        return price;
    }

    public int getBeds() {
        return beds;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumber() {
        return number;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setComfort(ComfortClass comfort) {
        this.comfort = comfort;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPrice(float price) {
        this.price = price;
    }

//    @Override
//    public String toString(){
//        return
//    }
}
