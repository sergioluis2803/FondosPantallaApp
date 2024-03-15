package com.serch.fondosdepantalla.Categorias.Cat_Dispositivo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderCD extends RecyclerView.ViewHolder {

    View mView;
    private ViewHolderCD.ClickListener mClickListener;

    public interface ClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderCD.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderCD(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(view -> mClickListener.OnItemClick(view, getBindingAdapterPosition()));
    }

    public void setCategoryDis(Context context, String category, String image) {
        ImageView imageCategory;
        TextView nameCategory;

        imageCategory = mView.findViewById(R.id.imagenCategoriaD);
        nameCategory = mView.findViewById(R.id.NombreCategoriaD);
        nameCategory.setText(category);

        try {
            Picasso.get().load(image).placeholder(R.drawable.categoria).into(imageCategory);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.categoria).into(imageCategory);
        }
    }
}
