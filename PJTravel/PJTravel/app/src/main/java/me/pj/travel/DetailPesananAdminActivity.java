package me.pj.travel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
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
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailPesananAdminActivity extends AppCompatActivity {

/*ADAPTER TAMPIL JADAWLA*/
    public class JadwaLAdapter extends RecyclerView.Adapter<JadwaLAdapter.MyViewHolder>{
        List<Jadwal> mKontakList;

        public int lastSelectedPosition = -1;
        public JadwaLAdapter(List <Jadwal> KontakList) {
            mKontakList = KontakList;
        }

        public void setLastSelectedPosition(int lastSelectedPosition) {
            this.lastSelectedPosition = lastSelectedPosition;
        }

        @Override
        public JadwaLAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_cek, parent, false);
            JadwaLAdapter.MyViewHolder mViewHolder = new JadwaLAdapter.MyViewHolder(mView);

            return mViewHolder;
        }

        @Override
        public void onBindViewHolder (JadwaLAdapter.MyViewHolder holder, final int position){
            holder.mTextViewId.setText(  mKontakList.get(position).getDriver() );
            holder.mTextViewNama.setText("Rp. "+ mKontakList.get(position).getTarif() +", Jam "+ mKontakList.get(position).getJam());
            holder.mTextViewNomor.setText(  mKontakList.get(position).getTujuan());

            holder.mRadioPilih.setChecked(lastSelectedPosition == position);
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
            public RadioButton mRadioPilih;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTextViewId = (TextView) itemView.findViewById(R.id.tvId);
                mTextViewNama = (TextView) itemView.findViewById(R.id.tvNama);
                mTextViewNomor = (TextView) itemView.findViewById(R.id.tvNomor);
                mRadioPilih  = (RadioButton) itemView.findViewById(R.id.radioButton);
                mRadioPilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        notifyDataSetChanged();

                      /*  Toast.makeText(DetailPesananAdminActivity.this,
                                "selected offer is " +mKontakList.get(lastSelectedPosition).getTujuan(),
                                Toast.LENGTH_LONG).show();*/
                        ID_JADWAL = mKontakList.get(lastSelectedPosition).getIdJadwal();
                        IDX_JADWAL = lastSelectedPosition;
                    }
                });
            }
        }
    }
/*TAMPIL DETEAIL DIALOG*/
    int IDX_JADWAL = -1;
    public void showDataDetail(final Jadwal news){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(news.getTujuan() );
        String webData =  "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head></head><body id=\"body\"   >"+
                "<img width=\"100%\" src=\""+ ApiClient.WEB_URL+"uploads/"+news.getFoto()+"\">" +
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
        alert.show();
    }
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText ed_tanggal;
    private ImageButton btDatePicker;
    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    EditText  ed_alamat_tujuan,
            ed_alamat_penjemputan,
            ed_Nama,
            ed_KTP,
            ed_Hp  ;
    Spinner sp_status_pembayaran, sp_status_pesanan;
/*MEDAPATKAN LOKASI GPS*/
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if (address !=null) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Preferences.loadPreferences(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpanData();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp_status_pembayaran = findViewById(R.id.sp_status_pembayaran);
        sp_status_pesanan = findViewById(R.id.sp_status_pesanan);

        List listString = new ArrayList<>();
        listString.add("BELUM");
        listString.add("LUNAS");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DetailPesananAdminActivity.this,  R.layout.spiner_item, listString);
        sp_status_pembayaran.setAdapter(dataAdapter);

        listString = new ArrayList<>();
        listString.add("VALID");
        listString.add("BARU");
        listString.add("BATAL");
        ArrayAdapter<String> dataAdapter_pesanan = new ArrayAdapter<String>(DetailPesananAdminActivity.this,  R.layout.spiner_item, listString);
        sp_status_pesanan.setAdapter(dataAdapter_pesanan);


        mRecyclerView =  findViewById(R.id.recyclerView);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


        ed_alamat_tujuan =findViewById(R.id.edTujuan);
        ed_alamat_penjemputan =findViewById(R.id.edAlamat);
        ed_Nama =findViewById(R.id.edNama);
        ed_KTP =findViewById(R.id.edKtp);
        ed_Hp =findViewById(R.id.edHP);

        ed_tanggal = findViewById(R.id.edTanggal);
        ed_tanggal.setKeyListener(null);
        btDatePicker =   findViewById(R.id.ibTanggal);
        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(view);
            }
        });
        CURRENT_DATA = new Pesanan();
        ID_JADWAL = "";

        MODE = getIntent().getIntExtra("MODE",-1);

        if (MODE!=-1) {
            String[] DATA = getIntent().getStringArrayExtra("DATA");
            ed_alamat_tujuan.setText(DATA[4]);
            ed_alamat_penjemputan.setText(DATA[3]);
            ed_Nama.setText(DATA[0]);
            ed_KTP.setText(DATA[1]);
            ed_Hp.setText(DATA[2]);
            ed_tanggal.setText(DATA[5]);
            ID_JADWAL = (DATA[6]);
            CURRENT_DATA.setIdJadwal(DATA[6]);
            CURRENT_DATA.setTanggal(DATA[5]);
            CURRENT_DATA.setNama(DATA[0]);
            CURRENT_DATA.setKTP(DATA[1]);
            CURRENT_DATA.setHp(DATA[2]);
            CURRENT_DATA.setAlamatPenjemputan(DATA[3]);
            CURRENT_DATA.setTujuan(DATA[4]);
            CURRENT_DATA.setmStatus(DATA[8]);
            CURRENT_DATA.setIdPemesanan(DATA[7]);
            CURRENT_DATA.setmPembayaran(DATA[9]);
            CURRENT_DATA.setIdUser(DATA[10]);
            String[] TanggalStr = DATA[5].split("-");
            ed_tanggal.setText(TanggalStr[2]+"-"+ TanggalStr[1]+"-"+ TanggalStr[0] );

            int spinnerPosition = dataAdapter.getPosition(CURRENT_DATA.getmPembayaran());
            sp_status_pembayaran.setSelection(spinnerPosition);


            spinnerPosition = dataAdapter_pesanan.getPosition(CURRENT_DATA.getmStatus());
            sp_status_pesanan.setSelection(spinnerPosition);


            loadData();

        } else {

          finish();

        }



    }
    int MODE;
    String ID_JADWAL = "";
    Pesanan CURRENT_DATA;
    /*VALIDASI INPUT DATA*/
    private void SimpanData() {

        // Store values at the time of the login attempt.
        final String  alamat_tujuan = ed_alamat_tujuan.getText().toString();
        final String alamat_penjempputan = ed_alamat_penjemputan.getText().toString();
        String  Nama = ed_Nama.getText().toString();
        String  KTP = ed_KTP.getText().toString();
        String  Hp = ed_Hp.getText().toString();
        String  tanggal = ed_tanggal.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(alamat_tujuan)) {
            ed_alamat_tujuan.setError(getString(R.string.error_field_required));
            focusView = ed_alamat_tujuan;
            cancel = true;
        }

        if (TextUtils.isEmpty(alamat_penjempputan)  ) {
            ed_alamat_penjemputan.setError(getString(R.string.error_incorrect_password));
            focusView = ed_alamat_penjemputan;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(Nama)) {
            ed_Nama.setError(getString(R.string.error_field_required));
            focusView = ed_Nama;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(KTP)) {
            ed_KTP.setError(getString(R.string.error_field_required));
            focusView = ed_KTP;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(Hp)) {
            ed_Hp.setError(getString(R.string.error_field_required));
            focusView = ed_Hp;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(tanggal)) {
            ed_tanggal.setError(getString(R.string.error_field_required));
            focusView = ed_tanggal;
            cancel = true;
        }
        LatLng lokasi = getLocationFromAddress(DetailPesananAdminActivity.this, alamat_penjempputan +", "+ DataListJADWAL.get(IDX_JADWAL).getTujuan()+", indonesia");

        if (lokasi==null){
            cancel=true;
            Toast.makeText(DetailPesananAdminActivity.this,"Isikan alamat dengan benar, alamat tidak ditemukan di peta google..",Toast.LENGTH_LONG).show();
            return;
        }

        if (ID_JADWAL.trim().length()==0){
            cancel=true;
            Toast.makeText(DetailPesananAdminActivity.this,"Silahkan Pilih Jadwal Pemberangkatan..",Toast.LENGTH_LONG).show();
            return;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            String[] atanggal = tanggal.split("-");
            tanggal = atanggal[2]+"-"+ atanggal[1]+"-"+ atanggal[0];


            CURRENT_DATA.setAlamatTujuan(alamat_tujuan);
            CURRENT_DATA.setAlamatPenjemputan(alamat_penjempputan);
            CURRENT_DATA.setNama(Nama);
            CURRENT_DATA.setKTP(KTP);
            CURRENT_DATA.setHp(Hp);
            CURRENT_DATA.setTanggal(tanggal);
            CURRENT_DATA.setIdJadwal(ID_JADWAL);
            CURRENT_DATA.setmStatus(sp_status_pesanan.getSelectedItem().toString());
            CURRENT_DATA.setmPembayaran(sp_status_pembayaran.getSelectedItem().toString());
            CURRENT_DATA.setLat(String.valueOf(  lokasi.latitude));
            CURRENT_DATA.setLng(String.valueOf(  lokasi.longitude));


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yakin Untuk Proses Penyimpanan Data Anda?");
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
                            new  SaveDataTask().execute();
                        }
                    });

            builder.show();


        }


    }
/*PROSES KIRIM DATA*/
    public class SaveDataTask extends AsyncTask<Void, Void, Boolean> {

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;

        public SaveDataTask() {

            Preferences.resetAction(DetailPesananAdminActivity.this);
            dialog = new ProgressDialog(DetailPesananAdminActivity.this);
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
/*PEMBENTUKAN REQUEST KE SERVER*/
    private void doSave() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);



        RequestBody Nama =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getNama());

        RequestBody KTP =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getKTP());

        RequestBody Hp =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getHp());

        RequestBody tanggal =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getTanggal());


        RequestBody alamat_tujuan =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getAlamatTujuan());


        RequestBody alamat_penjempputan =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getAlamatPenjemputan());


        RequestBody id_jadwal =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdJadwal());
        RequestBody id_pemesanana =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdPemesanan());

        RequestBody status =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getmStatus());


        RequestBody pembayaran =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getmPembayaran());




        RequestBody id_user =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdUser());

        RequestBody lat =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getLat());

        RequestBody lng =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getLng());





        Preferences.resetAction(this);



        Call<GsonAction> apiCall = mApiInterface.posteditPemesanan(  Nama,
                KTP,
                Hp,
                tanggal,
                alamat_tujuan,
                alamat_penjempputan,
                id_jadwal,
                id_user,
                id_pemesanana,
                status,
                pembayaran,
                lat,
                lng);
        apiCall.enqueue(myRespon);


    }
/*RESPON DARI SERVER*/
    Callback myRespon = new Callback<GsonAction>() {
        @Override
        public void onResponse(Call<GsonAction> call, Response<GsonAction>
                response) {
            try {
                GsonAction resJSON;
                resJSON = response.body();
                Toast.makeText(DetailPesananAdminActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                Preferences.saveAction(DetailPesananAdminActivity.this);
                Log.i("MyAPP", response.body().toString());
            }catch (Exception ex){
                Toast.makeText(DetailPesananAdminActivity.this, "Proses Penyimpanan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
            } finally {

            }

        }

        @Override
        public void onFailure(Call<GsonAction> call, Throwable t) {
            Log.e("Retrofit Get", t.toString());
            Toast.makeText(DetailPesananAdminActivity.this, "Proses Penyimpanan Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    };
    /*TAMPIL TANGGAL DIALOG*/
    private void showDateDialog(View v){


        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);


                ed_tanggal.setText( dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate((Calendar.getInstance().getTimeInMillis())+(1000*60*60*24*6));
        datePickerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*AMBIL DATA JADWAL DARI SERVER*/
    List<Jadwal> DataListJADWAL;
    private void loadData() {

        Call<JadwalResponse> apiCall = mApiInterface.getJadwal();
        apiCall.enqueue(new Callback<JadwalResponse>() {
            @Override
            public void onResponse(Call<JadwalResponse> call, Response<JadwalResponse>
                    response) {
                if (response.body().getStatus().equals( "OK"))
                {
                    DataListJADWAL = response.body().getJadwal();
                    JadwaLAdapter  mAdapter = new JadwaLAdapter(DataListJADWAL);
                    mAdapter.setLastSelectedPosition(-1);
                    int idx = 0;
                    for (Jadwal tmp : DataListJADWAL){
                        if (tmp.getIdJadwal().equals( CURRENT_DATA.getIdJadwal()) ){
                            mAdapter.setLastSelectedPosition(idx);
                            IDX_JADWAL =idx;
                        }
                        idx ++;
                    }

                    Log.d("Retrofit Get", "Jumlah data News: " +
                            String.valueOf(DataListJADWAL.size()));

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
