package com.example.changosmart.listasCompras.misListas;

public class ListaCompra {
    private String nombre_lista;
    private String supermercado;
    private String fecha_actualizacion;

    public ListaCompra(String nombre_lista, String supermercado, String fecha_actualizacion) {
        this.nombre_lista = nombre_lista;
        this.supermercado = supermercado;
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public String getNombre_lista() {
        return nombre_lista;
    }

    public void setNombre_lista(String nombre_lista) {
        this.nombre_lista = nombre_lista;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    public String getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(String fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }
}
