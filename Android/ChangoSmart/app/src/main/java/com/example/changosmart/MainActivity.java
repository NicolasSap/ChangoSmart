package com.example.changosmart;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.changosmart.bluetooth.BluetoothSettings;
import com.example.changosmart.chango.Chango;
import com.example.changosmart.listasCompras.misListas.MisListas;
import com.example.changosmart.compras.express.AnadirProductoExpress;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import BD.MyAppDatabase;
import BD.ProductoTabla;
import BT.Bluetooth;

public class MainActivity extends AppCompatActivity {
    public static MyAppDatabase myAppDatabase;
    private boolean primeraEjecucion = false;
    private Bluetooth bluetoothInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creo la primera instancia del BT, debe perdurar en toda la app, si se cierra, se pierde.
        bluetoothInstance = new Bluetooth();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Botón para abrir la configuración del bluetooth
        FloatingActionButton btSettings = findViewById(R.id.bluetoothConfiguration);
        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abro vista bt
                Intent btSettings = new Intent( view.getContext(), BluetoothSettings.class);
                //Le paso la instancia del bluetooth para que tenga los valores.
                btSettings.putExtra("btInstance", bluetoothInstance);
                startActivityForResult(btSettings, 17);
            }
        });

        //Botón para mover el chango
        FloatingActionButton moveChart = findViewById(R.id.moveChart);
        moveChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveChango = new Intent( view.getContext(), Chango.class);
                moveChango.putExtra("btInstance", bluetoothInstance);
                startActivity(moveChango);
            }
        });

        // INICIALIZO LA BASE
        // HAY QUE CREAR UN HILO PARA QUE NO TRABAJE EN EL HILO PRINCIPAL
        myAppDatabase = Room.databaseBuilder(this.getApplicationContext(), MyAppDatabase.class, "db").allowMainThreadQueries().build();

        if(! primeraEjecucion){
            cargarProductos();
            primeraEjecucion = true;
        }
    }

    private void cargarProductos() {
        List<ProductoTabla> listaProductoTabla = new ArrayList<ProductoTabla>();
        listaProductoTabla.add(new ProductoTabla("Oreo", "Comida", 20));
        listaProductoTabla.add(new ProductoTabla("Paty Swift", "Congelado", 30));
        listaProductoTabla.add(new ProductoTabla("Axe", "Higiene", 15));
        listaProductoTabla.add(new ProductoTabla("Cuchillo", "Hogar", 30));
        listaProductoTabla.add(new ProductoTabla("Pochoclera", "Cocina", 25));
        listaProductoTabla.add(new ProductoTabla("Birome Bic", "Libreria", 100));
        listaProductoTabla.add(new ProductoTabla("Pila", "Electricidad", 20));
        listaProductoTabla.add(new ProductoTabla("Lechuga", "Verduleria", 150));
        if (myAppDatabase.myDao().getProductos().isEmpty()) {
            myAppDatabase.myDao().cargarProductos(listaProductoTabla);
        }else{
            myAppDatabase.myDao().eliminarTodosProductos();
            myAppDatabase.myDao().cargarProductos(listaProductoTabla);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage (View view){
        Intent openMisListas = new Intent(this, MisListas.class);
        openMisListas.putExtra("btInstance", bluetoothInstance);
        startActivity(openMisListas);
    }

    public void openListExpress (View view){
        Intent openListExpress = new Intent(this, AnadirProductoExpress.class);
        openListExpress.putExtra("btInstance", bluetoothInstance);
        startActivity(openListExpress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 17) {
            if(resultCode == RESULT_OK) {
                bluetoothInstance = Objects.requireNonNull(data.getExtras()).getParcelable("btInstanceBack");
                Toast toast =
                        Toast.makeText(getApplicationContext(),
                                "El dispositivo conectado es: " + bluetoothInstance.getPairDevice().getName(), Toast.LENGTH_SHORT);

                toast.setGravity(Gravity.CENTER,0,0);

                toast.show();
            }
        }
    }
}
