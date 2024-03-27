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
import com.serch.fondosdepantalla.MainActivityAdmin;
import com.serch.fondosdepantalla.R;
import com.serch.fondosdepantalla.util.MyProgressDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileAdmin extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference DB_ADMIN;
    StorageReference storageReference;

    String storagePath = "Fotos_Perfil_Administradores/*";
    private Uri imageUri;
    private String imageProfile;
    private MyProgressDialogFragment progressDialog;
    ImageView photoProfile;
    TextView uidTextProfile, nameTextProfile, lastNameProfile, emailTextProfile, passwordTextProfile, ageTextProfile;
    Button updatePassword, updateData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        photoProfile = view.findViewById(R.id.FOTOPERFILIMG);
        uidTextProfile = view.findViewById(R.id.UIDPERFIL);
        nameTextProfile = view.findViewById(R.id.NOMBRESPERFIL);
        lastNameProfile = view.findViewById(R.id.APELLIDOSPERFIL);
        emailTextProfile = view.findViewById(R.id.CORREOPERFIL);
        passwordTextProfile = view.findViewById(R.id.PASSWORDPERFIL);
        ageTextProfile = view.findViewById(R.id.EDADPERFIL);
        updatePassword = view.findViewById(R.id.ACTUALIZARPASS);
        updateData = view.findViewById(R.id.ACTUALIZARDATOS);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        storageReference = getInstance().getReference();

        progressDialog = new MyProgressDialogFragment();

        DB_ADMIN = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        DB_ADMIN.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String uid = "" + snapshot.child("UID").getValue();
                    String names = "" + snapshot.child("NOMBRES").getValue();
                    String lastName = "" + snapshot.child("APELLIDOS").getValue();
                    String email = "" + snapshot.child("CORREO").getValue();
                    String password = "" + snapshot.child("PASSWORD").getValue();
                    String age = "" + snapshot.child("EDAD").getValue();
                    String image = "" + snapshot.child("IMAGEN").getValue();

                    uidTextProfile.setText(uid);
                    nameTextProfile.setText(names);
                    lastNameProfile.setText(lastName);
                    emailTextProfile.setText(email);
                    passwordTextProfile.setText(password);
                    ageTextProfile.setText(age);

                    try {
                        Picasso.get().load(image).placeholder(R.drawable.perfil).into(photoProfile);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.perfil).into(photoProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        photoProfile.setOnClickListener(task -> changeImageProfileAdmin());

        updatePassword.setOnClickListener(task -> {
            startActivity(new Intent(getActivity(), ChangePassword.class));
            getActivity().finish();
        });

        updateData.setOnClickListener(task -> editData());

        return view;
    }

    private void editData() {
        String[] options = getResources().getStringArray(R.array.options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.choose_option));
        builder.setItems(options, (dialogInterface, i) -> {
            switch (i) {
                case 0:
                    editChooseOption("NOMBRES", InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                    break;
                case 1:
                    editChooseOption("APELLIDOS", InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                    break;
                case 2:
                    editChooseOption("EDAD", InputType.TYPE_CLASS_NUMBER);
                    break;
            }
        });
        builder.create().show();
    }

    private void editChooseOption(String updateColumn, int input) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.title_choose_option));

        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);

        EditText editText = new EditText(getActivity());
        editText.setHint(getString(R.string.enter_new_data));
        editText.setInputType(input);

        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton(getString(R.string.button_positive), (dialogInterface, i) -> {
            String newData = editText.getText().toString().trim();
            if (!newData.isEmpty()) {
                HashMap<String, Object> result = new HashMap<>();
                if (updateColumn.equals("EDAD")) {
                    int newDataInt = Integer.parseInt(newData);
                    result.put(updateColumn, newDataInt);
                } else {
                    result.put(updateColumn, newData);
                }

                DB_ADMIN.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(task ->
                                Toast.makeText(getActivity(), getString(R.string.new_data_update), Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(
                                e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(getActivity(), getString(R.string.text_empty), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.button_negative), (dialogInterface, i) ->
                Toast.makeText(getActivity(), getString(R.string.message_cancel), Toast.LENGTH_SHORT).show());

        builder.create().show();
    }

    private void changeImageProfileAdmin() {
        String[] options = {"Cambiar foto de perfil"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.choose_option));

        builder.setItems(options, (dialogInterface, i) -> {
            if (i == 0) {
                imageProfile = "IMAGEN";
                choosePhoto();
            }
        });
        builder.create().show();
    }

    private void choosePhoto() {
        String[] options = getResources().getStringArray(R.array.choose_photo_option);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.select_photo_from));
        builder.setItems(options, (dialogInterface, i) -> {
            if (i == 0) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    chooseCamara();
                } else {
                    requestPermissionCamara.launch(Manifest.permission.CAMERA);
                }
            } else if (i == 1) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    chooseGallery();
                } else {
                    requestPermissionGallery.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        builder.create().show();
    }

    private void chooseGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        getImageGallery.launch(galleryIntent);
    }

    private void chooseCamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Foto Temporal");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion Temporal");
        imageUri = (requireActivity()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        getImageCamara.launch(camaraIntent);
    }

    private final ActivityResultLauncher<Intent> getImageCamara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        updateImageBD(imageUri);
                        progressDialog.show(requireActivity().getSupportFragmentManager(), "progressDialog");
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.message_cancel), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> getImageGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        imageUri = data.getData();
                        updateImageBD(imageUri);
                        progressDialog.show(requireActivity().getSupportFragmentManager(), "progressDialog");
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.message_cancel), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> requestPermissionGallery = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    chooseGallery();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> requestPermissionCamara = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    chooseCamara();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
            });

    private void updateImageBD(Uri uri) {
        String pathFileName = storagePath + imageProfile + "_" + user.getUid();
        StorageReference storageReference2 = storageReference.child(pathFileName);
        storageReference2.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            Uri download = uriTask.getResult();

            if (uriTask.isSuccessful()) {
                HashMap<String, Object> results = new HashMap<>();
                results.put(imageProfile, download.toString());
                DB_ADMIN.child(user.getUid()).updateChildren(results).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    startActivity(new Intent(getActivity(), MainActivityAdmin.class));
                    getActivity().finish();
                    Toast.makeText(getActivity(), getString(R.string.change_image_success), Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(task -> Toast.makeText(getActivity(), task.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}