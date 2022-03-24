
package me.pj.travel.models;

import com.google.gson.annotations.SerializedName;

public class Pesanan {

    @SerializedName("alamat")
    private String mAlamat;
    @SerializedName("alamat_penjemputan")
    private String mAlamatPenjemputan;
    @SerializedName("alamat_tujuan")
    private String mAlamatTujuan;
    @SerializedName("Driver")
    private String mDriver;
    @SerializedName("Foto")
    private String mFoto;
    @SerializedName("Hp")
    private String mHp;
    @SerializedName("id_jadwal")
    private String mIdJadwal;
    @SerializedName("id_pemesanan")
    private String mIdPemesanan;
    @SerializedName("id_user")
    private String mIdUser;
    @SerializedName("Jam")
    private String mJam;
    @SerializedName("Jenis_Armada")
    private String mJenisArmada;
    @SerializedName("KTP")
    private String mKTP;
    @SerializedName("Kapasitas")
    private String mKapasitas;
    @SerializedName("lat")
    private String mLat;
    @SerializedName("lng")
    private String mLng;
    @SerializedName("Merk")
    private String mMerk;
    @SerializedName("Nama")
    private String mNama;
    @SerializedName("No_Polisi")
    private String mNoPolisi;
    @SerializedName("tanggal")
    private String mTanggal;
    @SerializedName("Tarif")
    private String mTarif;
    @SerializedName("Tujuan")
    private String mTujuan;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("Status")
    private String mStatus;
    @SerializedName("Pembayaran")
    private String mPembayaran;

    public String[] toList(){
        return new String[]{
                mNama ,
                mKTP ,
                mHp ,
                mAlamatPenjemputan ,
                mAlamatTujuan ,
                mTanggal ,
                mIdJadwal ,
                mIdPemesanan ,
                mStatus,
                mPembayaran,
                mIdUser   };
    }
    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmPembayaran() {
        return mPembayaran;
    }

    public void setmPembayaran(String mPembayaran) {
        this.mPembayaran = mPembayaran;
    }

    public String getAlamat() {
        return mAlamat;
    }

    public void setAlamat(String alamat) {
        mAlamat = alamat;
    }

    public String getAlamatPenjemputan() {
        return mAlamatPenjemputan;
    }

    public void setAlamatPenjemputan(String alamatPenjemputan) {
        mAlamatPenjemputan = alamatPenjemputan;
    }

    public String getAlamatTujuan() {
        return mAlamatTujuan;
    }

    public void setAlamatTujuan(String alamatTujuan) {
        mAlamatTujuan = alamatTujuan;
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

    public String getHp() {
        return mHp;
    }

    public void setHp(String hp) {
        mHp = hp;
    }

    public String getIdJadwal() {
        return mIdJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        mIdJadwal = idJadwal;
    }

    public String getIdPemesanan() {
        return mIdPemesanan;
    }

    public void setIdPemesanan(String idPemesanan) {
        mIdPemesanan = idPemesanan;
    }

    public String getIdUser() {
        return mIdUser;
    }

    public void setIdUser(String idUser) {
        mIdUser = idUser;
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

    public String getKTP() {
        return mKTP;
    }

    public void setKTP(String kTP) {
        mKTP = kTP;
    }

    public String getKapasitas() {
        return mKapasitas;
    }

    public void setKapasitas(String kapasitas) {
        mKapasitas = kapasitas;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getLng() {
        return mLng;
    }

    public void setLng(String lng) {
        mLng = lng;
    }

    public String getMerk() {
        return mMerk;
    }

    public void setMerk(String merk) {
        mMerk = merk;
    }

    public String getNama() {
        return mNama;
    }

    public void setNama(String nama) {
        mNama = nama;
    }

    public String getNoPolisi() {
        return mNoPolisi;
    }

    public void setNoPolisi(String noPolisi) {
        mNoPolisi = noPolisi;
    }

    public String getTanggal() {
        return mTanggal;
    }

    public void setTanggal(String tanggal) {
        mTanggal = tanggal;
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

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}
