package com.serch.fondosdepantalla.Categorias;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serch.fondosdepantalla.CategoriasCliente.MusicaCliente;
import com.serch.fondosdepantalla.CategoriasCliente.PeliculasCliente;
import com.serch.fondosdepantalla.CategoriasCliente.SeriesCliente;
import com.serch.fondosdepantalla.CategoriasCliente.VideojuegosCliente;
import com.serch.fondosdepantalla.R;

public class ControladorCD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlador_cd);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String categoryGet = getIntent().getStringExtra("Categoria");
        if (categoryGet.equals("Peliculas")) {
            startActivity(new Intent(ControladorCD.this, PeliculasCliente.class));
            finish();
        }

        if (categoryGet.equals("Series")) {
            startActivity(new Intent(ControladorCD.this, SeriesCliente.class));
            finish();
        }

        if (categoryGet.equals("Musica")) {
            startActivity(new Intent(ControladorCD.this, MusicaCliente.class));
            finish();
        }

        if (categoryGet.equals("Videojuegos")) {
            startActivity(new Intent(ControladorCD.this, VideojuegosCliente.class));
            finish();
        }
    }
}