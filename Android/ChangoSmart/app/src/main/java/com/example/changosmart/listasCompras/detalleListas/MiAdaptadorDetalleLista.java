package com.example.changosmart.listasCompras.detalleListas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.changosmart.R;

import java.util.ArrayList;

public class MiAdaptadorDetalleLista extends BaseAdapter {
    private Context contexto;
    private ArrayList<LineaCompra> listaDetalle;
    private static LayoutInflater inflater;

    public MiAdaptadorDetalleLista(Context contexto, ArrayList<LineaCompra> listaDetalle) {
        this.contexto = contexto;
        this.listaDetalle = listaDetalle;
        inflater = LayoutInflater.from(contexto);
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public ArrayList<LineaCompra> getListaDetalle() {
        return listaDetalle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View myView = inflater.inflate(R.layout.detalle_linea_compra,null);

        TextView nombreProducto     = (TextView) myView.findViewById(R.id.textViewDetalleProducto);
        TextView categoriaProducto  = (TextView) myView.findViewById(R.id.textViewDetalleCategoria);
        TextView precioProducto     = (TextView) myView.findViewById(R.id.textViewDetallePrecio);
        TextView cantidadProducto   = (TextView) myView.findViewById(R.id.textViewDetalleCantComprar);


        nombreProducto.setText(listaDetalle.get(position).getNombreProducto());
        categoriaProducto.setText(listaDetalle.get(position).getCategoria());
        precioProducto.setText("$ " + String.valueOf(listaDetalle.get(position).getPrecio()));
        cantidadProducto.setText("Cantidad: " + String.valueOf(listaDetalle.get(position).getCantidadAComprar()));

        return myView;
    }



    public void setListaDetalle(ArrayList<LineaCompra> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

    @Override
    public int getCount() {
        return listaDetalle.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDetalle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void removeItem(int position) { this.listaDetalle.remove(position);}

    public void actualizarCantidadAComprar(int position, Integer valueOf) {
        this.listaDetalle.get(position).setCantidadAComprar(valueOf);
    }
}
