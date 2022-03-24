package me.pj.travel;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class BantuanActivity extends AppCompatActivity {


    LinearLayout LayButton,lay0,lay1,lay2,lay3,lay4;
    ScrollView LayHellp;

    List<LinearLayout> layDataText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LayButton = findViewById(R.id.buttonHelp) ;
        lay0 = findViewById(R.id.lay0) ;
        lay1 = findViewById(R.id.lay1) ;
        lay2 = findViewById(R.id.lay2) ;
        lay3 = findViewById(R.id.lay3) ;
        lay4 = findViewById(R.id.lay4) ;

        LayHellp = findViewById(R.id.layHelp);

        layDataText.add(lay0);
        layDataText.add(lay1);
        layDataText.add(lay2);
        layDataText.add(lay3);
        layDataText.add(lay4);
        for (int i = 0; i < layDataText.size(); i++) {
            layDataText.get(i).setVisibility(View.GONE);
        }
        LayHellp.setVisibility(View.GONE);
    }


    public void tampil_10(View v){
         tampil(0);
    }

    private void tampil(int j) {
        for (int i = 0; i < layDataText.size(); i++) {
            layDataText.get(i).setVisibility(View.GONE);
        }
        layDataText.get(j).setVisibility(View.VISIBLE);
        LayHellp.setVisibility(View.VISIBLE);
        LayButton.setVisibility(View.GONE);
    }

    public void tampil_11(View v){

        tampil(1);
    }

    public void tampil_12(View v){

        tampil(3);
    }


    public void tampil_13(View v){

        tampil(3);
    }


    public void tampil_14(View v){

        tampil(4);
    }

    public void resetALl(View v){

        for (int i = 0; i < layDataText.size(); i++) {
            layDataText.get(i).setVisibility(View.GONE);
        }
        LayHellp.setVisibility(View.GONE);
        LayButton.setVisibility(View.VISIBLE);
    }

}
