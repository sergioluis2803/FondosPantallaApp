package com.serch.fondosdepantalla.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.serch.fondosdepantalla.LoginSession;
import com.serch.fondosdepantalla.R;

public class Info extends Fragment {

    Button Login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acerca_de_cliente, container, false);

        Login = view.findViewById(R.id.Acceder);
        Login.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), LoginSession.class)));

        return view;
    }
}