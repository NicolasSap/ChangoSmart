package BD;



import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Clase para crear la tabla ListaCompra
 * Se emplea usando ROOM library
 */

@Entity(tableName = "Mis_Listas_Compras", primaryKeys ={"nombre_lista"})
public class MisListasCompraTabla {
    // Si quiero cambiarle el nombre a la tabla @ ColumnInfo(name = "nombre_lista")
    //Tengo que tener al menos una clave primaria
    @NonNull
    private String nombre_lista;
    private String supermercado;
    private String fecha_actualizacion;

    public MisListasCompraTabla(String nombre_lista, String supermercado){
        this.nombre_lista = nombre_lista;
        this.supermercado = supermercado;
        this.fecha_actualizacion = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

    // SETTERS
    public void setNombre_lista(String nombre_lista) {
        nombre_lista = nombre_lista;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    public void setFecha_actualizacion(String fecha_actualizacion) { this.fecha_actualizacion = fecha_actualizacion; }

    // GETTERS
    public String getNombre_lista() {
        return nombre_lista;
    }

    public String getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public String getSupermercado() { return supermercado; }



    // METODOS

    /**
     * Actualiza la fecha de Actualizacion a la de hoy
     */
    public void actualizarFechaActualizacion(){
        this.fecha_actualizacion = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

}