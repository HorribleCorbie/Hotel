package com.project.hotel.Model;

import java.time.LocalDate;

public class BookingRequest {
    private int roomId;
    private String login;
    private String in_date;
    private String out_date;
    private String price;

    public BookingRequest(int roomId, String login, LocalDate in_date, LocalDate out_date, String price) {
        this.roomId = roomId;
        this.login = login;
        String str = in_date.toString();
        this.in_date = str;
        str = out_date.toString();
        this.out_date = str;
        this.price = price;
    }
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }

    public String getOut_date() {
        return out_date;
    }

    public void setOut_date(String out_date) {
        this.out_date = out_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return  "№" + roomId +
                "\n" + in_date + ":\n" +
                out_date +
                "\n" + price + "₽";
    }
}
