package com.example.tubes_uas.Model;

import com.google.gson.annotations.SerializedName;

public class KosDAO {
    @SerializedName("id")
    private int id;

    @SerializedName("jenis")
    private String jenis;

    @SerializedName("fasilitas")
    private String fasilitas;

    @SerializedName("lama")
    private String lama;

    public KosDAO(int id, String jenis, String fasilitas, String lama) {
        this.id = id;
        this.jenis = jenis;
        this.fasilitas = fasilitas;
        this.lama = lama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public String getLama() {
        return lama;
    }

    public void setLama(String lama) {
        this.lama = lama;
    }
}
