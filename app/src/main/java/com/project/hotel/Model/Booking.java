package com.project.hotel.Model;

public class Booking {
    private Long id;
    private Room room;
    private User client;
    private String  in_date;
    private String  out_date;
    private float price;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

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

    static public String[] BookingClientToString(Booking booking) {
        String[] list = new String[4];
        list[0] = String.valueOf(booking.getRoom().getNumber());
        list[1] = String.valueOf(booking.getPrice());
        list[2] = booking.getIn_date();
        list[3] = booking.getOut_date();
        System.out.println(booking.toString());
        return list;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", room=" + room.getNumber() +
                ", client=" + client +
                ", in_date='" + in_date + '\'' +
                ", out_date='" + out_date + '\'' +
                ", price=" + price +
                '}';
    }
//    static public String[] BookingToString(Booking booking) {
//        String[] list = new String[5];
//        list[0] = String.valueOf(booking.getId());
//        list[1] = String.valueOf(booking.getRoom().getNumber());
//        list[2] = String.valueOf(booking.getClient().getId());
//        list[3] = booking.getComfort();
//        list[4] = String.valueOf(booking.getArea());
//        return list;
//    }
}
