package com.serch.fondosdepantalla.Modelo;

import com.serch.fondosdepantalla.R;

public enum MenuItemAdmin {
    HOME_ADMIN(R.id.InicioAdmin),
    PROFILE_ADMIN(R.id.PerfilAdmin),
    REGISTER_ADMIN(R.id.RegistrarAdmin),
    LIST_ADMIN(R.id.ListarAdmin),
    SIGN_OUT(R.id.Salir);

    private final int id;

    MenuItemAdmin(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
