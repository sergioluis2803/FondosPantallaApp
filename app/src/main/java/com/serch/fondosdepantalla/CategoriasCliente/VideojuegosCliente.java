package com.serch.fondosdepantalla.CategoriasCliente;

import android.app.Dialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA.Pelicula;
import com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA.Videojuego;
import com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA.ViewHolderVideojuego;
import com.serch.fondosdepantalla.DetalleCliente.DetalleImagen;
import com.serch.fondosdepantalla.R;

import java.util.HashMap;

public class VideojuegosCliente extends AppCompatActivity {

    RecyclerView recyclerViewVideojuegoC;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Videojuego, ViewHolderVideojuego> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Videojuego> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videojuegos_cliente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Videojuegos");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewVideojuegoC = findViewById(R.id.recyclerViewVideojuegoC);
        recyclerViewVideojuegoC.setHasFixedSize(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("VIDEOJUEGOS");

        dialog = new Dialog(VideojuegosCliente.this);
        ListarImagenesVideojuegos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRef != null && valueEventListener != null) {
            mRef.removeEventListener(valueEventListener);
        }
    }

    private void ListarImagenesVideojuegos() {
        options = new FirebaseRecyclerOptions.Builder<Videojuego>().setQuery(mRef, Videojuego.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Videojuego, ViewHolderVideojuego>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderVideojuego viewHolderVideojuego, int i, @NonNull Videojuego videojuego) {
                viewHolderVideojuego.SeteoVideojuegos(getApplicationContext(), videojuego.getNombre(), videojuego.getVistas(), videojuego.getImagen());
            }

            @NonNull
            @Override
            public ViewHolderVideojuego onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videojuegos, parent, false);
                ViewHolderVideojuego viewHolderVideojuego = new ViewHolderVideojuego(itemView);

                viewHolderVideojuego.setOnClickListener(new ViewHolderVideojuego.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        String id = getItem(position).getId();
                        String imagen = getItem(position).getImagen();
                        String nombre = getItem(position).getNombre();
                        int vistas = getItem(position).getVistas();
                        String vistasString = String.valueOf(vistas);

                        valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Pelicula pelicula = ds.getValue(Pelicula.class);

                                    if (pelicula.getId().equals(id)) {
                                        int i = 1;
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("vistas", vistas + i);
                                        ds.getRef().updateChildren(hashMap);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Intent intent = new Intent(VideojuegosCliente.this, DetalleImagen.class);
                        intent.putExtra("Imagen", imagen);
                        intent.putExtra("nombre", nombre);
                        intent.putExtra("Vista", vistasString);

                        startActivity(intent);
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {
                    }
                });
                return viewHolderVideojuego;
            }
        };

        sharedPreferences = VideojuegosCliente.this.getSharedPreferences("VIDEOJUEGOS", MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar", "Dos");

        if (ordenar_en.equals("Dos")) {
            recyclerViewVideojuegoC.setLayoutManager(new GridLayoutManager(VideojuegosCliente.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVideojuegoC.setAdapter(firebaseRecyclerAdapter);
        } else if (ordenar_en.equals("Tres")) {
            recyclerViewVideojuegoC.setLayoutManager(new GridLayoutManager(VideojuegosCliente.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVideojuegoC.setAdapter(firebaseRecyclerAdapter);
        }
    }

    private void Ordenar_Imagenes() {
        String ubicacion = "fuentes/sans_negrita.ttf";
        Typeface tf = Typeface.createFromAsset(VideojuegosCliente.this.getAssets(), ubicacion);

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

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}