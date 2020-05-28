package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       Thread splash = new Thread(){
           public void  run(){
               try {
                   sleep(2000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               finally {
                   startActivity(new Intent(Splash.this, MainActivity.class));
                   finish();
               }

           }

       };
       splash.start();



    }
}
