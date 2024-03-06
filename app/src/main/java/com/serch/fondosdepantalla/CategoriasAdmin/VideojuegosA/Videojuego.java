package com.serch.fondosdepantalla.CategoriasAdmin.VideojuegosA;

public class Videojuego {

    private String imagen;
    private String nombre;
    private int vistas;

    public Videojuego() {
    }

    public Videojuego(String imagen, String nombres, int vistas) {
        this.imagen = imagen;
        this.nombre = nombres;
        this.vistas = vistas;
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
