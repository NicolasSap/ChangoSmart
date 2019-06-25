package BD;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.changosmart.listasCompras.misListas.ListaCompra;
import com.example.changosmart.listasCompras.detalleListas.LineaCompra;
import com.example.changosmart.productos.Producto;

import java.util.List;

/**
 * Clase para conectarse a la BD ROOM
 */
@Dao
public interface MyDao {
    // Firma de metodo para insertar Listas de Compras en la base ROOM
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void  addLista(MisListasCompraTabla lista);

    @Query("SELECT nombre_lista, supermercado, fecha_actualizacion FROM Mis_Listas_Compras ORDER BY fecha_actualizacion DESC")
    public List<ListaCompra> getListaCompras();

    @Query("SELECT nombre, categoria, precio, cantidad FROM Productos ORDER BY Nombre")
    public  List<Producto> getProductos();

    @Query("SELECT nombre, categoria, precio, cantidad FROM Productos WHERE nombre = :nombreProducto")
    public  Producto getProducto(String nombreProducto);

    @Query("DELETE FROM Mis_Listas_Compras WHERE nombre_lista = :nombre_lista")
    public void eliminarLista(String nombre_lista);

    @Query("DELETE FROM Detalle_Lista_Compra WHERE nombre_lista = :nombre_lista")
    public void eliminarDetalleLista(String nombre_lista);

    @Query("SELECT Dlc.nombre_producto nombreProducto, P.categoria, P.precio, Dlc.cantidadAComprar, Dlc.cantidadComprada " +
            "FROM Detalle_Lista_Compra Dlc, Productos P " +
            "WHERE Dlc.nombre_producto = P.nombre " +
              "AND Dlc.nombre_lista = :nombre_lista")
    public List<LineaCompra> getDetalleLista(String nombre_lista);

    @Insert
    public void cargarProductos(List<ProductoTabla> listaProductoTabla);

    @Query("UPDATE Detalle_Lista_Compra SET cantidadAComprar = :cantidad + cantidadAComprar WHERE nombre_lista = :nombre_lista AND nombre_producto = :nombre_producto")
    public void actualizarCantidadAComprar(String nombre_lista, String nombre_producto, int cantidad);

    @Query("UPDATE Detalle_Lista_Compra SET cantidadAComprar = :cantidad WHERE nombre_lista = :nombre_lista AND nombre_producto = :nombre_producto")
    public void setNuevaCantidadAComprar(String nombre_lista, String nombre_producto, int cantidad);

    @Query("SELECT 1 FROM Detalle_Lista_Compra WHERE nombre_lista = :nombre_lista AND nombre_producto = :nombre_producto")
    public int existeProductoEnLista(String nombre_lista, String nombre_producto);

    @Insert
    public void insertarLineaCompra(DetalleListaCompraTabla detalleListaCompraTabla);

    @Query("DELETE FROM Detalle_Lista_Compra WHERE nombre_lista = :nombre_lista AND nombre_producto = :nombre_producto")
    public void eliminarProductoEnLista(String nombre_lista, String nombre_producto);

    @Query("SELECT nombre, categoria, precio, cantidad FROM Productos WHERE nombre = :nombreProducto")
    public Producto findByProductoExpress( String nombreProducto );

    @Query("DELETE FROM Productos")
    public void eliminarTodosProductos();
}
