package com.serch.fondosdepantalla.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.Detalle.DetailAdmin;
import com.serch.fondosdepantalla.Modelo.Administrador;
import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {

    private final Context context;
    private final List<Administrador> administradorList;

    public Adapter(Context context, List<Administrador> administradorList) {
        this.context = context;
        this.administradorList = administradorList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String UID = administradorList.get(position).getUID();
        String IMAGE = administradorList.get(position).getIMAGEN();
        String NAMES = administradorList.get(position).getNOMBRES();
        String LAST_NAME = administradorList.get(position).getAPELLIDOS();
        String EMAIL = administradorList.get(position).getCORREO();
        int AGE = administradorList.get(position).getEDAD();
        String EdadString = String.valueOf(AGE);

        holder.nameAdmin.setText(NAMES);
        holder.emailAdmin.setText(EMAIL);

        try {
            Picasso.get().load(IMAGE).placeholder(R.drawable.admin_item).into(holder.profileAdmin);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.admin_item).into(holder.profileAdmin);
        }

        holder.itemView.setOnClickListener(task -> {
            Intent intent = new Intent(context, DetailAdmin.class);
            intent.putExtra("UID", UID);
            intent.putExtra("NAMES", NAMES);
            intent.putExtra("LAST_NAME", LAST_NAME);
            intent.putExtra("EMAIL", EMAIL);
            intent.putExtra("AGE", EdadString);
            intent.putExtra("IMAGE", IMAGE);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return administradorList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView profileAdmin;
        TextView nameAdmin, emailAdmin;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileAdmin = itemView.findViewById(R.id.PerfilADMIN);
            nameAdmin = itemView.findViewById(R.id.NombresADMIN);
            emailAdmin = itemView.findViewById(R.id.CorreoADMIN);
        }
    }

}
