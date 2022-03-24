package me.pj.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    ProgressBar pbMain;
    int welcomeScreenDisplay = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final int welcomeScreenDisplay =100;
        pbMain = findViewById(R.id.progressBar1 );
        pbMain.setMax(welcomeScreenDisplay);

/*TIMER TUNGGU*/
        Thread threads = new Thread() {

            int wait = 0;

            @Override
            public void run() {
                try {
                    super.run();
                    //delay
                    while (wait < welcomeScreenDisplay) {
                        sleep(10);
                        wait +=  1;
                        pbMain.setProgress(wait);
                    }
                } catch (Exception e) {
                    System.out.println("Error:" + e);
                } finally {

                    newIntens();
                }
            }
        };
        threads.start();

    }

    protected void newIntens() {
        // TODO Auto-generated method stub
        //tampil menu utama
        startActivity(new Intent( this, LoginActivity.class ));
        finish();
    }

}
