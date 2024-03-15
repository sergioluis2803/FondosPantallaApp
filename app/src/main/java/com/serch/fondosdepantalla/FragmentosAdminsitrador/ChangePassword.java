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
import com.serch.fondosdepantalla.LoginSession;
import com.serch.fondosdepantalla.MainActivityAdmin;
import com.serch.fondosdepantalla.R;
import com.serch.fondosdepantalla.util.MyProgressDialog;

import java.util.HashMap;

public class ChangePassword extends AppCompatActivity {

    TextView PassActual;
    EditText ActualPassET, NuevoPassET;
    Button changePasswordBtn, homeBtn;
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
        actionBar.setTitle(getString(R.string.change_password));

        PassActual = findViewById(R.id.PassActual);
        ActualPassET = findViewById(R.id.ActualPassET);
        NuevoPassET = findViewById(R.id.NuevoPassET);
        changePasswordBtn = findViewById(R.id.CAMBIARPASSBTN);
        homeBtn = findViewById(R.id.IRINICIOBTN);

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

        changePasswordBtn.setOnClickListener(task -> {
            String ACTUAL_PASS = ActualPassET.getText().toString().trim();
            String NUEVO_PASS = NuevoPassET.getText().toString().trim();

            if (TextUtils.isEmpty(ACTUAL_PASS)) {
                Toast.makeText(this, getString(R.string.password_text_empty), Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(NUEVO_PASS)) {
                Toast.makeText(this, getString(R.string.passwordNew_text_empty), Toast.LENGTH_SHORT).show();
            }
            if (!ACTUAL_PASS.isEmpty() && !NUEVO_PASS.isEmpty() && NUEVO_PASS.length() >= 6) {
                changePassword(ACTUAL_PASS, NUEVO_PASS);
            } else {
                NuevoPassET.setError(getString(R.string.password_invalid));
                NuevoPassET.setFocusable(true);
            }
        });

        homeBtn.setOnClickListener(task -> startActivity(new Intent(this, MainActivityAdmin.class)));
    }

    private void changePassword(String pass_actual, String newPassword) {
        progressDialog.show();

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), pass_actual);
        user.reauthenticate(authCredential).addOnSuccessListener(unused -> user.updatePassword(newPassword).addOnSuccessListener(unused1 -> {
            progressDialog.dismiss();
            String NUEVO_PASS = NuevoPassET.getText().toString().trim();
            HashMap<String, Object> result = new HashMap<>();
            result.put("PASSWORD", NUEVO_PASS);
            BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(result).addOnSuccessListener(unused11 -> {
                Toast.makeText(ChangePassword.this, getString(R.string.password_success_change), Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(ChangePassword.this, LoginSession.class));
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        })).addOnFailureListener(e -> {
            Toast.makeText(ChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

}