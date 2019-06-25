package com.example.changosmart.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.changosmart.R;

import java.util.ArrayList;

public class MiAdaptadorDispositivosBluetooth extends BaseAdapter {
    private Context contexto;
    private ArrayList<BluetoothDevice> listaDispositivos;

    // Sirve para instanciar el xml
    private static LayoutInflater inflater = null;

    /** CONSTRUCTOR       ***************************************************/
    public MiAdaptadorDispositivosBluetooth(Context contexto, ArrayList<BluetoothDevice> dispositivos) {
        this.contexto = contexto;
        this.listaDispositivos = dispositivos;

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

    public ArrayList<BluetoothDevice> getListaDispositivos() {
        return listaDispositivos;
    }

    public void setListaDispositivos(ArrayList<BluetoothDevice> dispositivos) {
        this.listaDispositivos = dispositivos;
    }
    /************************************************************************/


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // Vista que voy a retornar
        final View myView = inflater.inflate(R.layout.item_lista_dispositivos_bluetooth, null);

        // Tomo los campos del xml donde voy a mostrar los datos
        TextView tv_nombreDispositivo      = (TextView) myView.findViewById(R.id.nombreDispositivo);
        TextView tv_adressDispositivo   = (TextView) myView.findViewById(R.id.adressDispositivo);

        // Populo los datos de los campos del xml
        String nombreFinal = listaDispositivos.get(i).getName();
        if (nombreFinal == null || nombreFinal.equals("")){
            nombreFinal = "Nombre protegido";
        }

        tv_nombreDispositivo.setText(nombreFinal);
        tv_adressDispositivo.setText(listaDispositivos.get(i).getAddress());

        // Retorno la vista generada
        return myView;
    }

    @Override
    public int getCount() {
        return listaDispositivos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDispositivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}