package com.serch.fondosdepantalla.CategoriasClienteFirebase;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderImgCatFElegida extends RecyclerView.ViewHolder {

    View mView;
    private ViewHolderImgCatFElegida.ClickListener mClickListener;

    public interface ClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderImgCatFElegida.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderImgCatFElegida(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(view -> mClickListener.OnItemClick(view, getBindingAdapterPosition()));
    }

    public void setCategoryFChoose(Context context, String name, int vista, String image) {
        ImageView ImgCatFElegida;
        TextView NombreImg_Cat_Elegida, VistaImg_Cat_Elegida;

        ImgCatFElegida = mView.findViewById(R.id.ImgCatFElegida);
        NombreImg_Cat_Elegida = mView.findViewById(R.id.NombreImg_Cat_Elegida);
        VistaImg_Cat_Elegida = mView.findViewById(R.id.VistaImg_Cat_Elegida);

        NombreImg_Cat_Elegida.setText(name);
        String vistaString = String.valueOf(vista);
        VistaImg_Cat_Elegida.setText(vistaString);

        try {
            Picasso.get().load(image).placeholder(R.drawable.categoria).into(ImgCatFElegida);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.categoria).into(ImgCatFElegida);
        }
    }
}
