package com.example.tubes_uas.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllUserResponse {
    @SerializedName("data")
    @Expose
    private List<UserDAO> data;

    public List<UserDAO> getUsers() {
        return data;
    }

//    public List<UserDAO> getEmail() {
//        return email;
//    }

    public void setUsers(List<UserDAO> users) {
        this.data = data;
    }
}
