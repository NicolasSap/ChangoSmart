package com.example.changosmart.compras.express;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.changosmart.R;
import com.example.changosmart.productos.Producto;

import java.util.ArrayList;

public class MiAdaptadorListaProductosExpress extends BaseAdapter {
    private Context contexto;
    private ArrayList<Producto> listaProductos;

    // Sirve para instanciar el xml
    private static LayoutInflater inflater = null;

    /** CONSTRUCTOR       ***************************************************/
    public MiAdaptadorListaProductosExpress(Context contexto, ArrayList<Producto> listaProductos) {
        this.contexto = contexto;
        this.listaProductos = listaProductos;

        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }
    /************************************************************************/

    /** GETTER & SETTERS  *************************************************/
    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
    /************************************************************************/


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // Vista que voy a retornar
        final View myView = inflater.inflate(R.layout.item_lista_producto, null);

        // Tomo los campos del xml donde voy a mostrar los datos
        TextView tv_nombreProducto      = (TextView) myView.findViewById(R.id.textViewNombreProducto);
        TextView tv_categoriaProducto   = (TextView) myView.findViewById(R.id.textViewCategoriaProducto);
        TextView tv_precioProducto      = (TextView) myView.findViewById(R.id.textViewPrecioProducto);
        TextView tv_cantidadProducto    = (TextView) myView.findViewById(R.id.textViewCantidadProducto);

        // Populo los datos de los campos del xml
        tv_nombreProducto.setText(listaProductos.get(i).getNombre());
        tv_categoriaProducto.setText(listaProductos.get(i).getCategoria());
        String precioFormateado = "$ " + listaProductos.get(i).getPrecio();
        tv_precioProducto.setText(precioFormateado);
        String cantidadFormateada = "Cantidad: " + listaProductos.get(i).getCantidad();
        tv_cantidadProducto.setText(cantidadFormateada);

        // Retorno la vista generada
        return myView;
    }

    @Override
    public int getCount() {
        return listaProductos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void removeItem(int position){ listaProductos.remove( position ); }

    public void actualizarCantidadAComprar(int position, Integer valueOf) {
        this.listaProductos.get(position).setCantidad(valueOf);
    }


}
