package fr.cytech.b3.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent openMainActivity =  new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(openMainActivity);
                finish();

            }
        }, 5000);  //it will call the MainActivity after 5 seconds
    }
}