package com.serch.fondosdepantalla.CategoriasAdmin.MusicaA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.serch.fondosdepantalla.R;

public class MusicaA extends AppCompatActivity {

    RecyclerView recyclerViewMusica;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Musica, ViewHolderMusica> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Musica> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Musica");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewMusica = findViewById(R.id.recyclerViewMusica);
        recyclerViewMusica.setHasFixedSize(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("MUSICA");

        ListarImagenesMusica();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void ListarImagenesMusica() {
        options = new FirebaseRecyclerOptions.Builder<Musica>().setQuery(mRef, Musica.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Musica, ViewHolderMusica>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderMusica viewHolderMusica, int i, @NonNull Musica musica) {
                viewHolderMusica.SeteoMusica(getApplicationContext(), musica.getNombre(), musica.getVistas(), musica.getImagen());
            }

            @NonNull
            @Override
            public ViewHolderMusica onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musica, parent, false);
                ViewHolderMusica viewHolderMusica = new ViewHolderMusica(itemView);

                viewHolderMusica.setOnClickListener(new ViewHolderMusica.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Toast.makeText(MusicaA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {
                        String nombre = getItem(position).getNombre();
                        String imagen = getItem(position).getImagen();
                        int vista = getItem(position).getVistas();
                        String vistaString = String.valueOf(vista);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MusicaA.this);
                        String[] opciones = {"Actualizar", "Eliminar"};

                        builder.setItems(opciones, (dialogInterface, i) -> {
                            if (i == 0) {
                                Toast.makeText(MusicaA.this, "ACTUALIZAR", Toast.LENGTH_SHORT).show();
                            } else {
                                EliminarDatos(nombre, imagen);
                            }
                        });
                        builder.create().show();
                    }
                });
                return viewHolderMusica;
            }
        };

        recyclerViewMusica.setLayoutManager(new GridLayoutManager(MusicaA.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewMusica.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarDatos(final String NombreActual, final String ImagenActual) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MusicaA.this);
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

                    Toast.makeText(MusicaA.this, "La imagen ha sido eliminada", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MusicaA.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            StorageReference imagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
            imagenSeleccionada.delete().addOnSuccessListener(unused -> Toast.makeText(MusicaA.this, "Eliminado", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(MusicaA.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("NO", (dialogInterface, i) -> Toast.makeText(MusicaA.this, "Cancelado por administrador", Toast.LENGTH_SHORT).show());

        builder.create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
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
            startActivity(new Intent(this, AgregarMusica.class));
            finish();
        } else if (item.getItemId() == R.id.Vista) {
            Toast.makeText(this, "Listar imagenes", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}