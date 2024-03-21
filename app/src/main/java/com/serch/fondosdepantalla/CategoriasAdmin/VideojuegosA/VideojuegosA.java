package com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

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

public class VideojuegosA extends AppCompatActivity {

    RecyclerView recyclerViewVideojuego;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Videojuego, ViewHolderVideojuego> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Videojuego> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videojuegos);
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

        recyclerViewVideojuego = findViewById(R.id.recyclerViewVideojuego);
        recyclerViewVideojuego.setHasFixedSize(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("VIDEOJUEGOS");

        dialog = new Dialog(VideojuegosA.this);
        ListarImagenesVideojuegos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
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
                        Toast.makeText(VideojuegosA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {
                        String id = getItem(position).getId();
                        String nombre = getItem(position).getNombre();
                        String imagen = getItem(position).getImagen();
                        int vista = getItem(position).getVistas();
                        String vistaString = String.valueOf(vista);

                        AlertDialog.Builder builder = new AlertDialog.Builder(VideojuegosA.this);
                        String[] opciones = {"Actualizar", "Eliminar"};

                        builder.setItems(opciones, (dialogInterface, i) -> {
                            if (i == 0) {
                                Intent intent = new Intent(VideojuegosA.this, AgregarVideojuego.class);
                                intent.putExtra("IdEnviado", id);
                                intent.putExtra("NombreEnviado", nombre);
                                intent.putExtra("ImagenEnviada", imagen);
                                intent.putExtra("VistaEnviada", vistaString);

                                startActivity(intent);
                            } else {
                                EliminarDatos(id, imagen);
                            }
                        });
                        builder.create().show();
                    }
                });
                return viewHolderVideojuego;
            }
        };

        sharedPreferences = VideojuegosA.this.getSharedPreferences("VIDEOJUEGOS", MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar", "Dos");

        if (ordenar_en.equals("Dos")) {
            recyclerViewVideojuego.setLayoutManager(new GridLayoutManager(VideojuegosA.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVideojuego.setAdapter(firebaseRecyclerAdapter);
        } else if (ordenar_en.equals("Tres")) {
            recyclerViewVideojuego.setLayoutManager(new GridLayoutManager(VideojuegosA.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVideojuego.setAdapter(firebaseRecyclerAdapter);
        }
    }

    private void Ordenar_Imagenes() {
        String ubicacion = "fuentes/sans_negrita.ttf";
        Typeface tf = Typeface.createFromAsset(VideojuegosA.this.getAssets(), ubicacion);

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

    private void EliminarDatos(final String idActual, final String ImagenActual) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideojuegosA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea eliminar imagen?");
        builder.setPositiveButton("SI", (dialogInterface, i) -> {
            Query query = mRef.orderByChild("id").equalTo(idActual);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ds.getRef().removeValue();
                    }

                    Toast.makeText(VideojuegosA.this, "La imagen ha sido eliminada", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(VideojuegosA.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            StorageReference imagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
            imagenSeleccionada.delete().addOnSuccessListener(unused -> Toast.makeText(VideojuegosA.this, "Eliminado", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(VideojuegosA.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("NO", (dialogInterface, i) -> Toast.makeText(VideojuegosA.this, "Cancelado por administrador", Toast.LENGTH_SHORT).show());

        builder.create().show();
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
            startActivity(new Intent(this, AgregarVideojuego.class));
            finish();
        } else if (item.getItemId() == R.id.Vista) {
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