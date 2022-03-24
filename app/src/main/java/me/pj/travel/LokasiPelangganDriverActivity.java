package me.pj.travel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LokasiPelangganDriverActivity extends AppCompatActivity {
    public static String ucfirst(String subject)
    {
        return Character.toUpperCase(subject.charAt(0)) + subject.substring(1);
    }
/*ADAPTER LIST*/
    public class JadwaLAdapter extends RecyclerView.Adapter<JadwaLAdapter.MyViewHolder>{
        List<Pesanan> mKontakList;

        public JadwaLAdapter(List <Pesanan> KontakList) {
            mKontakList = KontakList;
        }

        @Override
        public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news, parent, false);
            MyViewHolder mViewHolder = new MyViewHolder(mView);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder (MyViewHolder holder,final int position){
            holder.mTextViewId.setText(  mKontakList.get(position).getTanggal() + " | " + mKontakList.get(position).getTujuan() + " | " +mKontakList.get(position).getJam());
            holder.mTextViewNama.setText( mKontakList.get(position).getHp());
            holder.mTextViewNomor.setText(  ucfirst(mKontakList.get(position).getNama()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent mIntent = new Intent(view.getContext(), AddActivity.class);
                    mIntent.putExtra("Tanggal", mKontakList.get(position).getId_news());
                    mIntent.putExtra("Pelapor", mKontakList.get(position).getPelapor());
                    mIntent.putExtra("Judul", mKontakList.get(position).getJudul());
                    view.getContext().startActivity(mIntent);
                    */
                    showDataDetail(mKontakList.get(position));
                }
            });
        }

        @Override
        public int getItemCount () {
            return mKontakList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextViewId, mTextViewNama, mTextViewNomor;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTextViewId = (TextView) itemView.findViewById(R.id.tvId);
                mTextViewNama = (TextView) itemView.findViewById(R.id.tvNama);
                mTextViewNomor = (TextView) itemView.findViewById(R.id.tvNomor);
            }
        }
    }
/*TAMPIL DETAIL*/
    Pesanan CURRENT_DATA;
    public void showDataDetail(final Pesanan news){

        CURRENT_DATA = news;

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(news.getTujuan() );
        alert.setTitle( "Tujuan : "+ucfirst(   news.getTujuan()) );
        String webData =  "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head></head><body id=\"body\"   >"+
                "<h4><b>Nama : </b>"+news.getNama() +"</h4>" +
                "<h4><b>KTP : </b>"+news.getKTP() +"</h4>" +
                "<h4><b>Alamat : </b>"+news.getAlamatPenjemputan() +"</h4>" +
                "<h4><b>Tujuan : </b>"+news.getAlamatTujuan() +"</h4>" +
                "<h4><b>No.HP : </b>"+news.getHp() +"</h4>" +
                "<h4><b>Tanggal : </b>"+news.getTanggal() +"</h4>" +
                "<h4><b>Jam : </b>"+news.getJam() +"</h4>" +
                "<h4><b>No.Pol : </b>"+news.getNoPolisi() +"</h4>" +
                "<h4><b>Armada : </b>"+news.getJenisArmada() +"</h4>" +
                "<h4><b>No.Pol : </b>"+news.getNoPolisi() +"</h4>" +
                "<h4><b>Tarif : </b>"+news.getTarif() +"</h4>" +
                "<h4><b>Status : </b>"+news.getmStatus() +"</h4>" +
                "<h4><b>Pembayaran : </b>"+news.getmPembayaran() +"</h4>" +
                "</body></html>";

        WebView wv = new WebView(this);
        wv.loadData(webData, "text/html", "UTF-8");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Update Status Pembayaran", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                showStatusBayar();
            }
        });

        alert.show();
    }
/*TAMPIL KONFIRMASI UPDATE*/
    private void showStatusBayar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Status Pembayaran");
        builder.setNeutralButton("CLOSE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                });
        builder.setNegativeButton("SUDAH BAYAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CURRENT_DATA.setmPembayaran("SUDAH");
                        new SaveDataTask().execute();
                        dialog.cancel();

                    }
                });
        builder.setPositiveButton("BELUM DIBAYAR",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CURRENT_DATA.setmPembayaran("BELUM");
                        new SaveDataTask().execute();
                        dialog.cancel();

                    }
                });

        builder.show();

    }
/*DATA REQUEST*/
    private void kirimStatus() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        RequestBody idPemesanan =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdPemesanan());

        RequestBody Pembayaran =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getmPembayaran());

        Preferences.resetAction(LokasiPelangganDriverActivity.this);
        Call<GsonAction> apiCall = mApiInterface.bayarPemesanan(idPemesanan,Pembayaran);

        apiCall.enqueue(new Callback<GsonAction>() {
            @Override
            public void onResponse(Call<GsonAction> call, Response<GsonAction>
                    response) {
                try {
                    GsonAction resJSON;
                    resJSON = response.body();
                    Toast.makeText(LokasiPelangganDriverActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                    Preferences.saveAction(LokasiPelangganDriverActivity.this);
                    Log.i("MyAPP", response.body().toString());
                }catch (Exception ex){
                    Toast.makeText(LokasiPelangganDriverActivity.this, "Proses Penghapusan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
                } finally {

                }



            }

            @Override
            public void onFailure(Call<GsonAction> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(LokasiPelangganDriverActivity.this, "Proses Penyimpanan Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();
            }
        });
    }


/*KIRIM PERINTAH*/
    public class SaveDataTask extends AsyncTask<Void, Void, Boolean> {

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;

        public SaveDataTask() {

            Preferences.resetAction(LokasiPelangganDriverActivity.this);
            dialog = new ProgressDialog(LokasiPelangganDriverActivity.this);
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            kirimStatus();
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
            loadData();
        }

        @Override
        protected void onCancelled() {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                showADD();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mRecyclerView =  findViewById(R.id.recyclerView);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);



    }

    private void showADD() {
        startActivity(new Intent(this,InputJadwalAdminActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
/*LOAD DATA DARI SERVER*/
    private void loadData() {
        RequestBody Driver =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, Preferences.NAMA);

        Preferences.resetAction(LokasiPelangganDriverActivity.this);
        Call<PesananRespon> apiCall = mApiInterface.getPesananDriver(Driver);
        apiCall.enqueue(new Callback<PesananRespon>() {
            @Override
            public void onResponse(Call<PesananRespon> call, Response<PesananRespon>
                    response) {
                if (response.body().getStatus().equals( "OK"))
                {
                    List<Pesanan> DataList = response.body().getPesanan();
                    Log.d("Retrofit Get", "Jumlah data News: " +
                            String.valueOf(DataList.size()));
                    mAdapter = new JadwaLAdapter(DataList);
                    mRecyclerView.setAdapter(mAdapter);
                }

                Log.i("MyAPP",response.message() );
            }

            @Override
            public void onFailure(Call<PesananRespon> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
