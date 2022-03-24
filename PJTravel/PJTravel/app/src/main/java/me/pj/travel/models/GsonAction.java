package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonAction {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List list;
    @Override
    public String toString() {
        return "GsonAction{" +
                "status='" + status + '\'' +
                ", list=" + list +
                ", message='" + message + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    String message;



}
