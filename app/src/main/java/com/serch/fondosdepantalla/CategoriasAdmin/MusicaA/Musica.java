package com.serch.fondosdepantalla.CategoriasAdmin.MusicaA;

public class Musica {
    private String id;
    private String imagen;
    private String nombre;
    private int vistas;

    public Musica() {
    }

    public Musica(String id, String imagen, String nombre, int vistas) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.vistas = vistas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }
}
