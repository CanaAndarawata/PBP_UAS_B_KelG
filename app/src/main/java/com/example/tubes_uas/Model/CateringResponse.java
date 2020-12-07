package com.example.tubes_uas.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CateringResponse {
    @SerializedName("data")
    @Expose
    private CateringDAO data;

    @SerializedName("message")
    @Expose
    private String message;

    public CateringDAO getData() {
        return data;
    }

    public void setData(CateringDAO data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
