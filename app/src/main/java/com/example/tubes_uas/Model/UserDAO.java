package com.example.tubes_uas.Model;

import com.google.gson.annotations.SerializedName;

public class UserDAO {
    @SerializedName("id")
    private int id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public UserDAO(int id,
                   String nama, String email, String password){
        this.id     = id;
        this.nama   = nama;
        this.email  = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
