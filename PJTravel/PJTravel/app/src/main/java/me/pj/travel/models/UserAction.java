package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/*RESPON DARI REQUEST*/
public class UserAction {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Users> list;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Users> getList() {
        return list;
    }

    public void setList(List<Users> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
