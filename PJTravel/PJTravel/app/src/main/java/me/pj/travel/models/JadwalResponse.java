
package me.pj.travel.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class JadwalResponse {

    @SerializedName("result")
    private List<Jadwal> mJadwal;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private String mStatus;

    public List<Jadwal> getJadwal() {
        return mJadwal;
    }

    public void setJadwal(List<Jadwal> jadwal) {
        mJadwal = jadwal;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
