package com.serch.fondosdepantalla.DetalleCliente;

import android.Manifest;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.clans.fab.FloatingActionButton;
import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class DetalleImagen extends AppCompatActivity {

    ImageView ImagenDetalle;
    TextView NombreImagenDetalle, VistaDetalle;
    FloatingActionButton fabDescargar, fabCompartir, fabEstablecer;

    Bitmap bitmap;

    private final Uri imageUri = null;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_imagen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Detalle");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImagenDetalle = findViewById(R.id.ImagenDetalle);
        NombreImagenDetalle = findViewById(R.id.NombreImagenDetalle);
        VistaDetalle = findViewById(R.id.VistaDetalle);

        fabDescargar = findViewById(R.id.fabDescargar);
        fabCompartir = findViewById(R.id.fabCompartir);
        fabEstablecer = findViewById(R.id.fabEstablecer);

        dialog = new Dialog(this);

        String imagen = getIntent().getStringExtra("Imagen");
        String nombres = getIntent().getStringExtra("nombre");
        String vista = getIntent().getStringExtra("Vista");

        try {
            Picasso.get().load(imagen).placeholder(R.drawable.detalle_imagen).into(ImagenDetalle);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.detalle_imagen).into(ImagenDetalle);
        }
        NombreImagenDetalle.setText(nombres);
        VistaDetalle.setText(vista);

        fabDescargar.setOnClickListener(task -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(DetalleImagen.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                        .PERMISSION_GRANTED) {
                    downloadImage11();
                } else {
                    SolicitudPermisoDescargaAndroid11oSuperior.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(DetalleImagen.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                        .PERMISSION_GRANTED) {
                    downloadImage();
                } else {
                    SolicitudPermisoDescarga.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            } else {
                downloadImage();
            }
        });

        fabCompartir.setOnClickListener(task -> shareImage_Actu());
        fabEstablecer.setOnClickListener(task -> establecerImagen());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void establecerImagen() {
        bitmap = ((BitmapDrawable) ImagenDetalle.getDrawable()).getBitmap();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        try {
            wallpaperManager.setBitmap(bitmap);
            Animacion_Establecido();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadImage11() {
        bitmap = ((BitmapDrawable) ImagenDetalle.getDrawable()).getBitmap();
        OutputStream fos;

        String nameImage = NombreImagenDetalle.getText().toString();

        try {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, nameImage);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "Image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "/Fondos/");

            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos);
            Animacion_Descarga_Exitosa();
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo descargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadImage() {
        bitmap = ((BitmapDrawable) ImagenDetalle.getDrawable()).getBitmap();

        String dateDownload = new SimpleDateFormat("'Fecha Descarga: ' yyyy_MM_dd 'Hora: ' HH:mm:ss",
                Locale.getDefault()).format(System.currentTimeMillis());

        File path = Environment.getExternalStorageDirectory();
        File nameFile = new File(path + "Fondos");
        nameFile.mkdir();

        String getNameImage = NombreImagenDetalle.getText().toString();
        String nameImage = getNameImage + " " + dateDownload + ".JPEG";
        File file = new File(nameFile, nameImage);
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Animacion_Descarga_Exitosa();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<String> SolicitudPermisoDescarga = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            downloadImage();
        } else {
            Animacion_Active_Permisos();
        }
    });

    private final ActivityResultLauncher<String> SolicitudPermisoDescargaAndroid11oSuperior =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    downloadImage11();
                } else {
                    Animacion_Active_Permisos();
                }
            });

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void shareImage_Actu() {
        Uri contentUri = getContentUri();

        Intent sharedIntent = new Intent(Intent.ACTION_SEND);

        sharedIntent.setType("image/jpeg");
        sharedIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
        sharedIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        sharedIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(sharedIntent);
    }

    private Uri getContentUri() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ImagenDetalle.getDrawable();
        bitmap = bitmapDrawable.getBitmap();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source =
                        ImageDecoder.createSource(getContentResolver(), imageUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            }
        } catch (Exception e) {
        }

        File imageFolder = new File(getCacheDir(), "images");
        Uri contentUri = null;

        try {
            String NombreImagen = NombreImagenDetalle.getText().toString();
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            contentUri = FileProvider.getUriForFile(
                    this, "com.redsystem.fondodepantalla.fileprovider", file);
        } catch (Exception e) {
        }

        return contentUri;
    }

    private void Animacion_Active_Permisos() {
        Button OKPERMISOS;
        dialog.setContentView(R.layout.animacion_permiso);
        OKPERMISOS = dialog.findViewById(R.id.OKPERMISOS);

        OKPERMISOS.setOnClickListener(task -> dialog.dismiss());

        dialog.show();
        dialog.setCancelable(false);
    }

    private void Animacion_Descarga_Exitosa(){
        Button OKDESCARGA;

        dialog.setContentView(R.layout.animacion_descarga_exitosa);
        OKDESCARGA = dialog.findViewById(R.id.OKDESCARGA);
        OKDESCARGA.setOnClickListener(task -> dialog.dismiss());
        dialog.show();
        dialog.setCancelable(false);
    }

    private void Animacion_Establecido(){
        Button OKESTABLECIDO;

        dialog.setContentView(R.layout.animacion_establecido);
        OKESTABLECIDO = dialog.findViewById(R.id.OKESTABLECIDO);
        OKESTABLECIDO.setOnClickListener(task -> dialog.dismiss());
        dialog.show();
        dialog.setCancelable(false);
    }
}