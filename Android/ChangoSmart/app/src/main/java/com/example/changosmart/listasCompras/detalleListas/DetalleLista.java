package com.example.changosmart.listasCompras.detalleListas;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;
import com.example.changosmart.productos.TodosProductos;

import java.util.ArrayList;
import java.util.Objects;

import BT.Bluetooth;

public class DetalleLista extends AppCompatActivity {
    private ArrayList<LineaCompra> detalleLista;
    private ArrayList<LineaCompra> detalleListaFilt;
    private String nombreListaRecibido;
    private ListView listaProductosView;

    private Bluetooth bluetoothInstance;

    public static MiAdaptadorDetalleLista miAdaptadorDetalleLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleRecibido = this.getIntent().getExtras();
        // SE DEBE RECIBIR EN EL INTENT EL NOMBRE DE LA LISTA
        nombreListaRecibido = (String) bundleRecibido.get("NOMBRE_LISTA");
        setContentView(R.layout.activity_detalle_lista);
        Button buttonAgregarProductos = (Button) findViewById(R.id.buttonAgregarProductos);

        //DEFINICION DEL BUSCADOR
        EditText filter1 = (EditText) findViewById(R.id.searchFilter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        bluetoothInstance = Objects.requireNonNull(getIntent().getExtras()).getParcelable("btInstance");
        // Cargo la lista de los productos
        //Se castea porque retorna una clase List

        detalleLista = (ArrayList<LineaCompra>) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);

        if (detalleLista == null)
            detalleLista = new ArrayList<LineaCompra>();

        listaProductosView = (ListView) findViewById(R.id.listViewDetalleLista);

        //Seteo el adaptador y le paso la lista de los productos
        miAdaptadorDetalleLista = new MiAdaptadorDetalleLista(this, detalleLista);
        listaProductosView.setAdapter(miAdaptadorDetalleLista);

        buttonAgregarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProductos = new Intent(v.getContext(), TodosProductos.class);
                intentProductos.putExtra("NOMBRE_LISTA", nombreListaRecibido);
                intentProductos.putExtra("btInstance", bluetoothInstance);
                startActivity(intentProductos);
            }
        });

        listaProductosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleLista.this);
                View miAlertDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.alertdialog_linea_compra, null);
                final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextDetalleCantidadProducto);
                builder.setView(miAlertDialog);
                final AlertDialog dialog = builder.create();

                Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogDetalleAceptar);

                buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etCantidad.getText().toString().isEmpty() || Integer.valueOf(etCantidad.getText().toString()) <= 0) {
                            Toast.makeText(DetalleLista.this, "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                        } else {
                            MainActivity.myAppDatabase.myDao().actualizarCantidadAComprar(nombreListaRecibido, detalleLista.get(position).getNombreProducto(), Integer.valueOf(etCantidad.getText().toString()));
                            detalleLista.get(position).setCantidadAComprar(Integer.valueOf(etCantidad.getText().toString()));
                            miAdaptadorDetalleLista.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });

                Button buttonCancelarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogDetalleCancelar);

                buttonCancelarAlertProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button buttonEliminarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogDetalleEliminar);

                buttonEliminarAlertProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.myAppDatabase.myDao().eliminarProductoEnLista(nombreListaRecibido, detalleLista.get(position).getNombreProducto());
                        detalleLista.remove(position);
                        miAdaptadorDetalleLista.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });



        filter1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                detalleLista = (ArrayList<LineaCompra>) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);
                detalleListaFilt=new ArrayList<LineaCompra>();
                if(charSequence.toString().equals("")) {
                    // reset listview
                        detalleListaFilt=(ArrayList<LineaCompra>) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);
                }
                else {
                    for (LineaCompra item : detalleLista) {
                        if ((item.getNombreProducto().toLowerCase()).contains(charSequence.toString().toLowerCase())) {
                            detalleListaFilt.add(item);
                        }
                    }
                }
                detalleLista = detalleListaFilt;
                miAdaptadorDetalleLista.setListaDetalle(detalleLista);
                miAdaptadorDetalleLista.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Devuelve interface LIST, por eso lo casteo a ArrayList
        detalleLista = (ArrayList<LineaCompra>) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);
        miAdaptadorDetalleLista.setListaDetalle(detalleLista);
        listaProductosView.setAdapter(miAdaptadorDetalleLista);
    }
}