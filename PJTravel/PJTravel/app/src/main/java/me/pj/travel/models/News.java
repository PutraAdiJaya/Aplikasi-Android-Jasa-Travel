package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 2/3/17.
 */

public class News {
    @SerializedName("id_news")
    private String id_news;
    @SerializedName("pelapor")
    private String pelapor;
    @SerializedName("judul")
    private String judul;
    @SerializedName("deskripsi")
    private String deskripsi;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("images")
    private String images;

    public News(){

    }
    public News(String id_news, String pelapor, String judul, String deskripsi, String tanggal, String images) {
        this.id_news = id_news;
        this.pelapor = pelapor;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.images = images;
    }

    public String getId_news() {
        return id_news;
    }

    public void setId_news(String id_news) {
        this.id_news = id_news;
    }

    public String getPelapor() {
        return pelapor;
    }

    public void setPelapor(String pelapor) {
        this.pelapor = pelapor;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

}