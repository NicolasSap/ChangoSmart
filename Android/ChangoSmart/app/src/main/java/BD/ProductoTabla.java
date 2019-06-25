package BD;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Clase Tabla para utilizarla desde ROOM
 */

@Entity(tableName = "Productos")
public class ProductoTabla {
    @PrimaryKey
    @NonNull
    private String nombre;
    private String categoria;
    private int precio;
    private int cantidad;

    public ProductoTabla(String nombre, String categoria, int precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    /** GETTERS   *****************************/

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getPrecio() {
        return precio;
    }

    /** SETTERS   *****************************/

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
}
