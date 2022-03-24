
package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("akses")
    private String mAkses;
    @SerializedName("alamat")
    private String mAlamat;
    @SerializedName("hp")
    private String mHp;
    @SerializedName("Id_user")
    private String mIdUser;
    @SerializedName("nama")
    private String mNama;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("username")
    private String mUsername;

    public String getAkses() {
        return mAkses;
    }

    public void setAkses(String akses) {
        mAkses = akses;
    }

    public String getAlamat() {
        return mAlamat;
    }

    public void setAlamat(String alamat) {
        mAlamat = alamat;
    }

    public String getHp() {
        return mHp;
    }

    public void setHp(String hp) {
        mHp = hp;
    }

    public String getIdUser() {
        return mIdUser;
    }

    public void setIdUser(String idUser) {
        mIdUser = idUser;
    }

    public String getNama() {
        return mNama;
    }

    public void setNama(String nama) {
        mNama = nama;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
