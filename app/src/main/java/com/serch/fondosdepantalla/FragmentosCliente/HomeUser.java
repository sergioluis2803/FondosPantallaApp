package com.serch.fondosdepantalla.FragmentosCliente;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.serch.fondosdepantalla.Apartado_Informativo.Informacion;
import com.serch.fondosdepantalla.Apartado_Informativo.ViewHolderInformacion;
import com.serch.fondosdepantalla.Categorias.Cat_Dispositivo.CategoriaD;
import com.serch.fondosdepantalla.Categorias.Cat_Dispositivo.ViewHolderCD;
import com.serch.fondosdepantalla.Categorias.Cat_Firebase.CategoriaF;
import com.serch.fondosdepantalla.Categorias.Cat_Firebase.ViewHolderCF;
import com.serch.fondosdepantalla.Categorias.ControladorCD;
import com.serch.fondosdepantalla.CategoriasClienteFirebase.ListaCategoriaFirebase;
import com.serch.fondosdepantalla.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeUser extends Fragment {

    RecyclerView rvCategoryDevices, rvCategoryFirebase, rvInfo;
    FirebaseDatabase firebaseDatabase, firebaseDatabaseF, firebaseDatabaseInfo;
    DatabaseReference databaseReference, databaseReferenceF, databaseReferenceInfo;
    LinearLayoutManager linearLayoutManager, linearLayoutManagerF, linearLayoutManagerInfo;
    FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD> firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<CategoriaF, ViewHolderCF> firebaseRecyclerAdapterF;
    FirebaseRecyclerAdapter<Informacion, ViewHolderInformacion> firebaseRecyclerAdapterInfo;
    FirebaseRecyclerOptions<CategoriaD> optionsD;
    FirebaseRecyclerOptions<CategoriaF> optionsF;
    FirebaseRecyclerOptions<Informacion> optionsInfo;

    TextView fecha;

    LinearLayoutCompat ConCOnexion, SinConexion;

    ConnectivityManager.NetworkCallback networkCallback;
    ConnectivityManager connectivityManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

        ConCOnexion = view.findViewById(R.id.ConConexion);
        SinConexion = view.findViewById(R.id.SinConexion);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseF = FirebaseDatabase.getInstance();
        firebaseDatabaseInfo = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("CATEGORIAS_D");
        databaseReferenceF = firebaseDatabaseF.getReference("CATEGORIAS_F");
        databaseReferenceInfo = firebaseDatabaseInfo.getReference("INFORMACION");

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerF = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerInfo = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvCategoryDevices = view.findViewById(R.id.rvCategoryDevices);
        rvCategoryDevices.setHasFixedSize(true);
        rvCategoryDevices.setLayoutManager(linearLayoutManager);

        rvCategoryFirebase = view.findViewById(R.id.rvCategoryFirebase);
        rvCategoryFirebase.setHasFixedSize(true);
        rvCategoryFirebase.setLayoutManager(linearLayoutManagerF);

        rvInfo = view.findViewById(R.id.rvInfo);
        rvInfo.setHasFixedSize(true);
        rvInfo.setLayoutManager(linearLayoutManagerInfo);

        fecha = view.findViewById(R.id.fecha);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String stringDate = simpleDateFormat.format(date);
        fecha.setText(stringDate);

        showCategoryDevice();
        showCategoryFirebase();
        showInfo();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(@NonNull Network network) {
                //EXISTE CONEXION DE RED DISPONIBLE

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConCOnexion.setVisibility(View.VISIBLE);
                        SinConexion.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onLost(@NonNull Network network) {
                //SE PERDIO LA CONEXION DE INTERNET

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConCOnexion.setVisibility(View.GONE);
                        SinConexion.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        NetworkRequest networkRequest = new NetworkRequest.Builder().build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private void showInfo() {
        optionsInfo = new FirebaseRecyclerOptions.Builder<Informacion>().setQuery(databaseReferenceInfo, Informacion.class).build();
        firebaseRecyclerAdapterInfo = new FirebaseRecyclerAdapter<Informacion, ViewHolderInformacion>(optionsInfo) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderInformacion viewHolderInformacion, int i, @NonNull Informacion informacion) {
                viewHolderInformacion.setInformation(getActivity(), informacion.getNombre(), informacion.getImagen());
            }

            @NonNull
            @Override
            public ViewHolderInformacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apartado_informativo, parent, false);
                ViewHolderInformacion viewHolderInformacion = new ViewHolderInformacion(itemView);
                viewHolderInformacion.setOnClickListener((view, position) -> {
                        }
                );
                return viewHolderInformacion;
            }
        };

        rvInfo.setAdapter(firebaseRecyclerAdapterInfo);
    }

    private void showCategoryFirebase() {
        optionsF = new FirebaseRecyclerOptions.Builder<CategoriaF>().setQuery(databaseReferenceF, CategoriaF.class).build();
        firebaseRecyclerAdapterF = new FirebaseRecyclerAdapter<CategoriaF, ViewHolderCF>(optionsF) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCF viewHolderCD, int i, @NonNull CategoriaF categoriaF) {
                viewHolderCD.setCategoryFirebase(getActivity(), categoriaF.getCategoria(), categoriaF.getImagen());
            }

            @NonNull
            @Override
            public ViewHolderCF onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_firebase, parent, false);
                ViewHolderCF viewHolderCF = new ViewHolderCF(itemView);
                viewHolderCF.setOnClickListener((view, position) -> {
                            String category = getItem(position).getCategoria();
                            Intent intent = new Intent(view.getContext(), ListaCategoriaFirebase.class);
                            intent.putExtra("NOMBRE_CATEGORIA", category);
                            startActivity(intent);
                        }
                );
                return viewHolderCF;
            }
        };

        rvCategoryFirebase.setAdapter(firebaseRecyclerAdapterF);
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
        if (firebaseRecyclerAdapter != null && firebaseRecyclerAdapterF != null && firebaseRecyclerAdapterInfo != null) {
            firebaseRecyclerAdapter.startListening();
            firebaseRecyclerAdapterF.startListening();
            firebaseRecyclerAdapterInfo.startListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }
}