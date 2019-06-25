package com.example.changosmart.listasCompras.misListas;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;
import com.example.changosmart.listasCompras.detalleListas.DetalleLista;

import java.util.Objects;

import BD.MisListasCompraTabla;
import BT.Bluetooth;

public class CrearLista extends AppCompatActivity {
    private EditText etNombreLista,
                     etSupermercado;
    private Button buttonCrearListaVacia,
                    buttonCrearListaConProductos;

    private Bluetooth bluetoothInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_lista);

        bluetoothInstance = Objects.requireNonNull(getIntent().getExtras()).getParcelable("btInstanceBack");

        etNombreLista = this.findViewById(R.id.editTextNombreLista);
        etSupermercado = this.findViewById(R.id.editTextSupermercado);

        // Creo el boton para comitear los datos en la base
        buttonCrearListaVacia = (Button) findViewById(R.id.buttonCrearListaVacia);
        // Seteo el listener y lo creo adentro
        buttonCrearListaVacia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(crearNuevaLista(view)) {
                    Intent intentMisListas = new Intent(view.getContext(), MisListas.class);
                    intentMisListas.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intentMisListas.putExtra("btInstance", bluetoothInstance);
                    startActivity(intentMisListas);

                    // Eliminamos la actividad actual para que no quede viva
                    setResult(RESULT_OK, null);
                    finish();
                }
            }
          }
        );
        buttonCrearListaConProductos = (Button) findViewById(R.id.buttonCrearListaConProductos);

        buttonCrearListaConProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (crearNuevaLista(view)) {
                    Intent intentDetalleLista = new Intent(view.getContext(), DetalleLista.class);
                    intentDetalleLista.putExtra("NOMBRE_LISTA",etNombreLista.getText().toString());
                    intentDetalleLista.putExtra("btInstance", bluetoothInstance);
                    startActivity(intentDetalleLista);
                    finish();
                }
            }
        });
    }

    private boolean crearNuevaLista(View view){
        if( etNombreLista.getText().toString().isEmpty()) {
            // Muestro mensaje
            Toast.makeText(view.getContext(), "Ingrese un nombre de Lista valido", Toast.LENGTH_SHORT).show();
            etNombreLista.requestFocus();
            return false;
        }else {
            String nombreLista = etNombreLista.getText().toString();
            String supermercado= etSupermercado.getText().toString();

            // Creo el objeto que se guardara en la base ........
            MisListasCompraTabla listaCompra = new MisListasCompraTabla(nombreLista, supermercado);

            try {
                // Inserto en la base el objeto creado
                MainActivity.myAppDatabase.myDao().addLista(listaCompra);
                // Muestro mensaje de Lista Creada
                Toast.makeText(view.getContext(), "Lista Creada", Toast.LENGTH_SHORT).show();
            }catch(SQLiteException e){
                Toast.makeText(view.getContext(),"Esta lista ya se encuentra creada para este supermecado.",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        
        return true;
    }
}


