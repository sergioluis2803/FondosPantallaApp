package com.serch.fondosdepantalla.FragmentosCliente;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.serch.fondosdepantalla.R;

public class Info extends Fragment {

    TextView ir_linkedin, ir_twitter, ir_instagram, ir_github;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acerca_de_cliente, container, false);

        ir_linkedin = view.findViewById(R.id.ir_linkedin);
        ir_twitter = view.findViewById(R.id.ir_twitter);
        ir_instagram = view.findViewById(R.id.ir_instagram);
        ir_github = view.findViewById(R.id.ir_github);


        ir_linkedin.setOnClickListener(task -> {
                    Uri uri = Uri.parse("https://www.linkedin.com/in/sergio-mostacero-91762221b/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
        );
        ir_twitter.setOnClickListener(task -> {
            Uri uri = Uri.parse("https://twitter.com/SergioL41593868");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        ir_instagram.setOnClickListener(task -> {
            Uri uri = Uri.parse("https://www.instagram.com/serch.lmv/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        ir_github.setOnClickListener(task -> {
            Uri uri = Uri.parse("https://github.com/sergioluis2803");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        return view;
    }
}