package com.example.capstone1.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone1.R;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler handler = new Handler();
        handler.postDelayed(new splashHandler(),1500);
    }

    private class splashHandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplicationContext(),Main.class));
            Splash.this.finish();
        }
    }
}
