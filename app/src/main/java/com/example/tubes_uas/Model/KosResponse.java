package com.example.tubes_uas.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KosResponse {
    @SerializedName("data")
    @Expose
    private KosDAO data;

    @SerializedName("message")
    @Expose
    private String message;

    public KosDAO getData() {
        return data;
    }

    public void setData(KosDAO data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
