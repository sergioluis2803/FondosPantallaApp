package com.serch.fondosdepantalla.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.serch.fondosdepantalla.Categorias.Cat_Dispositivo.CategoriaD;
import com.serch.fondosdepantalla.Categorias.Cat_Dispositivo.ViewHolderCD;
import com.serch.fondosdepantalla.Categorias.ControladorCD;
import com.serch.fondosdepantalla.R;

public class HomeUser extends Fragment {

    RecyclerView rvCategoryDevices;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<CategoriaD> optionsD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CATEGORIAS_D");

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvCategoryDevices = view.findViewById(R.id.rvCategoryDevices);
        rvCategoryDevices.setHasFixedSize(true);
        rvCategoryDevices.setLayoutManager(linearLayoutManager);

        showCategoryDevice();

        return view;
    }

    private void showCategoryDevice() {
        optionsD = new FirebaseRecyclerOptions.Builder<CategoriaD>().setQuery(databaseReference, CategoriaD.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD>(optionsD) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCD viewHolderCD, int i, @NonNull CategoriaD categoriaD) {
                viewHolderCD.setCategoryDis(getActivity(), categoriaD.getCategoria(), categoriaD.getImagen());
            }

            @NonNull
            @Override
            public ViewHolderCD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_dispositivo, parent, false);
                ViewHolderCD viewHolderCD = new ViewHolderCD(itemView);
                viewHolderCD.setOnClickListener((view, position) -> {
                            String category = getItem(position).getCategoria();
                            Intent intent = new Intent(view.getContext(), ControladorCD.class);
                            intent.putExtra("Categoria", category);
                            startActivity(intent);
                            Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
                        }
                );
                return viewHolderCD;
            }
        };

        rvCategoryDevices.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }
}