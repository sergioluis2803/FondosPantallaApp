package com.serch.fondosdepantalla.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.serch.fondosdepantalla.R;

public class Share extends Fragment {

    Button BotonCompartir;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compartir_cliente, container, false);

        BotonCompartir = view.findViewById(R.id.BotonCompartir);
        BotonCompartir.setOnClickListener(task -> CompartirApp());

        return view;
    }

    private void CompartirApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));

            String message = "Hola, te invito a descargar esta app";
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}