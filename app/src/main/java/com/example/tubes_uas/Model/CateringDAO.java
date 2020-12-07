package com.example.tubes_uas.Model;

import com.google.gson.annotations.SerializedName;

public class CateringDAO {
    @SerializedName("id")
    private int id;

    @SerializedName("paket")
    private String paket;

    @SerializedName("hari")
    private String hari;

    @SerializedName("bulan")
    private String bulan;

    public CateringDAO(int id, String paket, String hari, String bulan) {
        this.id = id;
        this.paket = paket;
        this.hari = hari;
        this.bulan = bulan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaket() {
        return paket;
    }

    public void setPaket(String paket) {
        this.paket = paket;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }
}
