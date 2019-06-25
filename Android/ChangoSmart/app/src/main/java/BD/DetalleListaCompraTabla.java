package BD;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(tableName = "Detalle_Lista_Compra", primaryKeys ={"nombre_lista", "nombre_producto"})
public class DetalleListaCompraTabla {


    @NonNull
    private String nombre_lista;
    @NonNull
    private String nombre_producto;
    private int cantidadAComprar;
    private int cantidadComprada;

    public DetalleListaCompraTabla(@NonNull String nombre_lista, @NonNull String nombre_producto, int cantidadAComprar) {
        this.nombre_lista = nombre_lista;
        this.nombre_producto = nombre_producto;
        this.cantidadAComprar = cantidadAComprar;
        this.cantidadComprada = 0;
    }

    @NonNull
    public String getNombre_lista() {
        return nombre_lista;
    }

    public void setNombre_lista(@NonNull String nombre_lista) {
        this.nombre_lista = nombre_lista;
    }

    @NonNull
    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(@NonNull String nombre_producto) {
        this.nombre_producto = nombre_producto;
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
}
