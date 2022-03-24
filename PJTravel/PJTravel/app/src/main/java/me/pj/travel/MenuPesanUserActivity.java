package me.pj.travel;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MenuPesanUserActivity extends AppCompatActivity {
/*SUB MENU PESAN*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pesan_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void showMenuLihatPesanan(View v){
        startActivity(new Intent(this,PesananUserActivity.class));
    }
    public void showMenuPesanan(View v){
        startActivity(new Intent(this,BuatPesananUserActivity.class));

    }


}
