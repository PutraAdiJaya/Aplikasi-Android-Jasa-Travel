package me.pj.travel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import me.pj.travel.controller.ApiClient;
import me.pj.travel.controller.Preferences;
import me.pj.travel.interfece.ApiInterface;
import me.pj.travel.models.GsonAction;
import me.pj.travel.models.Pesanan;
import me.pj.travel.models.PesananRespon;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class PesananUserActivity extends AppCompatActivity {

    EditText ed_Nama,
            ed_alamat_tujuan,
            ed_tanggal,
            ed_kloter,
            ed_Tarif   ,
            ed_Pembayaran,ed_Driver  ;
    ApiInterface mApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Preferences.loadPreferences(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BatalPesan();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mApiInterface = ApiClient.getClient().create(ApiInterface.class);


        ed_Nama = findViewById(R.id.ed_Nama);
        ed_alamat_tujuan = findViewById(R.id.ed_alamat_tujuan);
        ed_tanggal = findViewById(R.id.ed_tanggal);
        ed_kloter = findViewById(R.id.ed_kloter);
        ed_Tarif   = findViewById(R.id.ed_Tarif);
        ed_Pembayaran = findViewById(R.id.ed_Pembayaran);
        ed_Driver = findViewById(R.id.ed_Driver);

        ed_Nama.setText("");
        ed_alamat_tujuan.setText("");
        ed_tanggal.setText("");
        ed_kloter.setText("");
        ed_Tarif.setText("");
        ed_Pembayaran.setText("");
        ed_Driver.setText("");


    }
/*KONFIRMASI BATAL*/
    private void BatalPesan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yakin Untuk Proses Pembatalan Pemesanan Data Anda?");
        // Set up the buttons
        builder.setPositiveButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SaveDataTask().execute();
                    }
                });

        builder.show();
    }
/*SIMPAN DATA*/
    public class SaveDataTask extends AsyncTask<Void, Void, Boolean> {

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;

        public SaveDataTask() {

            Preferences.resetAction(PesananUserActivity.this);
            dialog = new ProgressDialog(PesananUserActivity.this);
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            doSave();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return Preferences.IS_ACTION_OK;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            finish();
        }

        @Override
        protected void onCancelled() {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }
/*DATA REQUEST*/
    private void doSave() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);



        RequestBody id_pemesanan =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdPemesanan());
        RequestBody Status =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, "BATAL");

        Preferences.resetAction(this);
        Call<GsonAction> apiCall = mApiInterface.postPemesananBatal( id_pemesanan,Status );
        apiCall.enqueue(myRespon);


    }
/*DATA RESPON*/
    Callback myRespon = new Callback<GsonAction>() {
        @Override
        public void onResponse(Call<GsonAction> call, Response<GsonAction>
                response) {
            try {
                GsonAction resJSON;
                resJSON = response.body();
                Toast.makeText(PesananUserActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                Preferences.saveAction(PesananUserActivity.this);
                Log.i("MyAPP", response.body().toString());
            }catch (Exception ex){
                Toast.makeText(PesananUserActivity.this, "Proses Penyimpanan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
            } finally {

            }



        }

        @Override
        public void onFailure(Call<GsonAction> call, Throwable t) {
            Log.e("Retrofit Get", t.toString());
            Toast.makeText(PesananUserActivity.this, "Proses Penyimpanan Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
/*LOADA DATA DARI SERVER*/
    Pesanan CURRENT_DATA;
    private void loadData() {

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        RequestBody id_user =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM,Preferences.ID  );

        Preferences.resetAction(PesananUserActivity.this);
        CURRENT_DATA = new Pesanan();
        Call<PesananRespon> apiCall = mApiInterface.getPesanan(id_user);
        apiCall.enqueue(new Callback<PesananRespon>() {
            @Override
            public void onResponse(Call<PesananRespon> call, Response<PesananRespon>
                    response) {
                if (response.body().getStatus().equals( "OK"))
                {
                    if (response.body().getPesanan().isEmpty()){
                        Preferences.IS_ACTION_OK = false;
                        Toast.makeText(PesananUserActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Preferences.IS_ACTION_OK = true;
                        CURRENT_DATA = response.body().getPesanan().get(0);
                        setInputValue();
                    }
                    Preferences.saveAction(PesananUserActivity.this);
                }else {

                    Toast.makeText(PesananUserActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    finish();
                }

                Log.i("MyAPP",response.message() );
            }

            @Override
            public void onFailure(Call<PesananRespon> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });


    }

    private void setInputValue() {
        ed_Nama.setText(CURRENT_DATA.getNama());
        ed_alamat_tujuan.setText(CURRENT_DATA.getAlamatTujuan());
        ed_tanggal.setText(CURRENT_DATA.getTanggal());
        ed_kloter.setText(CURRENT_DATA.getJam());
        ed_Driver.setText(CURRENT_DATA.getDriver());
        ed_Tarif.setText(CURRENT_DATA.getTarif());
        ed_Pembayaran.setText(CURRENT_DATA.getmPembayaran());

        String webData =  "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head></head><body id=\"body\"   >"+
                "<img width=\"100%\" src=\""+ApiClient.WEB_URL+"uploads/"+CURRENT_DATA.getFoto()+"\">" +
                "</body></html>";

        WebView wv = findViewById(R.id.webView);
        wv.loadData(webData, "text/html", "UTF-8");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }
}
