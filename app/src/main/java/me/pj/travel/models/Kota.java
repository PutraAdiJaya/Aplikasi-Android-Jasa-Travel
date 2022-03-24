
package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

public class Kota {

    @SerializedName("id_kota")
    private String mIdKota;
    @SerializedName("kota")
    private String mKota;

    public String getIdKota() {
        return mIdKota;
    }

    public void setIdKota(String idKota) {
        mIdKota = idKota;
    }

    public String getKota() {
        return mKota;
    }

    public void setKota(String kota) {
        mKota = kota;
    }

}
