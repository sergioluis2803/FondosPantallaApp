package com.serch.fondosdepantalla.FragmentosAdminsitrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.serch.fondosdepantalla.MainActivityAdministrador;
import com.serch.fondosdepantalla.R;
import com.serch.fondosdepantalla.util.MyProgressDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RegistrarAdmin extends Fragment {

    TextView FechaDeRegistro;
    EditText Correo, Password, Nombres, Apellidos, Edad;
    Button Registrar;

    FirebaseAuth auth;
    MyProgressDialogFragment progressDialogFragment = new MyProgressDialogFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_admin, container, false);

        FechaDeRegistro = view.findViewById(R.id.FechaDeRegistro);
        Correo = view.findViewById(R.id.Correo);
        Password = view.findViewById(R.id.Password);
        Nombres = view.findViewById(R.id.Nombres);
        Apellidos = view.findViewById(R.id.Apellidos);
        Edad = view.findViewById(R.id.Edad);
        Registrar = view.findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMM 'del' yyyy", Locale.getDefault());
        String SFecha = fecha.format(date);
        FechaDeRegistro.setText(SFecha);

        Registrar.setOnClickListener(view1 -> {
            String correo = Correo.getText().toString();
            String pass = Password.getText().toString();
            String nombres = Nombres.getText().toString();
            String apellidos = Apellidos.getText().toString();
            String edad = Edad.getText().toString();

            if (correo.isEmpty() || pass.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || edad.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    Correo.setError("Correo inválido");
                    Correo.setFocusable(true);
                } else if (pass.length() < 6) {
                    Password.setError("Contraseña debe ser mayor o igual a 6");
                    Password.setFocusable(true);
                } else {
                    RegistroAdministradores(correo, pass);
                }
            }

        });

        return view;
    }

    private void RegistroAdministradores(String correo, String pass) {
        progressDialogFragment.show(requireActivity().getSupportFragmentManager(), "progressDialog");

        auth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(task -> {
                    if ((task.isSuccessful())) {
                        progressDialogFragment.dismiss();

                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;

                        String UID = user.getUid();
                        String nombres = Nombres.getText().toString();
                        String apellidos = Apellidos.getText().toString();
                        String edad = Edad.getText().toString();
                        int EdadInt = Integer.parseInt(edad);

                        HashMap<Object, Object> Administradores = new HashMap<>();
                        Administradores.put("UID", UID);
                        Administradores.put("CORREO", correo);
                        Administradores.put("PASSWORD", pass);
                        Administradores.put("NOMBRES", nombres);
                        Administradores.put("APELLIDOS", apellidos);
                        Administradores.put("EDAD", EdadInt);
                        Administradores.put("IMAGEN", "");

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("BASE DE DATOS ADMINISTRADORES");
                        reference.child(UID).setValue(Administradores);

                        startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                        Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();

                    } else {
                        progressDialogFragment.dismiss();
                        Toast.makeText(getActivity(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(task -> Toast.makeText(getActivity(), "Error: " + task.getMessage(), Toast.LENGTH_SHORT).show());
    }

}