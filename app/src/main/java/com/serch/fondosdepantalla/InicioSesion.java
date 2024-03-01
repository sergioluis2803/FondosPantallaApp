package com.serch.fondosdepantalla;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.serch.fondosdepantalla.util.MyProgressDialogFragment;

public class InicioSesion extends AppCompatActivity {

    EditText Correo, Password;
    Button Acceder;
    FirebaseAuth firebaseAuth;
    MyProgressDialogFragment progressDialogFragment = new MyProgressDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Inicio sesión");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Correo = findViewById(R.id.Correo);
        Password = findViewById(R.id.Password);
        Acceder = findViewById(R.id.Acceder);

        firebaseAuth = FirebaseAuth.getInstance();

        Acceder.setOnClickListener(task -> {
            String correo = Correo.getText().toString();
            String pass = Password.getText().toString();

            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    Correo.setError("Correo inválido");
                    Correo.setFocusable(true);
                } else if (pass.length() < 6) {
                    Password.setError("Contraseña debe ser mayor o igual a 6");
                    Password.setFocusable(true);
                } else {
                    LogeoAdministradores(correo, pass);
                }
            }
        });
    }

    private void LogeoAdministradores(String correo, String pass) {
        progressDialogFragment.show(this.getSupportFragmentManager(), "progressDialog");

        firebaseAuth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialogFragment.dismiss();
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        startActivity(new Intent(this, MainActivityAdministrador.class));
                        assert user != null;
                        Toast.makeText(this, "Bienvenido(a) " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialogFragment.dismiss();
                        UsuarioInvalido();
                    }
                })
                .addOnFailureListener(task -> {
                    progressDialogFragment.dismiss();
                    UsuarioInvalido();
                });
    }

    private void UsuarioInvalido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InicioSesion.this);
        builder.setCancelable(false);
        builder.setTitle("OCURRIO UN ERROR");
        builder.setMessage("VERIFIQUE SU CORREO Y CONTRASEÑA").setPositiveButton("Entendido", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}