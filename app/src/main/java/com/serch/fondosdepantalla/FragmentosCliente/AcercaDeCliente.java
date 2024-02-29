package com.serch.fondosdepantalla.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.serch.fondosdepantalla.InicioSesion;
import com.serch.fondosdepantalla.R;

public class AcercaDeCliente extends Fragment {

    Button Acceder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acerca_de_cliente, container, false);

        Acceder = view.findViewById(R.id.Acceder);
        Acceder.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), InicioSesion.class)));

        return view;
    }
}