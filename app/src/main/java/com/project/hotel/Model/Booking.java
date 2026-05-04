package com.project.hotel.Model;

public class Booking {
    private Long id;
    private Room room;
    private User client;
    private String  in_date;
    private String  out_date;
    private float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String  getIn_date() {
        return in_date;
    }

    public void setIn_date(String  in_date) {
        this.in_date = in_date;
    }

    public String  getOut_date() {
        return out_date;
    }

    public void setOut_date(String  out_date) {
        this.out_date = out_date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Booking(Room room, User client, String  in_date, String  out_date, float price) {
        this.room = room;
        this.client = client;
        this.in_date = in_date;
        this.out_date = out_date;
        this.price = price;
    }
}
