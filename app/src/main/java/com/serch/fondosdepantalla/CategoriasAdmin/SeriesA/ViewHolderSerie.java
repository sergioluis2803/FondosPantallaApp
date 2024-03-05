package com.serch.fondosdepantalla.CategoriasAdmin.SeriesA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderSerie extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderSerie.ClickListener mClickListener;

    public interface ClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderSerie.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderSerie(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(view -> mClickListener.OnItemClick(view, getBindingAdapterPosition()));

        itemView.setOnLongClickListener(view -> {
            mClickListener.OnItemLongClick(view, getBindingAdapterPosition());
            return true;
        });
    }


    public void SeteoSeries(Context context, String nombre, int vista, String imagen) {
        ImageView ImagenSerie;
        TextView NombreImagenSerie;
        TextView VistaSerie;

        ImagenSerie = mView.findViewById(R.id.ImagenSerie);
        NombreImagenSerie = mView.findViewById(R.id.NombreImagenSerie);
        VistaSerie = mView.findViewById(R.id.VistaSerie);

        NombreImagenSerie.setText(nombre);
        String VistaString = String.valueOf(vista);
        VistaSerie.setText(VistaString);

        try {
            Picasso.get().load(imagen).into(ImagenSerie);
        } catch (Exception e) {
            Toast.makeText(context, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
