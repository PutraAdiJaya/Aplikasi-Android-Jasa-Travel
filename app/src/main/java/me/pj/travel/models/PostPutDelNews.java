package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PostPutDelNews {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List lsRespon;
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
    public void setMessage(String message) {
        this.message = message;
    }

}