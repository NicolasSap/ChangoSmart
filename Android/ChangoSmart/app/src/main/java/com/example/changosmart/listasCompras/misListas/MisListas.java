package com.example.changosmart.listasCompras.misListas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;
import com.example.changosmart.compras.porLista.ComprarPorLista;
import com.example.changosmart.listasCompras.detalleListas.DetalleLista;

import java.util.ArrayList;
import java.util.Objects;

import BT.Bluetooth;

public class MisListas extends AppCompatActivity {
    private ArrayList<ListaCompra> misListasCompras;

    private Bluetooth bluetoothInstance;

    private MiAdaptadorListaCompras adaptador;
    private SensorEventListener proximitySensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_listas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bluetoothInstance = Objects.requireNonNull(getIntent().getExtras()).getParcelable("btInstance");
        // Inicio las listas para caso de prueba
        // Devuelve interface LIST, por eso lo casteo a ArrayList
        misListasCompras = (ArrayList<ListaCompra>) MainActivity.myAppDatabase.myDao().getListaCompras();

        final ListView listaComprasView = (ListView) findViewById(R.id.listViewMisListas);

        //Creamos un Adaptador para mostrarlo por la vista
        adaptador = new MiAdaptadorListaCompras(this, misListasCompras);

        listaComprasView.setAdapter(adaptador);


        listaComprasView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                // Muestro mensaje
                final AlertDialog.Builder opcionesListaCompra = new AlertDialog.Builder(view.getContext());

                opcionesListaCompra.setTitle(R.string.title_alert_dialog_lista)
                        .setItems(R.array.tresOpcionesAlertDialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Opcion 1 (pos 0) -> Iniciar Compra
                                // Opcion 2 (pos 1) -> Editar Lista
                                // Opcion 3 (pos 2) -> Eliminar Lista
                                switch (which){
                                    case 0:
                                        Intent intentIniciarCompra = new Intent(view.getContext(), ComprarPorLista.class);
                                        intentIniciarCompra.putExtra("NOMBRE_LISTA",  misListasCompras.get(position).getNombre_lista());
                                        intentIniciarCompra.putExtra("btInstance", bluetoothInstance);
                                        startActivity(intentIniciarCompra);
                                        finish();
                                        break;
                                    case 1:
                                        Intent intentDetalleLista = new Intent(view.getContext(), DetalleLista.class);
                                        intentDetalleLista.putExtra("NOMBRE_LISTA", misListasCompras.get(position).getNombre_lista());
                                        intentDetalleLista.putExtra("btInstance", bluetoothInstance);
                                        startActivity(intentDetalleLista);
                                        break;
                                    case 2: // Eliminar Lista
                                        eliminarListaCompra(view, position);
                                        break;
                                }
                            }

                        });
                opcionesListaCompra.show();
            }
        });


        Button buttonCrearLista = (Button) findViewById(R.id.buttonCrearLista);
        buttonCrearLista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent newActivity = new Intent(view.getContext(),CrearLista.class);
                        newActivity.putExtra("btInstance", bluetoothInstance);
                        startActivityForResult(newActivity, 1);
                    }
                }
        );
    }


    @Override
    public void onStart() {
        super.onStart();
        // Devuelve interface LIST, por eso lo casteo a ArrayList
        misListasCompras = (ArrayList) MainActivity.myAppDatabase.myDao().getListaCompras();
        adaptador.setListaProductos(misListasCompras);
        adaptador.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        misListasCompras = (ArrayList) MainActivity.myAppDatabase.myDao().getListaCompras();
        adaptador.setListaProductos(misListasCompras);
        adaptador.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refreshActivity = new Intent(this, MisListas.class);
            refreshActivity.putExtra("btInstance", bluetoothInstance);
            startActivity(refreshActivity);
            this.finish();
        }
    }

    /**
     * Elimina lista de compra de la base de datos
     * @param view
     * @param pos
     */
    private void eliminarListaCompra(final View view, final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Confirmar Accion")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.myAppDatabase.myDao().eliminarLista(misListasCompras.get(pos).getNombre_lista());
                        MainActivity.myAppDatabase.myDao().eliminarDetalleLista(misListasCompras.get(pos).getNombre_lista());
                        misListasCompras.remove(pos);
                        adaptador.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

}
