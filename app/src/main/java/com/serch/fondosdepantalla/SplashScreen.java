package com.serch.fondosdepantalla;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    TextView app_name, name_developer;
    final int DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        String location = "fuentes/sans_negrita.ttf";
        Typeface tf = Typeface.createFromAsset(SplashScreen.this.getAssets(), location);

        app_name = findViewById(R.id.app_name);
        name_developer = findViewById(R.id.desarrollador);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivityAdmin.class);
            startActivity(intent);
            finish();
        }, DURATION);

        app_name.setTypeface(tf);
        name_developer.setTypeface(tf);
    }
}