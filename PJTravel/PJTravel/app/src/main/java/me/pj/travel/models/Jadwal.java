
package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Jadwal {

    @SerializedName("Driver")
    private String mDriver;
    @SerializedName("Foto")
    private String mFoto;
    @SerializedName("id_jadwal")
    private String mIdJadwal;
    @SerializedName("Jam")
    private String mJam;
    @SerializedName("Jenis_Armada")
    private String mJenisArmada;
    @SerializedName("Kapasitas")
    private String mKapasitas;
    @SerializedName("Merk")
    private String mMerk;
    @SerializedName("No_Polisi")
    private String mNoPolisi;
    @SerializedName("Tarif")
    private String mTarif;
    @SerializedName("Tujuan")
    private String mTujuan;
    @SerializedName("Lat")
    private String mLat;
    @SerializedName("Lng")
    private String mLng;

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLng() {
        return mLng;
    }

    public void setmLng(String mLng) {
        this.mLng = mLng;
    }

    public String getDriver() {
        return mDriver;
    }

    public void setDriver(String driver) {
        mDriver = driver;
    }

    public String getFoto() {
        return mFoto;
    }

    public void setFoto(String foto) {
        mFoto = foto;
    }

    public String getIdJadwal() {
        return mIdJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        mIdJadwal = idJadwal;
    }

    public String getJam() {
        return mJam;
    }

    public void setJam(String jam) {
        mJam = jam;
    }

    public String getJenisArmada() {
        return mJenisArmada;
    }

    public void setJenisArmada(String jenisArmada) {
        mJenisArmada = jenisArmada;
    }

    public String getKapasitas() {
        return mKapasitas;
    }

    public void setKapasitas(String kapasitas) {
        mKapasitas = kapasitas;
    }

    public String getMerk() {
        return mMerk;
    }

    public void setMerk(String merk) {
        mMerk = merk;
    }

    public String getNoPolisi() {
        return mNoPolisi;
    }

    public void setNoPolisi(String noPolisi) {
        mNoPolisi = noPolisi;
    }

    public String getTarif() {
        return mTarif;
    }

    public void setTarif(String tarif) {
        mTarif = tarif;
    }

    public String getTujuan() {
        return mTujuan;
    }

    public void setTujuan(String tujuan) {
        mTujuan = tujuan;
    }

    @Override
    public String toString() {
        return "Jadwal{" +
                "mDriver='" + mDriver + '\'' +
                ", mFoto='" + mFoto + '\'' +
                ", mIdJadwal='" + mIdJadwal + '\'' +
                ", mJam='" + mJam + '\'' +
                ", mJenisArmada='" + mJenisArmada + '\'' +
                ", mKapasitas='" + mKapasitas + '\'' +
                ", mMerk='" + mMerk + '\'' +
                ", mNoPolisi='" + mNoPolisi + '\'' +
                ", mTarif='" + mTarif + '\'' +
                ", mTujuan='" + mTujuan + '\'' +
                '}';
    }
    public String[] toList(){
        return new String[]{
                mDriver ,
                mFoto ,
                mIdJadwal ,
                mJam ,
                mJenisArmada ,
                mKapasitas ,
                mMerk ,
                mNoPolisi ,
                mTarif ,
                mTujuan };
    }
}
