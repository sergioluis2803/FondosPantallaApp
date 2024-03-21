package com.serch.fondosdepantalla.Apartado_Informativo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderInformacion extends RecyclerView.ViewHolder {
    View mView;
    private ViewHolderInformacion.ClickListener mClickListener;

    public interface ClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderInformacion.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderInformacion(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(view -> mClickListener.OnItemClick(view, getBindingAdapterPosition()));
    }

    public void setInformation(Context context, String name, String image) {
        ImageView imageInformation;
        TextView nameInformation;

        imageInformation = mView.findViewById(R.id.imagenInformativo);
        nameInformation = mView.findViewById(R.id.TituloInformativoTXT);
        nameInformation.setText(name);

        try {
            Picasso.get().load(image).placeholder(R.drawable.categoria).into(imageInformation);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.categoria).into(imageInformation);
        }
    }
}
