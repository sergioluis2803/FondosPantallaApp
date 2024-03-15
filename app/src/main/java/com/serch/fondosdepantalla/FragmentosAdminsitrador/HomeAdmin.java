package com.serch.fondosdepantalla.FragmentosAdminsitrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.serch.fondosdepantalla.CategoriasAdmin.MusicaA.MusicaA;
import com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA.PeliculasA;
import com.serch.fondosdepantalla.CategoriasAdmin.SeriesA.SeriesA;
import com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA.VideojuegosA;
import com.serch.fondosdepantalla.R;

public class HomeAdmin extends Fragment {

    Button movies, series, music, videoGames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        movies = view.findViewById(R.id.Peliculas);
        series = view.findViewById(R.id.Series);
        music = view.findViewById(R.id.Musica);
        videoGames = view.findViewById(R.id.Videojuegos);

        movies.setOnClickListener(task -> startActivity(new Intent(getActivity(), PeliculasA.class)));
        series.setOnClickListener(task -> startActivity(new Intent(getActivity(), SeriesA.class)));
        music.setOnClickListener(task -> startActivity(new Intent(getActivity(), MusicaA.class)));
        videoGames.setOnClickListener(task -> startActivity(new Intent(getActivity(), VideojuegosA.class)));

        return view;
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