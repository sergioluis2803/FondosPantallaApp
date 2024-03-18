package com.serch.fondosdepantalla.CategoriasCliente;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA.Pelicula;
import com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA.ViewHolderPelicula;
import com.serch.fondosdepantalla.R;

public class PeliculasCliente extends AppCompatActivity {

    RecyclerView recyclerViewPeliculaC;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Pelicula, ViewHolderPelicula> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Películas");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewPeliculaC = findViewById(R.id.recyclerViewPeliculaC);
        recyclerViewPeliculaC.setHasFixedSize(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("PELICULAS");

        dialog = new Dialog(PeliculasCliente.this);

        ListarImagenesPeliculas();
    }

    private void ListarImagenesPeliculas() {
        options = new FirebaseRecyclerOptions.Builder<Pelicula>().setQuery(mRef, Pelicula.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pelicula, ViewHolderPelicula>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderPelicula viewHolderPelicula, int i, @NonNull Pelicula pelicula) {
                viewHolderPelicula.SeteoPeliculas(getApplicationContext(), pelicula.getNombre(), pelicula.getVistas(), pelicula.getImagen());
            }

            @NonNull
            @Override
            public ViewHolderPelicula onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelicula, parent, false);
                ViewHolderPelicula viewHolderPelicula = new ViewHolderPelicula(itemView);

                viewHolderPelicula.setOnClickListener(new ViewHolderPelicula.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Toast.makeText(PeliculasCliente.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {
                    }
                });
                return viewHolderPelicula;
            }
        };

        sharedPreferences = PeliculasCliente.this.getSharedPreferences("PELICULAS", MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar", "Dos");

        if (ordenar_en.equals("Dos")) {
            recyclerViewPeliculaC.setLayoutManager(new GridLayoutManager(PeliculasCliente.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPeliculaC.setAdapter(firebaseRecyclerAdapter);
        } else if (ordenar_en.equals("Tres")) {
            recyclerViewPeliculaC.setLayoutManager(new GridLayoutManager(PeliculasCliente.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPeliculaC.setAdapter(firebaseRecyclerAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_vista, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Vista) {
            Ordenar_Imagenes();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Ordenar_Imagenes() {
        String ubicacion = "fuentes/sans_negrita.ttf";
        Typeface tf = Typeface.createFromAsset(PeliculasCliente.this.getAssets(), ubicacion);

        TextView OrdenarTXT;
        Button Dos_Columnas, Tres_Columnas;

        dialog.setContentView(R.layout.dialog_ordenar);

        OrdenarTXT = dialog.findViewById(R.id.OrdenarTXT);
        Dos_Columnas = dialog.findViewById(R.id.Dos_Columnas);
        Tres_Columnas = dialog.findViewById(R.id.Tres_Columnas);

        dialog.show();

        OrdenarTXT.setTypeface(tf);
        Dos_Columnas.setTypeface(tf);
        Tres_Columnas.setTypeface(tf);

        Dos_Columnas.setOnClickListener(task -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Ordenar", "Dos");
            editor.apply();
            recreate();
            dialog.dismiss();
        });

        Tres_Columnas.setOnClickListener(task -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Ordenar", "Tres");
            editor.apply();
            recreate();
            dialog.dismiss();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}