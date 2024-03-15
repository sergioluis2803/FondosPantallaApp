package com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderVideojuego extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderVideojuego.ClickListener mClickListener;

    public interface ClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderVideojuego.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderVideojuego(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(view -> mClickListener.OnItemClick(view, getBindingAdapterPosition()));

        itemView.setOnLongClickListener(view -> {
            mClickListener.OnItemLongClick(view, getBindingAdapterPosition());
            return true;
        });
    }


    public void SeteoVideojuegos(Context context, String nombre, int vista, String imagen) {
        ImageView ImagenVideojuego;
        TextView NombreImagenVideojuego;
        TextView VistaVideojuego;

        ImagenVideojuego = mView.findViewById(R.id.ImagenVideojuego);
        NombreImagenVideojuego = mView.findViewById(R.id.NombreImagenVideojuego);
        VistaVideojuego = mView.findViewById(R.id.VistaVideojuego);

        NombreImagenVideojuego.setText(nombre);
        String VistaString = String.valueOf(vista);
        VistaVideojuego.setText(VistaString);

        try {
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenVideojuego);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.categoria).into(ImagenVideojuego);
        }

    }

}
