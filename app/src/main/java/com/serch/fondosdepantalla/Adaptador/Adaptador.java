package com.serch.fondosdepantalla.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.Detalle.Detalle_Administrador;
import com.serch.fondosdepantalla.Modelo.Administrador;
import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder> {

    private final Context context;
    private final List<Administrador> administradores;

    public Adaptador(Context context, List<Administrador> administradores) {
        this.context = context;
        this.administradores = administradores;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String UID = administradores.get(position).getUID();
        String IMAGEN = administradores.get(position).getIMAGEN();
        String NOMBRES = administradores.get(position).getNOMBRES();
        String APELLIDOS = administradores.get(position).getAPELLIDOS();
        String CORREO = administradores.get(position).getCORREO();
        int EDAD = administradores.get(position).getEDAD();
        String EdadString = String.valueOf(EDAD);

        holder.NombresADMIN.setText(NOMBRES);
        holder.CorreoADMIN.setText(CORREO);

        try {
            Picasso.get().load(IMAGEN).placeholder(R.drawable.admin_item).into(holder.PerfilADMIN);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.admin_item).into(holder.PerfilADMIN);
        }

        holder.itemView.setOnClickListener(task -> {
            Intent intent = new Intent(context, Detalle_Administrador.class);
            intent.putExtra("UID", UID);
            intent.putExtra("NOMBRES", NOMBRES);
            intent.putExtra("APELLIDOS", APELLIDOS);
            intent.putExtra("CORREO", CORREO);
            intent.putExtra("EDAD", EdadString);
            intent.putExtra("IMAGEN", IMAGEN);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return administradores.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView PerfilADMIN;
        TextView NombresADMIN, CorreoADMIN;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            PerfilADMIN = itemView.findViewById(R.id.PerfilADMIN);
            NombresADMIN = itemView.findViewById(R.id.NombresADMIN);
            CorreoADMIN = itemView.findViewById(R.id.CorreoADMIN);

        }
    }
}
