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
import com.serch.fondosdepantalla.MainActivityAdmin;
import com.serch.fondosdepantalla.R;
import com.serch.fondosdepantalla.util.MyProgressDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RegisterAdmin extends Fragment {

    TextView dateRegister;
    EditText email, password, names, lastName, age;
    Button Registrar;

    FirebaseAuth auth;
    MyProgressDialogFragment progressDialogFragment = new MyProgressDialogFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_admin, container, false);

        dateRegister = view.findViewById(R.id.FechaDeRegistro);
        email = view.findViewById(R.id.Correo);
        password = view.findViewById(R.id.Password);
        names = view.findViewById(R.id.Nombres);
        lastName = view.findViewById(R.id.Apellidos);
        age = view.findViewById(R.id.Edad);
        Registrar = view.findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d 'de' MMM 'del' yyyy", Locale.getDefault());
        String dateString = dateFormat.format(date);
        dateRegister.setText(dateString);

        Registrar.setOnClickListener(view1 -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            String namesText = names.getText().toString();
            String lastNamesText = lastName.getText().toString();
            String ageText = age.getText().toString();

            if (emailText.isEmpty() || passwordText.isEmpty() || namesText.isEmpty() || lastNamesText.isEmpty() || ageText.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.message_register_alert), Toast.LENGTH_SHORT).show();
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    email.setError(getString(R.string.email_invalid));
                    email.setFocusable(true);
                } else if (passwordText.length() < 6) {
                    password.setError(getString(R.string.password_invalid));
                    password.setFocusable(true);
                } else {
                    registerAdmin(emailText, passwordText);
                }
            }

        });

        return view;
    }

    private void registerAdmin(String emailRegister, String passwordRegister) {
        progressDialogFragment.show(requireActivity().getSupportFragmentManager(), "progressDialog");

        auth.createUserWithEmailAndPassword(emailRegister, passwordRegister)
                .addOnCompleteListener(task -> {
                    if ((task.isSuccessful())) {
                        progressDialogFragment.dismiss();

                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;

                        String UID = user.getUid();
                        String namesRegister = names.getText().toString();
                        String lastNamesRegister = lastName.getText().toString();
                        String ageRegister = age.getText().toString();
                        int ageIntRegister = Integer.parseInt(ageRegister);

                        HashMap<Object, Object> admins = new HashMap<>();
                        admins.put("UID", UID);
                        admins.put("CORREO", emailRegister);
                        admins.put("PASSWORD", passwordRegister);
                        admins.put("NOMBRES", namesRegister);
                        admins.put("APELLIDOS", lastNamesRegister);
                        admins.put("EDAD", ageIntRegister);
                        admins.put("IMAGEN", "");

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("BASE DE DATOS ADMINISTRADORES");
                        reference.child(UID).setValue(admins);

                        startActivity(new Intent(getActivity(), MainActivityAdmin.class));
                        Toast.makeText(getActivity(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    } else {
                        progressDialogFragment.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(task ->
                        Toast.makeText(getActivity(), "Error: " + task.getMessage(), Toast.LENGTH_SHORT).show());
    }

}