package com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA;

public class Pelicula {
    private String imagen;
    private String nombre;
    private int vistas;

    public Pelicula(String imagen, String nombres, int vistas) {
        this.imagen = imagen;
        this.nombre = nombres;
        this.vistas = vistas;
    }

    public Pelicula() {
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