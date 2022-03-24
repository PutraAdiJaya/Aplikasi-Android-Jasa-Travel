package me.pj.travel;

import androidx.appcompat.app.AppCompatActivity;
import me.pj.travel.controller.Preferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout layCustomer,layAdmin,layDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Preferences.loadPreferences(this);
        layAdmin = findViewById(R.id.LayAdmin);
        layCustomer = findViewById(R.id.LayCostumer );
        layDriver = findViewById(R.id.LayDriver );

        layAdmin.setVisibility(View.GONE);
        layCustomer.setVisibility(View.GONE);
        layDriver.setVisibility(View.GONE);

    }
/*AKSI PADA MENU UTAMA*/
    public void show_Jadwal_Admin(View v){
        startActivity(new Intent(this,JadwalAdminActivity.class));
    }

    public void show_Pesan_Admin(View v){

        startActivity(new Intent(MainActivity.this,PesananAdminActivity.class));
    }

    public void show_Laporan_Admin(View v){

        startActivity(new Intent(MainActivity.this,LaporanAdminActivity.class));
    }

    public void show_Jadwal_User(View v){

        startActivity(new Intent(MainActivity.this,JadwalUserActivity.class));
    }

    public void show_Pesan_User(View v){
        startActivity(new Intent(MainActivity.this,MenuPesanUserActivity.class));
    }

    public void show_Lokasi_User(View v){

        startActivity(new Intent(MainActivity.this,LokasiUserActivity.class));
    }

    public void show_Bantuan(View v){
        startActivity(new Intent(MainActivity.this,BantuanActivity.class));

    }

    public void show_Pesan_Driver(View v){

        startActivity(new Intent(MainActivity.this, LokasiPelangganDriverActivity.class));
    }

    public void show_Lokasi_Driver(View v){

        startActivity(new Intent(MainActivity.this,MapDriverActivity.class));
    }





    @Override
    protected void onResume() {
        super.onResume();
        if (!Preferences.IS_LOGIN){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else {
            if (Preferences.AKSES.equals("ADMIN")){
                layAdmin.setVisibility(View.VISIBLE);
            }else if (Preferences.AKSES.equals("DRIVER")){
                layDriver.setVisibility(View.VISIBLE);
            }else if (Preferences.AKSES.equals("USER")){
                layCustomer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menu");
        // Set up the buttons
        builder.setNeutralButton("Batal",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Logout",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Preferences.resetUserPreferences(MainActivity.this);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();

                    }
                });
        builder.setPositiveButton("Keluar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder.show();
    }
}
