package com.serch.fondosdepantalla.Detalle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serch.fondosdepantalla.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailAdmin extends AppCompatActivity {

    CircleImageView imageDetailAdmin;
    TextView uidDetailAdmin, nameDetailAdmin, lastNameDetailAdmin, emailDetailAdmin, ageDetailAdmin;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_administrador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.detail_toolbar));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        imageDetailAdmin = findViewById(R.id.ImagenDetalleAdmin);
        uidDetailAdmin = findViewById(R.id.UidDetalleAdmin);
        nameDetailAdmin = findViewById(R.id.NombresDetalleAdmin);
        lastNameDetailAdmin = findViewById(R.id.ApellidosDetalleAdmin);
        emailDetailAdmin = findViewById(R.id.CorreoDetalleAdmin);
        ageDetailAdmin = findViewById(R.id.EdadDetalleAdmin);

        String uidDetail = getIntent().getStringExtra("UID");
        String nameDetail = getIntent().getStringExtra("NAMES");
        String lastNameDetail = getIntent().getStringExtra("LAST_NAME");
        String emailDetail = getIntent().getStringExtra("EMAIL");
        String ageDetail = getIntent().getStringExtra("AGE");
        String imageDetail = getIntent().getStringExtra("IMAGE");

        uidDetailAdmin.setText("UID= " + uidDetail);
        nameDetailAdmin.setText("NOMBRES= " + nameDetail);
        lastNameDetailAdmin.setText("APELLIDOS= " + lastNameDetail);
        emailDetailAdmin.setText("CORREO= " + emailDetail);
        ageDetailAdmin.setText("EDAD= " + ageDetail);

        try {
            Picasso.get().load(imageDetail).placeholder(R.drawable.admin_item).into(imageDetailAdmin);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.admin_item).into(imageDetailAdmin);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }
}