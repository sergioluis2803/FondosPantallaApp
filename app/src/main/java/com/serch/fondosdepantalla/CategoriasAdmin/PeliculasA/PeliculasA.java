package com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.serch.fondosdepantalla.R;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class PeliculasA extends AppCompatActivity {

    RecyclerView recyclerViewPelicula;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Pelicula, ViewHolderPelicula> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Películas");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        recyclerViewPelicula = findViewById(R.id.recyclerViewPelicula);
        recyclerViewPelicula.setHasFixedSize(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("PELICULAS");

        ListarImagenesPeliculas();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void EliminarDatos(final String NombreActual, final String ImagenActual) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PeliculasA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea eliminar imagen?");
        builder.setPositiveButton("SI", (dialogInterface, i) -> {
            Query query = mRef.orderByChild("nombre").equalTo(NombreActual);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ds.getRef().removeValue();
                    }

                    Toast.makeText(PeliculasA.this, "La imagen ha sido eliminada", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PeliculasA.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            StorageReference imagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
            imagenSeleccionada.delete().addOnSuccessListener(unused -> Toast.makeText(PeliculasA.this, "Eliminado", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(PeliculasA.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("NO", (dialogInterface, i) -> Toast.makeText(PeliculasA.this, "Cancelado por administrador", Toast.LENGTH_SHORT).show());

        builder.create().show();
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
                        Toast.makeText(PeliculasA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {

                        String nombre = getItem(position).getNombre();
                        String imagen = getItem(position).getImagen();
                        int vista = getItem(position).getVistas();
                        String vistaString = String.valueOf(vista);

                        AlertDialog.Builder builder = new AlertDialog.Builder(PeliculasA.this);
                        String[] opciones = {"Actualizar", "Eliminar"};

                        builder.setItems(opciones, (dialogInterface, i) -> {
                            if (i == 0) {
                                Intent intent = new Intent(PeliculasA.this, AgregarPelicula.class);
                                intent.putExtra("NombreEnviado", nombre);
                                intent.putExtra("ImagenEnviada", imagen);
                                intent.putExtra("VistaEnviada", vistaString);

                                startActivity(intent);
                            } else {
                                EliminarDatos(nombre, imagen);
                            }
                        });
                        builder.create().show();
                    }
                });
                return viewHolderPelicula;
            }
        };

        recyclerViewPelicula.setLayoutManager(new GridLayoutManager(PeliculasA.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewPelicula.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agregar, menu);
        menuInflater.inflate(R.menu.menu_vista, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Agregar) {
            startActivity(new Intent(this, AgregarPelicula.class));
            finish();
        } else if (item.getItemId() == R.id.Vista) {
            Toast.makeText(this, "Listar imagenes", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}