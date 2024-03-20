package com.serch.fondosdepantalla.Categorias.Cat_Firebase;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderCF extends RecyclerView.ViewHolder {

    View mView;
    private ViewHolderCF.ClickListener mClickListener;

    public interface ClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderCF.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderCF(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(view -> mClickListener.OnItemClick(view, getBindingAdapterPosition()));
    }

    public void setCategoryFirebase(Context context, String category, String image) {
        ImageView imageCategoryF;
        TextView nameCategoryF;

        imageCategoryF = mView.findViewById(R.id.imagenCategoriaF);
        nameCategoryF = mView.findViewById(R.id.NombreCategoriaF);
        nameCategoryF.setText(category);

        try {
            Picasso.get().load(image).placeholder(R.drawable.categoria).into(imageCategoryF);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.categoria).into(imageCategoryF);
        }
    }
}
