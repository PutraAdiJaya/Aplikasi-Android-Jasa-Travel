package me.pj.travel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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

public class PesananAdminActivity extends AppCompatActivity {

    public static String ucfirst(String subject)
    {
        return Character.toUpperCase(subject.charAt(0)) + subject.substring(1);
    }
/*ADAPTER DATA*/
    public class PesanaAdapter extends RecyclerView.Adapter<PesanaAdapter.MyViewHolder>{
        List<Pesanan> mKontakList;

        public PesanaAdapter(List <Pesanan> KontakList) {
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
    public void showDataDetail(final Pesanan news){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle( "Tujuan : "+ucfirst(   news.getTujuan()) );
        String webData =  "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head></head><body id=\"body\"   >"+
                "<h4><b>Nama : </b>"+news.getNama() +"</h4>" +
                "<h4><b>Alamat : </b>"+news.getAlamatPenjemputan() +"</h4>" +
                "<h4><b>Tujuan : </b>"+news.getAlamatTujuan() +"</h4>" +
                "<h4><b>No.HP : </b>"+news.getHp() +"</h4>" +
                "<h4><b>Tanggal : </b>"+news.getTanggal() +"</h4>" +
                "<h4><b>Jam : </b>"+news.getJam() +"</h4>" +
                "<h4><b>Driver : </b>"+news.getDriver() +"</h4>" +
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
        alert.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                doEdit(news);
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                ConfirmHapus(news);
            }
        });
        alert.show();
    }
/*EDIT DATA*/
    private void doEdit(Pesanan news) {
        Intent i = new Intent(PesananAdminActivity.this,DetailPesananAdminActivity.class);
        i.putExtra("MODE",1);
        i.putExtra("DATA",news.toList());
        startActivity(i);
    }
/*KONFIRMASI PENGHAPUSAN*/
    Pesanan CURRENT_DATA;
    private void ConfirmHapus(final Pesanan news) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yakin untuk menghapus data ?");
        builder.setNegativeButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        CURRENT_DATA = news;
                        new SaveDataTask().execute();

                    }
                });
        builder.setPositiveButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

        builder.show();
    }

/*KIRIM DATA KE SERVER*/
    public class SaveDataTask extends AsyncTask<Void, Void, Boolean> {

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;

        public SaveDataTask() {

            Preferences.resetAction(PesananAdminActivity.this);
            dialog = new ProgressDialog(PesananAdminActivity.this);
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            doDelete();
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
/*REQUEST HAPUS*/
    private void doDelete() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        RequestBody idPemesanan =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdPemesanan());

        Preferences.resetAction(PesananAdminActivity.this);
        Call<GsonAction> apiCall = mApiInterface.deletePemesanan(idPemesanan);

        apiCall.enqueue(new Callback<GsonAction>() {
            @Override
            public void onResponse(Call<GsonAction> call, Response<GsonAction>
                    response) {
                try {
                    GsonAction resJSON;
                    resJSON = response.body();
                    Toast.makeText(PesananAdminActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                    Preferences.saveAction(PesananAdminActivity.this);
                    Log.i("MyAPP", response.body().toString());
                }catch (Exception ex){
                    Toast.makeText(PesananAdminActivity.this, "Proses Penghapusan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
                } finally {

                }



            }

            @Override
            public void onFailure(Call<GsonAction> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(PesananAdminActivity.this, "Proses Penyimpanan Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();
            }
        });

    }

    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mRecyclerView =  findViewById(R.id.recyclerView);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);



    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
/*LOAD DATA DARI SERVER*/
    private void loadData() {

        Call<PesananRespon> apiCall = mApiInterface.getPesan();
        apiCall.enqueue(new Callback<PesananRespon>() {
            @Override
            public void onResponse(Call<PesananRespon> call, Response<PesananRespon>
                    response) {
                if (response.body().getStatus().equals( "OK"))
                {
                    List<Pesanan> DataList = response.body().getPesanan();
                    Log.d("Retrofit Get", "Jumlah data News: " +
                            String.valueOf(DataList.size()));
                    mAdapter = new PesanaAdapter(DataList);
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
