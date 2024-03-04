package com.serch.fondosdepantalla.CategoriasAdmin.MusicaA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderMusica extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolderMusica.ClickListener mClickListener;

    public interface ClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderMusica.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderMusica(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(view -> mClickListener.OnItemClick(view, getBindingAdapterPosition()));

        itemView.setOnLongClickListener(view -> {
            mClickListener.OnItemLongClick(view, getBindingAdapterPosition());
            return true;
        });
    }


    public void SeteoMusica(Context context, String nombre, int vista, String imagen) {
        ImageView Imagen_Musica;
        TextView NombreImagen_Musica;
        TextView Vista_Musica;

        Imagen_Musica = mView.findViewById(R.id.ImagenMusica);
        NombreImagen_Musica = mView.findViewById(R.id.NombreImagenMusica);
        Vista_Musica = mView.findViewById(R.id.VistaMusica);

        NombreImagen_Musica.setText(nombre);
        String VistaString = String.valueOf(vista);
        Vista_Musica.setText(VistaString);

        try {
            Picasso.get().load(imagen).into(Imagen_Musica);
        } catch (Exception e) {
            Toast.makeText(context, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
