package com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.serch.fondosdepantalla.R;
import com.serch.fondosdepantalla.util.MyProgressDialog;

public class AgregarVideojuego extends AppCompatActivity {

    TextView VistaPeliculas;
    EditText NombreVideojuego;
    ImageView ImagenAgregarVideojuego;
    Button PublicarVideojuego;

    String RutaDeAlmacenamiento = "Videojuego_Subida/";
    String RutaDeBaseDeDatos = "VIDEOJUEGOS";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;
    MyProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_videojuego);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        VistaPeliculas = findViewById(R.id.VistaPeliculas);
        NombreVideojuego = findViewById(R.id.NombreVideojuego);
        ImagenAgregarVideojuego = findViewById(R.id.ImagenAgregarVideojuego);
        PublicarVideojuego = findViewById(R.id.PublicarVideojuego);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);

        progressDialog = new MyProgressDialog(this);

        ImagenAgregarVideojuego.setOnClickListener(view -> selectImageLauncher.launch("image/*"));
        PublicarVideojuego.setOnClickListener(task -> SubirImagen());
    }

    private void SubirImagen() {
        if (RutaArchivoUri != null) {
            progressDialog.show();
            StorageReference storageReference = mStorageReference.child(RutaDeAlmacenamiento + System.currentTimeMillis() + "." + ObtenerExtensionDelArchivo(RutaArchivoUri));

            storageReference.putFile(RutaArchivoUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;

                Uri downloadURI = uriTask.getResult();
                String mNombre = NombreVideojuego.getText().toString();
                String mVista = VistaPeliculas.getText().toString();
                int VISTA = Integer.parseInt(mVista);

                Videojuego videojuego = new Videojuego(downloadURI.toString(), mNombre, VISTA);
                String ID_IMAGEN = DatabaseReference.push().getKey();
                assert ID_IMAGEN != null;
                DatabaseReference.child(ID_IMAGEN).setValue(videojuego);

                progressDialog.dismiss();

                Toast.makeText(AgregarVideojuego.this, "Subido Exitosamente", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(AgregarVideojuego.this, VideojuegosA.class));
                finish();
            }).addOnFailureListener(task -> {
                progressDialog.dismiss();
                Toast.makeText(AgregarVideojuego.this, "ERROR: " + task.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "DEBE ASIGNAR UNA IMAGEN", Toast.LENGTH_SHORT).show();
        }
    }

    private String ObtenerExtensionDelArchivo(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private final ActivityResultLauncher<String> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if (result != null) {
                        RutaArchivoUri = result;

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), RutaArchivoUri);
                            ImagenAgregarVideojuego.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            Toast.makeText(AgregarVideojuego.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}