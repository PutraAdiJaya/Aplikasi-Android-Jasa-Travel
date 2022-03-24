
package me.pj.travel.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;
 
public class PesananRespon {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("result")
    private List<Pesanan> mPesanan;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Pesanan> getPesanan() {
        return mPesanan;
    }

    public void setPesanan(List<Pesanan> pesanan) {
        mPesanan = pesanan;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
