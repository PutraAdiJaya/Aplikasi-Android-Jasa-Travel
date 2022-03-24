
package me.pj.travel.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;
public class KotaResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Kota> mKota;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Kota> getResult() {
        return mKota;
    }

    public void setResult(List<Kota> kota) {
        mKota = kota;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
