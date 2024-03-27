package com.serch.fondosdepantalla.FragmentosAdminsitrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serch.fondosdepantalla.CategoriasAdmin.MusicaA.MusicaA;
import com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA.PeliculasA;
import com.serch.fondosdepantalla.CategoriasAdmin.SeriesA.SeriesA;
import com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA.VideojuegosA;
import com.serch.fondosdepantalla.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;

public class HomeAdmin extends Fragment {

    Button movies, series, music, videoGames;

    TextView fechaAdmin, NombreTXT;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        movies = view.findViewById(R.id.Peliculas);
        series = view.findViewById(R.id.Series);
        music = view.findViewById(R.id.Musica);
        videoGames = view.findViewById(R.id.Videojuegos);

        fechaAdmin = view.findViewById(R.id.fechaAdmin);
        NombreTXT = view.findViewById(R.id.NombreTXT);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String stringDate = simpleDateFormat.format(date);
        fechaAdmin.setText("Hoy es: " + stringDate);

        movies.setOnClickListener(task -> startActivity(new Intent(getActivity(), PeliculasA.class)));
        series.setOnClickListener(task -> startActivity(new Intent(getActivity(), SeriesA.class)));
        music.setOnClickListener(task -> startActivity(new Intent(getActivity(), MusicaA.class)));
        videoGames.setOnClickListener(task -> startActivity(new Intent(getActivity(), VideojuegosA.class)));

        return view;
    }

    private void loadData() {
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = "" + snapshot.child("NOMBRES").getValue();
                    NombreTXT.setText(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void comprobarUserActive() {
        if (user != null) {
            loadData();
        }
    }

    @Override
    public void onStart() {
        comprobarUserActive();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
    }

}