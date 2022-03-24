package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by root on 2/3/17.
 */

public class GetNews {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<News> listDataNews;
    @SerializedName("message")
    String message;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void sfetMessage(String message) {
        this.message = message;
    }
    public List<News> getListDataNews() {
        return listDataNews;
    }
    public void setListDataNews(List<News> listDataNews) {
        this.listDataNews = listDataNews;
    }
}