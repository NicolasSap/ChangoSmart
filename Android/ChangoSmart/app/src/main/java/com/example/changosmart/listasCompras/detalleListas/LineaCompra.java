package com.example.changosmart.listasCompras.detalleListas;

public class LineaCompra {
    private String nombreProducto,
                    categoria;
    private int cantidadAComprar,
                cantidadComprada = 0,
                precio;

    public LineaCompra(String nombreProducto, String categoria, int cantidadAComprar, int precio) {
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.cantidadAComprar = cantidadAComprar;
        this.precio = precio;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidadAComprar() {
        return cantidadAComprar;
    }

    public void setCantidadAComprar(int cantidadAComprar) {
        this.cantidadAComprar = cantidadAComprar;
    }

    public int getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(int cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }


    @Override
    public String toString() {
        return nombreProducto;
    }
}
