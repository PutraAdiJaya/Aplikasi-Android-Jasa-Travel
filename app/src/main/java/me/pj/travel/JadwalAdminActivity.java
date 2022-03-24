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
import me.pj.travel.models.Jadwal;
import me.pj.travel.models.JadwalResponse;
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

public class JadwalAdminActivity extends AppCompatActivity {

/*ADAPTER JADWAL*/
    public class JadwaLAdapter extends RecyclerView.Adapter<JadwaLAdapter.MyViewHolder>{
        List<Jadwal> mKontakList;

        public JadwaLAdapter(List <Jadwal> KontakList) {
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
            holder.mTextViewId.setText(  mKontakList.get(position).getDriver() );
            holder.mTextViewNama.setText( mKontakList.get(position).getJam());
            holder.mTextViewNomor.setText(  mKontakList.get(position).getTujuan());
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
    public void showDataDetail(final Jadwal news){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(news.getTujuan() );
        String webData =  "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head></head><body id=\"body\"   >"+
                "<img width=\"100%\" src=\""+ApiClient.WEB_URL+"uploads/"+news.getFoto()+"\">" +
                "<h4><b>Jam : </b>"+news.getJam() +"</h4>" +
                "<h4><b>Driver : </b>"+news.getDriver() +"</h4>" +
                "<h4><b>Tarif : </b>"+news.getTarif() +"</h4>" +
                "<h4><b>Kapasitas : </b>"+news.getKapasitas() +"</h4>" +
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
/*KIRIM DATA YANG AKAN DIEDIT*/
    private void doEdit(Jadwal news) {
        Intent i = new Intent(JadwalAdminActivity.this,InputJadwalAdminActivity.class);
        i.putExtra("MODE",1);
        i.putExtra("DATA",news.toList());
        startActivity(i);
    }

    Jadwal CURRENT_DATA;
    /*KONFIRMASI PENGHAPUSAN*/
    private void ConfirmHapus(final Jadwal news) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yakin untuk menghapus data ?");
        // Set up the buttons
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

/*PROSES KIRIM DATA KE SERVER*/
    public class SaveDataTask extends AsyncTask<Void, Void, Boolean> {

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;

        public SaveDataTask() {

            Preferences.resetAction(JadwalAdminActivity.this);
            dialog = new ProgressDialog(JadwalAdminActivity.this);
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
/*REQUEST DATA PENGHAPUSAN*/
    private void doDelete() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        RequestBody id_jadwal =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdJadwal());

        Preferences.resetAction(JadwalAdminActivity.this);
        Call<GsonAction> apiCall = mApiInterface.deleteJadwal(id_jadwal);

        apiCall.enqueue(new Callback<GsonAction>() {
            @Override
            public void onResponse(Call<GsonAction> call, Response<GsonAction>
                    response) {
                try {
                    GsonAction resJSON;
                    resJSON = response.body();
                    Toast.makeText(JadwalAdminActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                    Preferences.saveAction(JadwalAdminActivity.this);
                    Log.i("MyAPP", response.body().toString());
                }catch (Exception ex){
                    Toast.makeText(JadwalAdminActivity.this, "Proses Penghapusan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
                } finally {

                }



            }

            @Override
            public void onFailure(Call<GsonAction> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(JadwalAdminActivity.this, "Proses Penyimpanan Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();
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
    /*TAMPIL HALAMAAN TAMBAH*/
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

        Call<JadwalResponse> apiCall = mApiInterface.getJadwal();
        apiCall.enqueue(new Callback<JadwalResponse>() {
            @Override
            public void onResponse(Call<JadwalResponse> call, Response<JadwalResponse>
                    response) {
                if (response.body().getStatus().equals( "OK"))
                {
                    List<Jadwal> DataList = response.body().getJadwal();
                    Log.d("Retrofit Get", "Jumlah data News: " +
                            String.valueOf(DataList.size()));
                    mAdapter = new JadwaLAdapter(DataList);
                    mRecyclerView.setAdapter(mAdapter);
                }

                Log.i("MyAPP",response.message() );
            }

            @Override
            public void onFailure(Call<JadwalResponse> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
