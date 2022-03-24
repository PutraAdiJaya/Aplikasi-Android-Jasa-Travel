package me.pj.travel;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.pj.travel.controller.ApiClient;
import me.pj.travel.controller.Preferences;
import me.pj.travel.interfece.ApiInterface;
import me.pj.travel.models.GsonAction;
import me.pj.travel.models.Jadwal;
import me.pj.travel.models.Kota;
import me.pj.travel.models.KotaResponse;
import me.pj.travel.models.User;
import me.pj.travel.models.UserResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InputJadwalAdminActivity extends AppCompatActivity {

    EditText  ed_Jenis_Armada,
            ed_Merk,
            ed_No_Polisi,
            ed_Kapasitas,
            ed_Foto,
            ed_Tarif  ;
    Spinner sp_Driver,sp_Jam,sp_Kota;
    int MODE=-1;
    ImageButton btnFoto;
    ApiInterface  mApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_jadwal_admin);
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
        sp_Driver = findViewById(R.id.sp_Driver);
        sp_Jam = findViewById(R.id.sp_Jam);

        List listString = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String tmp =  padLeftZeros(String.valueOf(i), 2)+":00";
            listString.add(tmp);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InputJadwalAdminActivity.this,  R.layout.spiner_item, listString);
        sp_Jam.setAdapter(dataAdapter);

        ed_No_Polisi =findViewById(R.id.ed_No_Polisi);
        ed_Jenis_Armada =findViewById(R.id.ed_Jenis_Armada);
        ed_Kapasitas =findViewById(R.id.ed_Kapasitas);
        ed_Merk =findViewById(R.id.ed_Merk);
        ed_Tarif =findViewById(R.id.ed_Tarif);
        sp_Kota =findViewById(R.id.sp_Kota);


        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        imageView  = (ImageView) findViewById(R.id.foto);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED  ) {

            System.out.println("REQUEST");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE ) ) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            System.out.println("GRANTED");
        }

        CURRENT_DATA = new Jadwal();
        MODE=getIntent().getIntExtra("MODE",-1);
        if (MODE==1){
            String[] DATA = getIntent().getStringArrayExtra("DATA");

        /*    NamaFIle = DATA[1];*/
            CURRENT_DATA.setJenisArmada(DATA[4]);
            CURRENT_DATA.setMerk(DATA[6]);
            CURRENT_DATA.setNoPolisi(DATA[7]);
            CURRENT_DATA.setKapasitas(DATA[5]);
            CURRENT_DATA.setTujuan(DATA[9]);
            CURRENT_DATA.setTarif(DATA[8]);
            CURRENT_DATA.setJam(DATA[3]);
            CURRENT_DATA.setDriver(DATA[0]);
            CURRENT_DATA.setFoto(DATA[1]);
            CURRENT_DATA.setIdJadwal(DATA[2]);
            ID_JADWAL = DATA[2];

            ed_No_Polisi.setText(CURRENT_DATA.getNoPolisi());
            ed_Jenis_Armada.setText(CURRENT_DATA.getJenisArmada());
            ed_Kapasitas.setText(CURRENT_DATA.getKapasitas());
            ed_Merk.setText(CURRENT_DATA.getMerk());
            ed_Tarif.setText(CURRENT_DATA.getTarif());


            int spinnerPosition = dataAdapter.getPosition(CURRENT_DATA.getJam());
            sp_Jam.setSelection(spinnerPosition);


        }else {
            ed_No_Polisi.setText("");
            ed_Jenis_Armada.setText("");
            ed_Kapasitas.setText("");
            ed_Merk.setText("");
            ed_Tarif.setText("");
            /* ed_No_Polisi.setText("AG 2323 ED");
            ed_Jenis_Armada.setText("Mini BUS");
            ed_Kapasitas.setText("50");
            ed_Merk.setText("APV");
            ed_Tarif.setText("50000");*/
        }


    }



    int  MY_PERMISSIONS_REQUEST_READ_CONTACTS =1;
    private static final int PICK_PHOTO_FOR_AVATAR = 1888;
    private ImageView imageView;
    String NamaFIle ="";
    /*RESPON DARI LOAD GAMVAR*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

            System.out.println(picturePath);
            NamaFIle = picturePath;
            imageView =  findViewById(R.id.foto);
            imageView.setImageBitmap(bitmap);

        }
    }
/*AMBIL GAMBAR DARI FOLDER*/
    public void takePhoto(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }
    String ID_JADWAL;
    Jadwal CURRENT_DATA;
    /*VALIDASI DATA INPUTA*/
    private void SimpanData() {

        // Reset errors.
        ed_Jenis_Armada.setError(null);
        ed_Merk.setError(null);
        ed_No_Polisi.setError(null);
        ed_Kapasitas.setError(null);
        ed_Tarif.setError(null);


        // Store values at the time of the login attempt.
        final String  Jenis_Armada = ed_Jenis_Armada.getText().toString();
        final String Merk = ed_Merk.getText().toString();
        String  No_Polisi = ed_No_Polisi.getText().toString();
        String  Kapasitas = ed_Kapasitas.getText().toString();
        String  Tarif = ed_Tarif.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(Jenis_Armada)) {
            ed_Jenis_Armada.setError(getString(R.string.error_field_required));
            focusView = ed_Jenis_Armada;
            cancel = true;
        }

        if (TextUtils.isEmpty(Merk)  ) {
            ed_Merk.setError(getString(R.string.error_incorrect_password));
            focusView = ed_Merk;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(No_Polisi)) {
            ed_No_Polisi.setError(getString(R.string.error_field_required));
            focusView = ed_No_Polisi;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(Kapasitas)) {
            ed_Kapasitas.setError(getString(R.string.error_field_required));
            focusView = ed_Kapasitas;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(Tarif)) {
            ed_Tarif.setError(getString(R.string.error_field_required));
            focusView = ed_Tarif;
            cancel = true;
        }

        if (NamaFIle.trim().length()==0){
            cancel=true;
            Toast.makeText(InputJadwalAdminActivity.this,"Silahkan Pilih Gambar..",Toast.LENGTH_LONG).show();
            return;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            CURRENT_DATA.setJenisArmada(Jenis_Armada);
            CURRENT_DATA.setMerk(Merk);
            CURRENT_DATA.setNoPolisi(No_Polisi);
            CURRENT_DATA.setKapasitas(Kapasitas);
            CURRENT_DATA.setTarif(Tarif);
            CURRENT_DATA.setJam(sp_Jam.getSelectedItem().toString());
            CURRENT_DATA.setDriver(sp_Driver.getSelectedItem().toString());
            CURRENT_DATA.setTujuan(sp_Kota.getSelectedItem().toString());
            CURRENT_DATA.setFoto("");


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
                            new SaveDataTask().execute();
                        }
                    });

            builder.show();


        }


    }
/*PROSES KIRIM DATA KESERVER*/
    public class SaveDataTask extends AsyncTask<Void, Void, Boolean> {

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;

        public SaveDataTask() {

            Preferences.resetAction(InputJadwalAdminActivity.this);
            dialog = new ProgressDialog(InputJadwalAdminActivity.this);
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            doSaveAndEdit();
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
/*REQUEST DATA*/
    private void doSaveAndEdit() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        File file =  new File(NamaFIle);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        MultipartBody.Part bodyFile =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        RequestBody Jenis_Armada =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getJenisArmada());

        RequestBody Merk =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getMerk());

        RequestBody No_Polisi =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getNoPolisi());

        RequestBody Kapasitas =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getKapasitas());


        RequestBody Tujuan =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getTujuan());


        RequestBody Tarif =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getTarif());


        RequestBody Jam =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getJam());


        RequestBody Driver =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getDriver());

        RequestBody Foto =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getFoto());



        Preferences.resetAction(InputJadwalAdminActivity.this);

        if (MODE==1){
            RequestBody id_jadwal =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdJadwal());

            Call<GsonAction> apiCall = mApiInterface.editJadwal(bodyFile,
                    Jenis_Armada,
                    Merk,
                    No_Polisi,
                    Kapasitas,
                    Tujuan,
                    Tarif,
                    Jam,
                    Driver,
                    Foto,
                    id_jadwal);
            apiCall.enqueue(myRespon);
        }else {
            Call<GsonAction> apiCall = mApiInterface.postJadwal(bodyFile,
                    Jenis_Armada,
                    Merk,
                    No_Polisi,
                    Kapasitas,
                    Tujuan,
                    Tarif,
                    Jam,
                    Driver,
                    Foto);
            apiCall.enqueue(myRespon);

        }

    }
    /*RESPON DARI SERVER*/
    Callback myRespon = new Callback<GsonAction>() {
        @Override
        public void onResponse(Call<GsonAction> call, Response<GsonAction>
                response) {
            try {
                GsonAction resJSON;
                resJSON = response.body();
                Toast.makeText(InputJadwalAdminActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                Preferences.saveAction(InputJadwalAdminActivity.this);
                Log.i("MyAPP", response.body().toString());
            }catch (Exception ex){
                Toast.makeText(InputJadwalAdminActivity.this, "Proses Penyimpanan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
            } finally {

            }



        }

        @Override
        public void onFailure(Call<GsonAction> call, Throwable t) {
            Log.e("Retrofit Get", t.toString());
            Toast.makeText(InputJadwalAdminActivity.this, "Proses Penyimpanan Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();
        }
    };
    private void showProgress(boolean b) {

    }
    public String padLeftZeros(String inputString, int length) {

        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadDriver();
        loadKota();
    }
    List<User> DriverList ;
    private void loadDriver() {
        sp_Driver.setAdapter(null);
        Call<UserResponse> DataCall = mApiInterface.getDriver();
        DataCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse>
                    response) {
                if (response.body().getStatus().equals( "OK"))
                {
                    DriverList = response.body().getUsers();
                    Log.d("Retrofit Get", "Jumlah data : " +
                            String.valueOf(DriverList.size()));

                    List listString = new ArrayList<>();
                    for (int i = 0; i < DriverList.size(); i++) {
                        listString.add(DriverList.get(i).getNama());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InputJadwalAdminActivity.this,  R.layout.spiner_item, listString);
                    sp_Driver.setAdapter(dataAdapter);

                    if (MODE==1){
                        int spinnerPosition = dataAdapter.getPosition(CURRENT_DATA.getDriver());
                        sp_Driver.setSelection(spinnerPosition);
                    }




                }

                Log.i("MyAPP",response.message() );
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(InputJadwalAdminActivity.this,"Proses Pengambilan data Gagal, Cek Koneksi Internet Anda",Toast.LENGTH_LONG).show();
                InputJadwalAdminActivity.this.finish();
            }
        });
    }
    List<Kota> KotaList ;
    private void loadKota() {
        sp_Driver.setAdapter(null);
        Call<KotaResponse> DataCall = mApiInterface.getKota();
        DataCall.enqueue(new Callback<KotaResponse>() {
            @Override
            public void onResponse(Call<KotaResponse> call, Response<KotaResponse>
                    response) {
                if (response.body().getStatus().equals( "OK"))
                {
                    KotaList  = response.body().getResult();
                    Log.d("Retrofit Get", "Jumlah data : " +
                            String.valueOf(KotaList.size()));

                    List listString = new ArrayList<>();
                    for (int i = 0; i < KotaList.size(); i++) {
                        listString.add(KotaList.get(i).getKota() );
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InputJadwalAdminActivity.this,  R.layout.spiner_item, listString);
                    sp_Kota.setAdapter(dataAdapter);

                    if (MODE==1){
                        int spinnerPosition = dataAdapter.getPosition(CURRENT_DATA.getTujuan());
                        sp_Kota.setSelection(spinnerPosition);
                    }




                }

                Log.i("MyAPP",response.message() );
            }

            @Override
            public void onFailure(Call<KotaResponse> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(InputJadwalAdminActivity.this,"Proses Pengambilan data Gagal, Cek Koneksi Internet Anda",Toast.LENGTH_LONG).show();
                InputJadwalAdminActivity.this.finish();
            }
        });
    }
}
