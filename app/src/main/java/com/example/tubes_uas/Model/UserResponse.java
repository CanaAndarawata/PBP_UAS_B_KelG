package com.example.tubes_uas.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("user")
    @Expose
    private UserDAO user;

    @SerializedName("users")
    @Expose
    private List<UserDAO> users;

    @SerializedName("data")
    @Expose
    private UserDAO data;

    @SerializedName("message")
    @Expose
    private String message;

    public UserDAO getUser() {
        return user;
    }

    public List<UserDAO> getUsers() {
        return users;
    }

    public void setUser(UserDAO user) {
        this.user = user;
    }

    public void setUsers(List<UserDAO> users) {
        this.users = users;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDAO getData() {
        return data;
    }

    public void setData(UserDAO data) {
        this.data = data;
    }
}
