package com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.serch.fondosdepantalla.R;
import com.serch.fondosdepantalla.util.MyProgressDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AgregarVideojuego extends AppCompatActivity {

    TextView VistaVideojuego, idVideojuegos;
    EditText NombreVideojuego;
    ImageView ImagenAgregarVideojuego;
    Button PublicarVideojuego;

    String RutaDeAlmacenamiento = "Videojuego_Subida/";
    String RutaDeBaseDeDatos = "VIDEOJUEGOS";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;
    MyProgressDialog progressDialog;

    String rId,rNombre, rImagen, rVista;
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

        VistaVideojuego = findViewById(R.id.VistaPeliculas);
        NombreVideojuego = findViewById(R.id.NombreVideojuego);
        ImagenAgregarVideojuego = findViewById(R.id.ImagenAgregarVideojuego);
        PublicarVideojuego = findViewById(R.id.PublicarVideojuego);
        idVideojuegos = findViewById(R.id.idVideojuegos);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);

        progressDialog = new MyProgressDialog(this);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            rId = intent.getString("IdEnviado");
            rNombre = intent.getString("NombreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rVista = intent.getString("VistaEnviada");

            idVideojuegos.setText(rId);
            NombreVideojuego.setText(rNombre);
            VistaVideojuego.setText(rVista);
            Picasso.get().load(rImagen).into(ImagenAgregarVideojuego);

            actionBar.setTitle("Actualizar");
            String actualizar = "Actualizar";
            PublicarVideojuego.setText(actualizar);
        }

        ImagenAgregarVideojuego.setOnClickListener(view -> selectImageLauncher.launch("image/*"));
        PublicarVideojuego.setOnClickListener(task ->  {
            if (PublicarVideojuego.getText().equals("Publicar")) {
                SubirImagen();
            } else {
                EmpezarActualizacion();
            }
        });
    }

    private void EmpezarActualizacion() {
        progressDialog.setTitle("Actualizando");
        progressDialog.show();
        progressDialog.setCancelable(false);

        EliminarImagenAnterior();
    }

    private void EliminarImagenAnterior() {
        StorageReference Imagen = getInstance().getReferenceFromUrl(rImagen);
        Imagen.delete().addOnSuccessListener(task -> {
            Toast.makeText(this, "La imagen anterior a sido eliminada", Toast.LENGTH_SHORT).show();
            SubirNuevaImagen();
        }).addOnFailureListener(task -> Toast.makeText(this, task.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void SubirNuevaImagen() {
        String nuevaImagen = System.currentTimeMillis() + "png";
        StorageReference mStorageReference2 = mStorageReference.child(RutaDeAlmacenamiento + nuevaImagen);
        Bitmap bitmap = ((BitmapDrawable) ImagenAgregarVideojuego.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mStorageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(task -> {
            Toast.makeText(this, "Nueva imagen cargada", Toast.LENGTH_SHORT).show();
            Task<Uri> uriTask = task.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            Uri downloadUri = uriTask.getResult();

            ActualizarImagenBD(downloadUri.toString());
        }).addOnFailureListener(task -> {
            Toast.makeText(this, task.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

    private void ActualizarImagenBD(String NuevaImagen) {
        final String nombreActualizar = NombreVideojuego.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("VIDEOJUEGOS");

        Query query = databaseReference.orderByChild("id").equalTo(rId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().child("nombre").setValue(nombreActualizar);
                    ds.getRef().child("imagen").setValue(NuevaImagen);
                }
                progressDialog.dismiss();
                Toast.makeText(AgregarVideojuego.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarVideojuego.this, VideojuegosA.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SubirImagen() {
        if (RutaArchivoUri != null) {
            progressDialog.show();
            StorageReference storageReference = mStorageReference.child(RutaDeAlmacenamiento + System.currentTimeMillis() + "." + ObtenerExtensionDelArchivo(RutaArchivoUri));

            storageReference.putFile(RutaArchivoUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;

                Uri downloadURI = uriTask.getResult();

                String ID = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis());
                idVideojuegos.setText(ID);

                String mNombre = NombreVideojuego.getText().toString();
                String nId = idVideojuegos.getText().toString();
                String mVista = VistaVideojuego.getText().toString();
                int VISTA = Integer.parseInt(mVista);

                Videojuego videojuego = new Videojuego(mNombre + "/" + nId,downloadURI.toString(), mNombre, VISTA);
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