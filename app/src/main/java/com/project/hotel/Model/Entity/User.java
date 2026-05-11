package com.project.hotel.Model.Entity;

public class User {
    private Long id;

    public Long getId() {
        return id;
    }

    private String login;
    private String password;
    private String name;
    private String role;

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(User oldUser) {
        this.id = oldUser.getId();
        this.login = oldUser.getLogin();
        this.password = oldUser.getPassword();
        this.name = oldUser.getName();
        this.role = oldUser.getRole();
    }

    public User(String login, String password, String name, String role) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
