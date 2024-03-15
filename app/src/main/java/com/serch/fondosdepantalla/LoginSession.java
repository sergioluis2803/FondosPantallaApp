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
import com.serch.fondosdepantalla.util.MyProgressDialog;

public class LoginSession extends AppCompatActivity {

    EditText Email, Password;
    Button Login;
    FirebaseAuth firebaseAuth;
    MyProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.login);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Email = findViewById(R.id.Correo);
        Password = findViewById(R.id.Password);
        Login = findViewById(R.id.Acceder);

        firebaseAuth = FirebaseAuth.getInstance();

        myProgressDialog = new MyProgressDialog(this);

        Login.setOnClickListener(task -> {
            String email = Email.getText().toString();
            String pass = Password.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, R.string.empty_box, Toast.LENGTH_SHORT).show();
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Email.setError(getString(R.string.email_invalid));
                    Email.setFocusable(true);
                } else if (pass.length() < 6) {
                    Password.setError(getString(R.string.password_invalid));
                    Password.setFocusable(true);
                } else {
                    signInAdmin(email, pass);
                }
            }
        });
    }

    private void signInAdmin(String email, String pass) {
        myProgressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        myProgressDialog.dismiss();
                        startActivity(new Intent(this, MainActivityAdmin.class));
                        finish();
                    } else {
                        myProgressDialog.dismiss();
                        userInvalid();
                    }
                })
                .addOnFailureListener(task -> {
                    myProgressDialog.dismiss();
                    userInvalid();
                });
    }

    private void userInvalid() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginSession.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.error));
        builder.setMessage(getString(R.string.error_credentials)).setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}