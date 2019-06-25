package com.example.changosmart.listasCompras.misListas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.changosmart.R;

import java.util.ArrayList;

public class MiAdaptadorListaCompras extends BaseAdapter {
    private Context contexto;
    private ArrayList<ListaCompra> listaListaCompra;

    // Sirve para instanciar el xml
    private static LayoutInflater inflater = null;

    /** CONSTRUCTOR       ***************************************************/
    public MiAdaptadorListaCompras(Context contexto, ArrayList<ListaCompra> listaListaCompra) {
        this.contexto = contexto;
        this.listaListaCompra = listaListaCompra;

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

    public ArrayList<ListaCompra> getListaProductos() {
        return listaListaCompra;
    }

    public void setListaProductos(ArrayList<ListaCompra> listaProductos) {
        this.listaListaCompra = listaProductos;
    }
    /************************************************************************/


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int i, final View convertView, ViewGroup parent) {
        // Vista que voy a retornar
        final View myView = inflater.inflate(R.layout.item_lista_lista_compra, null);

        // Tomo los campos del xml donde voy a mostrar los datos
        TextView nombreLista        = (TextView) myView.findViewById(R.id.textViewListaCompra);
        TextView supermercado       = (TextView) myView.findViewById(R.id.textViewSupermercado);
        TextView fechaActualizacion = (TextView) myView.findViewById(R.id.textViewFechaActualizacion);

        // Elemento de la lista

        // Populo los datos de los campos del xml
        nombreLista.setText(listaListaCompra.get(i).getNombre_lista());
        supermercado.setText(listaListaCompra.get(i).getSupermercado());
        fechaActualizacion.setText(listaListaCompra.get(i).getFecha_actualizacion());

        // Retorno la vista generada
        return myView;
    }
    @Override
    public int getCount() {
        return listaListaCompra.size();
    }

    @Override
    public Object getItem(int position) {
        return listaListaCompra.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void removeItem(int position){this.listaListaCompra.remove(position);}

}
