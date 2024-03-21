package com.serch.fondosdepantalla.Apartado_Informativo;

public class Informacion {

    String nombre;
    String imagen;

    public Informacion() {
    }

    public Informacion(String name, String image) {
        this.nombre = name;
        this.imagen = image;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
