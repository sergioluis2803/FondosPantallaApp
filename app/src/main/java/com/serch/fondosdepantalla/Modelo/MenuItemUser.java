package com.serch.fondosdepantalla.Modelo;

import com.serch.fondosdepantalla.R;

public enum MenuItemUser {
    HOME_USER(R.id.InicioCliente),
    INFO(R.id.AcercaDe),
    SHARE(R.id.Compartir);

    private final int id;

    MenuItemUser(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
