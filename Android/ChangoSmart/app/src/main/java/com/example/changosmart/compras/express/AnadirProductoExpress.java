package com.example.changosmart.compras.express;

import android.app.AlertDialog;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v4.content.LocalBroadcastManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.QR.QR;
import com.example.changosmart.R;
import com.example.changosmart.chango.Chango;
import com.example.changosmart.productos.Producto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

import BT.Bluetooth;
import BT.BluetoothConnectionService;

import static java.lang.StrictMath.abs;

public class AnadirProductoExpress extends AppCompatActivity {
    public static final int REQUEST_CODE_QR = 1010;
    public static final int REQUEST_CODE_QR_QUITAR = 1111;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MiAdaptadorListaProductosExpress adaptator;
    private static ArrayList<Producto> listaProductos;
    private TextView precioParcial;
    private TextView temperaturaTextView;
    private int cantIngresar;

    private Bluetooth bluetoothInstance;

    private BluetoothConnectionService bluetoothConnection;

    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private float prevX;
    private boolean reinicioLista = false;
    private StringBuilder temperaturaStringBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_producto_express);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SensorManager mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor myAccelerometerSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        prevX = 0;
        reinicioLista = false;
        temperaturaStringBuilder = new StringBuilder();
        temperaturaTextView = findViewById(R.id.textViewTemperatura);
        temperaturaTextView.setText("");

        //Se instancia el bluetooth en base a la conexión actual
        bluetoothInstance = Objects.requireNonNull(getIntent().getExtras()).getParcelable("btInstance");



        //Se verifica que se tenga una conexión activa de bluetooth.
        if (bluetoothInstance != null){
            //Se inicia el socket del bt para escuchar mensajes del arduino.
            bluetoothConnection = new BluetoothConnectionService(getApplicationContext());
            if (bluetoothInstance.getPairDevice() != null){
                bluetoothConnection.startClient( bluetoothInstance.getPairDevice(), bluetoothConnection.getDeviceUUID());
                //Registro el evento del broadcast para detectar el provider que genera la lectura de un dato enviado por el arduino (BluetoothServiceConnection)
                LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter("IncomingMessage"));
            }else {
                //Se informa al usuario que debe emparejarse con un dispositivo.
                Toast toast1 =
                        Toast.makeText(getApplicationContext(), "No estás conectado a ningún dispositivo. Conectate vía bluetooth por favor..." , Toast.LENGTH_SHORT);

                toast1.setGravity(Gravity.CENTER,0,0);

                toast1.show();
            }
        }

        //Solo si se detecta sensor.
        if (myAccelerometerSensor != null) {
           mySensorManager.registerListener(accelerometerSensorEventListener,myAccelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        
		final ListView listaProductosView = (ListView) findViewById(R.id.listViewProductosExpress);
        listaProductos = new ArrayList<Producto>();
        precioParcial = (TextView) findViewById(R.id.textViewExpressTotalParcial);
        //Le seteo el total al label de la vista.
        precioParcial.setText(String.valueOf(0));

        //Seteo el adaptador y le paso la lista de los productos
        adaptator = new MiAdaptadorListaProductosExpress(this, listaProductos);
        listaProductosView.setAdapter(adaptator);

        //Si se clickea un elemento de la lista se propone eliminarlo, de ser así se elimina de la lista.
        listaProductosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AnadirProductoExpress.this);
                    View miAlertDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.alertdialog_linea_compra, null);
                    final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextDetalleCantidadProducto);
                    builder.setView(miAlertDialog);
                final AlertDialog dialog = builder.create();

                    Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogDetalleAceptar);

                    buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etCantidad.getText().toString().isEmpty() || Integer.valueOf(etCantidad.getText().toString()) <= 0) {
                                Toast.makeText(AnadirProductoExpress.this, "Error - Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                            } else {
                                int montoActual = Integer.valueOf(precioParcial.getText().toString()) - listaProductos.get(position).getTotalPorProducto();
                                int nuevoMonto;

                                listaProductos.get(position).setCantidad(Integer.valueOf(etCantidad.getText().toString()));
                                adaptator.notifyDataSetChanged();

                                nuevoMonto = listaProductos.get(position).getTotalPorProducto() + montoActual;

                                dialog.dismiss();

                                precioParcial.setText(String.valueOf(nuevoMonto));
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
                            // HAY QUE AGREGAR EL ALERT DIALOG PARA RECIBIR LA BARRERA
                            int montoActual = Integer.valueOf(precioParcial.getText().toString());
                            int nuevoMonto = montoActual - listaProductos.get(position).getTotalPorProducto();

                            listaProductos.remove(position);
                            adaptator.notifyDataSetChanged();
                            dialog.dismiss();

                            precioParcial.setText(String.valueOf(nuevoMonto));
                        }
                    });

                    dialog.show();
            }
        });

        Button limpiarLista = (Button) findViewById(R.id.ClearListButton);

        limpiarLista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                        builder.setTitle("¿Desea vaciar la lista de productos?")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reinicioLista = true;
                                        TextView totalParcial = (TextView) findViewById(R.id.textViewExpressTotalParcial);
                                        totalParcial.setText(String.valueOf(0));
                                        listaProductos.clear();
                                        adaptator.notifyDataSetChanged();
                                        AlertDialog.Builder builderLiberar = new AlertDialog.Builder(view.getContext())
                                                .setTitle("Vaciar el chango")
                                                .setMessage("Quite los productos del chango y luego presione aceptar")
                                                .setCancelable(false)
                                                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        reinicioLista = false;
                                                    }
                                                });
                                        AlertDialog quitarProductosDialog = builderLiberar.create();
                                        quitarProductosDialog.setCanceledOnTouchOutside(false);
                                        quitarProductosDialog.show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    }
                }
        );
     
        FloatingActionButton qrFab = findViewById(R.id.qr);
        qrFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothInstance.getPairDevice() != null) {
                    Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                    openQr.putExtra("btInstance", bluetoothInstance);
                    // se abre la vista de la camara para escanear el código qr y agregar el producto.
                    startActivityForResult(openQr, REQUEST_CODE_QR);
                } else {
                    //Se informa al usuario que debe emparejarse con un dispositivo.
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), "No estás conectado a ningún dispositivo. Conectate vía bluetooth por favor...", Toast.LENGTH_SHORT);

                    toast1.setGravity(Gravity.CENTER, 0, 0);

                    toast1.show();
                }
            }
        });

        Button finalizarCompraButton = (Button) findViewById(R.id.finalizarCompraButton);

        finalizarCompraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("¿Seguro que desea finalizar la compra?")
                        .setMessage("Al finalizar la compra se le dira a que caja debe dirigirse")
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Random random = new Random();
                                AlertDialog.Builder builderFinalizarCompra = new AlertDialog.Builder(v.getContext());
                                builderFinalizarCompra.setTitle("Compra Finalizada")
                                        .setMessage("Monto total acumulado $ "+ precioParcial.getText() +"\nDirigase a la caja Nº "+ (random.nextInt(10)+1))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                TextView totalParcial = (TextView) findViewById(R.id.textViewExpressTotalParcial);
                                                totalParcial.setText(String.valueOf(0));
                                                listaProductos.clear();
                                                adaptator.notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialogFinalizarCompra = builderFinalizarCompra.create();
                                alertDialogFinalizarCompra.setCancelable(false);
                                alertDialogFinalizarCompra.setCanceledOnTouchOutside(false);
                                alertDialogFinalizarCompra.show();
                            }
                        });
                builder.show();
            }
        });

        FloatingActionButton changoFab = findViewById(R.id.moveChartCompraExpress);
        changoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openChango = new Intent(AnadirProductoExpress.this, Chango.class);
                openChango.putExtra("btInstance", bluetoothInstance);
                // se abre la vista de la camara para escanear el código qr y agregar el producto.
                startActivity(openChango);
            }
        });
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Acá se recibe el mensaje del arduino y se evalua si es In o Out
            String text = intent.getStringExtra("theMessage");
            Log.e("Entrada", text );
            if (text.equals("E")){
                Toast toast1 =
                        Toast.makeText(getApplicationContext(), "Ingresó un producto al chango." , Toast.LENGTH_SHORT);

                toast1.setGravity(Gravity.CENTER,0,0);
                cantIngresar--;
                toast1.show();
            }else if (text.equals("O")){
                Toast toast1 =
                        Toast.makeText(getApplicationContext(), "Salió un producto del chango." , Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER,0,0);
                toast1.show();

                Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                openQr.putExtra("btInstance", bluetoothInstance);
                startActivityForResult(openQr, REQUEST_CODE_QR_QUITAR);


            }else if (text.matches("[0-9]")){
                temperaturaStringBuilder.append(text);
                if(temperaturaStringBuilder.length() >= 2 ){
                    temperaturaTextView.setText(temperaturaStringBuilder.toString());
                    temperaturaStringBuilder.setLength(0);
                }
            }
        }
    };


    SensorEventListener accelerometerSensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta; // perform low-cut filter

                if (mAccel > 15 && abs(abs(x)-abs(prevX))>=20 ) {
                    prevX=x;
                    Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                    openQr.putExtra("btInstance", bluetoothInstance);
                    startActivityForResult(openQr, REQUEST_CODE_QR);
                }
            }
        }
    };
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode) {
                case REQUEST_CODE_QR:
                    // A REALIZAR
                    // CREAR UN ASYNCTASK QUE SI RECIBE UN 1 DE LA BARRERA DEL ARDUINO HAGA UN DISMISS DEL DIALOG Y TE AGREGUE EL PRODUCTO

                    // Si pudo ingresar el nuevo producto tengo que esperar a las barreras
                    final Producto productoNuevo = MainActivity.myAppDatabase.myDao().getProducto(data.getStringExtra("nombreProducto"));
                    // SE VA A PODER EDITAR POR POPUP LA CANTIDAD
                    if (productoNuevo != null){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AnadirProductoExpress.this);
                        final View miAlertDialog = LayoutInflater.from(AnadirProductoExpress.this).inflate(R.layout.alertdialog_producto, null);
                        final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextCantidadProducto);
                        builder.setView(miAlertDialog);
                        final AlertDialog dialog = builder.create();
                        Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoAceptar);

                        buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (etCantidad.getText().toString().isEmpty() || Integer.valueOf(etCantidad.getText().toString()) <= 0) {
                                    Toast.makeText(AnadirProductoExpress.this, "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                                } else {
                                    int cantidadNueva = Integer.valueOf(etCantidad.getText().toString());
                                    productoNuevo.setCantidad(cantidadNueva);
                                    if(!listaProductos.contains(productoNuevo)){
                                        anadirProducto(productoNuevo);
                                    } else {
                                        Toast.makeText(AnadirProductoExpress.this, "Ese producto ya está agregado a la lista.", Toast.LENGTH_SHORT).show();
                                        int i = 0;
                                        Iterator<Producto> iteratorProducto = listaProductos.iterator();
                                        Producto productoActual = iteratorProducto.next();
                                        while(iteratorProducto.hasNext() && ! productoNuevo.getNombre().equals(productoActual.getNombre())) {
                                            productoActual = iteratorProducto.next();
                                            i++;
                                        }
                                        int montoTotal = Integer.parseInt(precioParcial.getText().toString()) - listaProductos.get(i).getTotalPorProducto();
                                        listaProductos.get(i).sumarCantidadActual(cantidadNueva);
                                        precioParcial.setText(String.valueOf(listaProductos.get(i).getTotalPorProducto() + montoTotal));
                                        adaptator.notifyDataSetChanged();
                                    }
                                    dialog.dismiss();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AnadirProductoExpress.this);
                                    builder.setTitle("Ingrese el producto al chango\n");
                                    AlertDialog dialogIngresarProd = builder.create();
                                    // DESCOMENTAR ACA
                                    dialogIngresarProd.setCancelable(false);
                                    dialogIngresarProd.setCanceledOnTouchOutside(false);
                                    dialogIngresarProd.show();
                                    cantIngresar = cantidadNueva;
                                    Thread hilo = new Thread(){
                                        public void run(){
                                            while(cantIngresar > 0){}
                                            dialog.dismiss();
                                        }
                                    };
                                    hilo.start();
                                }
                            }
                        });

                        Button buttonCancelarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoCancelar);

                        buttonCancelarAlertProducto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }else{
                        Toast.makeText(this, "Este producto no se encuentra registrado en el sistema.", Toast.LENGTH_SHORT).show();
                    }
                break;
                case REQUEST_CODE_QR_QUITAR:
                    if ( ! reinicioLista ) {
                        int pos = 0;
                        String nombreProducto = data.getStringExtra("nombreProducto");
                        for (Producto producto : listaProductos) {
                            if (producto.getNombre().equals(nombreProducto)) {
                                break;
                            } else {
                                pos++;
                            }
                        }
                        if (! listaProductos.isEmpty() ) {
                            if (listaProductos.get(pos).getNombre().equals(nombreProducto)) {
                                Toast.makeText(AnadirProductoExpress.this, "Se quitara el producto " + nombreProducto, Toast.LENGTH_SHORT).show();
                                precioParcial.setText(String.valueOf(Integer.valueOf(precioParcial.getText().toString()) - listaProductos.get(pos).getTotalPorProducto()));
                                listaProductos.remove(pos);
                                adaptator.notifyDataSetChanged();
                            }
                        }
                    }
                break;
                default:
                    Intent refreshActivity = new Intent(this, AnadirProductoExpress.class);
                    refreshActivity.putExtra("btInstance", bluetoothInstance);
                    startActivity(refreshActivity);
                    this.finish();
            }
        }
    }

    private void anadirProducto(Producto producto){
        int montoTotal = Integer.parseInt(precioParcial.getText().toString());
        montoTotal += producto.getTotalPorProducto();
        precioParcial.setText(String.valueOf(montoTotal));
        listaProductos.add(producto);
        adaptator.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("", "[OnDestoy]: Dejo de verificar por los receivers");
        try {
            unregisterReceiver(myReceiver);
        }catch(Exception ex){}
    }

    @Override
    public void onBackPressed() {
        if(! listaProductos.isEmpty()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(AnadirProductoExpress.this);
            builder.setTitle("¿Seguro que desea finalizar la compra?")
                    .setMessage("Al finalizar la compra se le dira a que caja debe dirigirse")
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Random random = new Random();
                            AlertDialog.Builder builderFinalizarCompra = new AlertDialog.Builder(AnadirProductoExpress.this);
                            builderFinalizarCompra.setTitle("Compra Finalizada")
                                    .setMessage("Monto total acumulado $ " + precioParcial.getText() + "\nDirigase a la caja Nº " + (random.nextInt(10) + 1))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                            AlertDialog alertDialogFinalizarCompra = builderFinalizarCompra.create();
                            alertDialogFinalizarCompra.setCancelable(false);
                            alertDialogFinalizarCompra.setCanceledOnTouchOutside(false);
                            alertDialogFinalizarCompra.show();
                        }
                    });
            builder.show();
        }else {
            super.onBackPressed();
        }
    }
}
