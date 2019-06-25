package com.example.changosmart.productos;

public class Producto {
    private String  nombre,
                    categoria;
    private int precio,cantidad;

    public Producto(String nombre, String categoria, int precio, int cantidad) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String descripcion) {
        this.categoria = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public void sumarCantidadActual(int cantidadNueva){
        this.cantidad += cantidadNueva;
    }

    public int getTotalPorProducto(){ return this.cantidad * this.precio;}

    @Override
    public boolean equals(Object obj) {
        boolean sonIguales = false;
        if (obj instanceof Producto){
            Producto auxiliar = (Producto) obj;
            sonIguales = this.getNombre().equals(auxiliar.getNombre());
        }
        return  sonIguales;
    }

}
