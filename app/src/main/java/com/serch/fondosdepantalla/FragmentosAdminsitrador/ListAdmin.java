package com.serch.fondosdepantalla.FragmentosAdminsitrador;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serch.fondosdepantalla.Adaptador.Adapter;
import com.serch.fondosdepantalla.Modelo.Administrador;
import com.serch.fondosdepantalla.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdmin extends Fragment {

    RecyclerView administradores_recyclerView;
    Adapter adapter;
    List<Administrador> listAdmin;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_admin, container, false);

        administradores_recyclerView = view.findViewById(R.id.administradores_recyclerView);
        administradores_recyclerView.setHasFixedSize(true);
        administradores_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        listAdmin = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        getListResult();

        return view;
    }

    private void getListResult() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        reference.orderByChild("APELLIDOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listAdmin.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Administrador administrador = ds.getValue(Administrador.class);

                    assert administrador != null;
                    assert user != null;
                    if (!administrador.getUID().equals(user.getUid())) {
                        listAdmin.add(administrador);
                    }
                    adapter = new Adapter(getActivity(), listAdmin);
                    administradores_recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void searchAdmin(String adminSearch) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        reference.orderByChild("APELLIDOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listAdmin.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Administrador administrador = ds.getValue(Administrador.class);

                    assert administrador != null;
                    assert user != null;
                    if (!administrador.getUID().equals(user.getUid())) {
                        if (administrador.getNOMBRES().toLowerCase().contains(adminSearch.toLowerCase()) ||
                                administrador.getCORREO().toLowerCase().contains(adminSearch.toLowerCase())) {
                            listAdmin.add(administrador);
                        }
                    }
                    adapter = new Adapter(getActivity(), listAdmin);
                    administradores_recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscar, menu);
        MenuItem item = menu.findItem(R.id.buscar_administrador);

        SearchView searchView = (SearchView) item.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchAdmin(query);
                } else {
                    getListResult();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchAdmin(query);
                } else {
                    getListResult();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}