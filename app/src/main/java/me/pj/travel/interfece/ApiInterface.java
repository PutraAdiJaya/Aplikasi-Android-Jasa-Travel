package me.pj.travel.interfece;



import me.pj.travel.models.GsonAction;
import me.pj.travel.models.JadwalResponse;
import me.pj.travel.models.KotaResponse;
import me.pj.travel.models.PesananRespon;
import me.pj.travel.models.Users;
import me.pj.travel.models.UserResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiInterface {

    /*PERINTAH UNTUK DIKIRIM KE SERVER*/

    @Headers({
            "Accept: application/json"
    })


    @GET("user/")
    Call<Users> getUser();


    @Multipart
    @POST("user/")
    Call<GsonAction> postUser(
            @Part("nama") RequestBody nama,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("hp") RequestBody hp,
            @Part("alamat") RequestBody alamat,
            @Part("akses") RequestBody akses);


    @Multipart
    @POST("user/login")
    Call<UserResponse> loginUser(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password );


    @FormUrlEncoded
    @PUT("user")
    Call<UserResponse> putUser(
            @Part("Id_user") RequestBody Id_user,
            @Part("nama") RequestBody nama,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("hp") RequestBody hp,
            @Part("alamat") RequestBody alamat,
            @Part("akses") RequestBody akses);


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "user", hasBody = true)
    Call<UserResponse> deleteUser(@Field("username") String username);


    @GET("user/driver")
    Call<UserResponse> getDriver();

    @GET("kota")
    Call<KotaResponse> getKota();
/*==============================================END USER API==================================================*/

    @Multipart
    @POST("pemesanan/edit")
    Call<GsonAction> bayarPemesanan(@Part("id_pemesanan") RequestBody id_pemesanan,
                                    @Part("Pembayaran") RequestBody Pembayaran
    );
    @Multipart
    @POST("pemesanan/edit")
    Call<GsonAction> statusPemesanan(@Part("id_pemesanan") RequestBody id_pemesanan,
                                     @Part("Status") RequestBody Status
    );

    @GET("pemesanan/laporan")
    Call<PesananRespon> getLaporan();

    @Multipart
    @POST("pemesanan/driver")
    Call<PesananRespon> getPesananDriver(
            @Part("Driver") RequestBody Driver);

    @GET("pemesanan/")
    Call<PesananRespon> getPesan();

    @Multipart
    @POST("pemesanan/user")
    Call<PesananRespon> getPesanan(
            @Part("id_user") RequestBody id_user);
    @Multipart
    @POST("pemesanan/")
    Call<GsonAction> postPemesanan(
            @Part("Nama") RequestBody Nama,
            @Part("KTP") RequestBody KTP,
            @Part("Hp") RequestBody Hp,
            @Part("tanggal") RequestBody tanggal,
            @Part("alamat_tujuan") RequestBody alamat_tujuan,
            @Part("alamat_penjemputan") RequestBody alamat_penjempputan,
            @Part("id_jadwal") RequestBody id_jadwal,
            @Part("id_user") RequestBody id_user ,
            @Part("lat") RequestBody lat ,
            @Part("lng") RequestBody lng);

    @Multipart
    @POST("pemesanan/edit")
    Call<GsonAction> posteditPemesanan(
            @Part("Nama") RequestBody Nama,
            @Part("KTP") RequestBody KTP,
            @Part("Hp") RequestBody Hp,
            @Part("tanggal") RequestBody tanggal,
            @Part("alamat_tujuan") RequestBody alamat_tujuan,
            @Part("alamat_penjemputan") RequestBody alamat_penjempputan,
            @Part("id_jadwal") RequestBody id_jadwal,
            @Part("id_user") RequestBody id_user,
            @Part("id_pemesanan") RequestBody id_pemesanan,
            @Part("Status") RequestBody Status,
            @Part("Pembayaran") RequestBody Pembayaran,
            @Part("lat") RequestBody lat ,
            @Part("lng") RequestBody lng );

    @Multipart
    @POST("pemesanan/batal")
    Call<GsonAction> postPemesananBatal(
            @Part("id_pemesanan") RequestBody id_pemesanan,
            @Part("Status") RequestBody Status);

    @GET("jadwal/")
    Call<JadwalResponse> getJadwal();
    @Multipart
    @POST("jadwal/")
    Call<GsonAction> postJadwal(
            @Part MultipartBody.Part file,
            @Part("Jenis_Armada") RequestBody Jenis_Armada,
            @Part("Merk") RequestBody Merk,
            @Part("No_Polisi") RequestBody No_Polisi,
            @Part("Kapasitas") RequestBody Kapasitas,
            @Part("Tujuan") RequestBody Tujuan,
            @Part("Tarif") RequestBody Tarif,
            @Part("Jam") RequestBody Jam,
            @Part("Driver") RequestBody Driver,
            @Part("Foto") RequestBody Foto);

    @Multipart
    @POST("pemesanan/delete")
    Call<GsonAction> deletePemesanan(@Part("id_pemesanan") RequestBody id_pemesanan);

    /*==============================================END PEMESANAN API==================================================*/

    @Multipart
    @POST("jadwal/edit")
    Call<GsonAction> editJadwal(
            @Part MultipartBody.Part file,
            @Part("Jenis_Armada") RequestBody Jenis_Armada,
            @Part("Merk") RequestBody Merk,
            @Part("No_Polisi") RequestBody No_Polisi,
            @Part("Kapasitas") RequestBody Kapasitas,
            @Part("Tujuan") RequestBody Tujuan,
            @Part("Tarif") RequestBody Tarif,
            @Part("Jam") RequestBody Jam,
            @Part("Driver") RequestBody Driver,
            @Part("Foto") RequestBody Fotor,
            @Part("id_jadwal") RequestBody id_jadwal);

    @Multipart
    @POST("jadwal/delete")
    Call<GsonAction> deleteJadwal(@Part("id_jadwal") RequestBody id_jadwal);


    /*==============================================END JADWAL API==================================================*/


}