package com.serch.fondosdepantalla.FragmentosAdminsitrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.serch.fondosdepantalla.CategoriasAdmin.MusicaA;
import com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA;
import com.serch.fondosdepantalla.CategoriasAdmin.SeriesA;
import com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA;
import com.serch.fondosdepantalla.R;

public class InicioAdmin extends Fragment {

    Button Peliculas, Series, Musica, Videojuegos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        Peliculas = view.findViewById(R.id.Peliculas);
        Series = view.findViewById(R.id.Series);
        Musica = view.findViewById(R.id.Musica);
        Videojuegos = view.findViewById(R.id.Videojuegos);

        Peliculas.setOnClickListener(task -> startActivity(new Intent(getActivity(), PeliculasA.class)));
        Series.setOnClickListener(task -> startActivity(new Intent(getActivity(), SeriesA.class)));
        Musica.setOnClickListener(task -> startActivity(new Intent(getActivity(), MusicaA.class)));
        Videojuegos.setOnClickListener(task -> startActivity(new Intent(getActivity(), VideojuegosA.class)));

        return view;
    }
}