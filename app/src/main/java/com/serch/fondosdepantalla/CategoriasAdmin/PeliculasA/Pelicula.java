package com.serch.fondosdepantalla.CategoriasAdmin.PeliculasA;

public class Pelicula {
    private String imagen;
    private String nombres;
    private int vistas;

    public Pelicula(String imagen, String nombres, int vistas) {
        this.imagen = imagen;
        this.nombres = nombres;
        this.vistas = vistas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }
}