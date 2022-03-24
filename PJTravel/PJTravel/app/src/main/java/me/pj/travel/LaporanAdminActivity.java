package me.pj.travel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.pj.travel.controller.ApiClient;
import me.pj.travel.controller.Preferences;
import me.pj.travel.interfece.ApiInterface;
import me.pj.travel.models.GsonAction;
import me.pj.travel.models.Jadwal;
import me.pj.travel.models.JadwalResponse;
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

public class LaporanAdminActivity extends AppCompatActivity {
    public static String ucfirst(String subject)
    {
        return Character.toUpperCase(subject.charAt(0)) + subject.substring(1);
    }

    /*ADAPTER JADWAL*/
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
    /*TAMPIL DIALOG DETAIL*/
    public void showDataDetail(final Pesanan news){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(news.getTujuan() );
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
        alert.show();
    }


    Jadwal CURRENT_DATA;


    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_admin);
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

        Call<PesananRespon> apiCall = mApiInterface.getLaporan();
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
