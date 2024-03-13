package com.serch.fondosdepantalla.FragmentosAdminsitrador;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.serch.fondosdepantalla.MainActivityAdministrador;
import com.serch.fondosdepantalla.R;
import com.serch.fondosdepantalla.util.MyProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PerfilAdmin extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

    StorageReference storageReference;
    String RutaDeAlamcenamiento = "Fotos_Perfil_Administradores/*";
    private Uri imagen_uri;
    private String imagen_perfil;
    private MyProgressDialog progressDialog;
    ImageView FOTOPERFILIMG;
    TextView UIDPERFIL, NOMBRESPERFIL, APELLIDOSPERFIL, CORREOPERFIL, PASSWORDPERFIL, EDADPERFIL;
    Button ACTUALIZARPASS, ACTUALIZARDATOS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        FOTOPERFILIMG = view.findViewById(R.id.FOTOPERFILIMG);
        UIDPERFIL = view.findViewById(R.id.UIDPERFIL);
        NOMBRESPERFIL = view.findViewById(R.id.NOMBRESPERFIL);
        APELLIDOSPERFIL = view.findViewById(R.id.APELLIDOSPERFIL);
        CORREOPERFIL = view.findViewById(R.id.CORREOPERFIL);
        PASSWORDPERFIL = view.findViewById(R.id.PASSWORDPERFIL);
        EDADPERFIL = view.findViewById(R.id.EDADPERFIL);
        ACTUALIZARPASS = view.findViewById(R.id.ACTUALIZARPASS);
        ACTUALIZARDATOS = view.findViewById(R.id.ACTUALIZARDATOS);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        storageReference = getInstance().getReference();

        progressDialog = new MyProgressDialog(getActivity());

        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uid = "" + snapshot.child("UID").getValue();
                    String nombre = "" + snapshot.child("NOMBRES").getValue();
                    String apellidos = "" + snapshot.child("APELLIDOS").getValue();
                    String correo = "" + snapshot.child("CORREO").getValue();
                    String password = "" + snapshot.child("PASSWORD").getValue();
                    String edad = "" + snapshot.child("EDAD").getValue();
                    String imagen = "" + snapshot.child("IMAGEN").getValue();

                    UIDPERFIL.setText(uid);
                    NOMBRESPERFIL.setText(nombre);
                    APELLIDOSPERFIL.setText(apellidos);
                    CORREOPERFIL.setText(correo);
                    PASSWORDPERFIL.setText(password);
                    EDADPERFIL.setText(edad);

                    try {
                        Picasso.get().load(imagen).placeholder(R.drawable.perfil).into(FOTOPERFILIMG);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.perfil).into(FOTOPERFILIMG);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FOTOPERFILIMG.setOnClickListener(task -> CambiarImagenPerfilAdministrador());

        ACTUALIZARPASS.setOnClickListener(task -> {
            startActivity(new Intent(getActivity(), Cambio_Pass.class));
            getActivity().finish();
        });

        ACTUALIZARDATOS.setOnClickListener(task -> {
            EditarDatos();
        });

        return view;
    }

    private void EditarDatos() {
        String[] opciones = {"Editar nombres", "Editar apellidos", "Editar edad"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elegir opción: ");
        builder.setItems(opciones, (dialogInterface, i) -> {
            if (i == 0) {
                EditarNombres();
            } else if (i == 1) {
                EditarApellidos();
            } else if (i == 2) {
                EditarEdad();
            }
        });
        builder.create().show();
    }

    private void EditarEdad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actualizar información: ");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato ...");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String nuevoDato = editText.getText().toString().trim();
            if (!nuevoDato.isEmpty()) {
                int nuevoDatoEntero = Integer.parseInt(nuevoDato);
                HashMap<String, Object> resultado = new HashMap<>();
                resultado.put("EDAD", nuevoDatoEntero);
                BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(resultado)
                        .addOnSuccessListener(task ->
                                Toast.makeText(getActivity(), "Dato actualizado", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(
                                e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show());
        builder.create().show();
    }

    private void EditarApellidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actualizar información: ");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato ...");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String nuevoDato = editText.getText().toString().trim();
            if (!nuevoDato.isEmpty()) {
                HashMap<String, Object> resultado = new HashMap<>();
                resultado.put("APELLIDOS", nuevoDato);
                BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(resultado)
                        .addOnSuccessListener(task ->
                                Toast.makeText(getActivity(), "Dato actualizado", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(
                                e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show());
        builder.create().show();
    }

    private void EditarNombres() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actualizar información: ");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato ...");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", (dialogInterface, i) -> {
            String nuevoDato = editText.getText().toString().trim();
            if (!nuevoDato.isEmpty()) {
                HashMap<String, Object> resultado = new HashMap<>();
                resultado.put("NOMBRES", nuevoDato);
                BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(resultado)
                        .addOnSuccessListener(task ->
                                Toast.makeText(getActivity(), "Dato actualizado", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(
                                e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show());
        builder.create().show();
    }

    private void CambiarImagenPerfilAdministrador() {
        String[] opcion = {"Cambiar foto de perfil"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elegir una opción");

        builder.setItems(opcion, (dialogInterface, i) -> {
            if (i == 0) {
                imagen_perfil = "IMAGEN";
                ElegirFoto();
            }
        });
        builder.create().show();
    }

    private void ElegirFoto() {
        String[] opciones = {"Cámara", "Galería"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, (dialogInterface, i) -> {
            if (i == 0) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Elegir_De_Camara();
                } else {
                    SolicitudPermisoCamara.launch(Manifest.permission.CAMERA);
                }
            } else if (i == 1) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Elegir_De_Galeria();
                } else {
                    SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        builder.create().show();
    }

    private void Elegir_De_Galeria() {
        Intent galeriaIntetn = new Intent(Intent.ACTION_PICK);
        galeriaIntetn.setType("image/*");
        ObtenerImagenGaleria.launch(galeriaIntetn);

    }

    private void Elegir_De_Camara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Foto Temporal");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion Temporal");
        imagen_uri = (requireActivity()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagen_uri);

        ObtenerImagenCamara.launch(camaraIntent);

    }

    private final ActivityResultLauncher<Intent> ObtenerImagenCamara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        ActualizarImagenEnBD(imagen_uri);
                        progressDialog.show();
                    } else {
                        Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        imagen_uri = data.getData();
                        ActualizarImagenEnBD(imagen_uri);
                        progressDialog.show();
                    } else {
                        Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> SolicitudPermisoGaleria = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Elegir_De_Galeria();
        } else {
            Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    });


    private final ActivityResultLauncher<String> SolicitudPermisoCamara = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Elegir_De_Camara();
        } else {
            Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    });

    private void ActualizarImagenEnBD(Uri uri) {
        String Ruta_de_archivo_y_nombre = RutaDeAlamcenamiento + imagen_perfil + "_" + user.getUid();
        StorageReference storageReference2 = storageReference.child(Ruta_de_archivo_y_nombre);
        storageReference2.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            Uri download = uriTask.getResult();

            if (uriTask.isSuccessful()) {
                HashMap<String, Object> results = new HashMap<>();
                results.put(imagen_perfil, download.toString());
                BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(results).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                    getActivity().finish();
                    Toast.makeText(getActivity(), "Imagen cambiada con éxito", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(task -> Toast.makeText(getActivity(), task.getMessage(), Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}