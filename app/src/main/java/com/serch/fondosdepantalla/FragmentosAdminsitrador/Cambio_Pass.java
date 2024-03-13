package com.serch.fondosdepantalla.FragmentosAdminsitrador;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.serch.fondosdepantalla.InicioSesion;
import com.serch.fondosdepantalla.MainActivityAdministrador;
import com.serch.fondosdepantalla.R;
import com.serch.fondosdepantalla.util.MyProgressDialog;

import java.util.HashMap;

public class Cambio_Pass extends AppCompatActivity {

    TextView PassActual;
    EditText ActualPassET, NuevoPassET;
    Button CAMBIARPASSBTN, IRINICIOBTN;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    MyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Cambiar contraseña");

        PassActual = findViewById(R.id.PassActual);
        ActualPassET = findViewById(R.id.ActualPassET);
        NuevoPassET = findViewById(R.id.NuevoPassET);
        CAMBIARPASSBTN = findViewById(R.id.CAMBIARPASSBTN);
        IRINICIOBTN = findViewById(R.id.IRINICIOBTN);

        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressDialog = new MyProgressDialog(this);

        Query query = BASE_DE_DATOS_ADMINISTRADORES.orderByChild("CORREO").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String pass = "" + ds.child("PASSWORD").getValue();
                    PassActual.setText(pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CAMBIARPASSBTN.setOnClickListener(task -> {
            String ACTUAL_PASS = ActualPassET.getText().toString().trim();
            String NUEVO_PASS = NuevoPassET.getText().toString().trim();

            if (TextUtils.isEmpty(ACTUAL_PASS)) {
                Toast.makeText(this, "El campo contraseña actual está vacío", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(NUEVO_PASS)) {
                Toast.makeText(this, "El campo contraseña nueva está vacío", Toast.LENGTH_SHORT).show();
            }
            if (!ACTUAL_PASS.isEmpty() && !NUEVO_PASS.isEmpty() && NUEVO_PASS.length() >= 6) {
                Cambio_Password(ACTUAL_PASS, NUEVO_PASS);
            } else {
                NuevoPassET.setError("La contraseña debe ser mayor o igual a 6");
                NuevoPassET.setFocusable(true);
            }
        });

        IRINICIOBTN.setOnClickListener(task -> startActivity(new Intent(this, MainActivityAdministrador.class)));
    }

    private void Cambio_Password(String pass_actual, String nuevo_pass) {
        progressDialog.show();

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), pass_actual);
        user.reauthenticate(authCredential).addOnSuccessListener(unused -> user.updatePassword(nuevo_pass).addOnSuccessListener(unused1 -> {
            progressDialog.dismiss();
            String NUEVO_PASS = NuevoPassET.getText().toString().trim();
            HashMap<String, Object> resultado = new HashMap<>();
            resultado.put("PASSWORD", NUEVO_PASS);
            BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(resultado).addOnSuccessListener(unused11 -> {
                Toast.makeText(Cambio_Pass.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(Cambio_Pass.this, InicioSesion.class));
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        })).addOnFailureListener(e -> {
            Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

}