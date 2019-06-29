package com.example.changosmart.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.lang.reflect.Method;
import java.util.ArrayList;

import BT.Bluetooth;
import BT.BluetoothConnectionService;

//Clase para manejar el bluetooth, va a servir para gestionar el mismo en toda la app.
public class BluetoothSettings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static BluetoothAdapter miAdaptadorBluetooth;

    private ArrayList<BluetoothDevice> listaDispositivos;

    private static MiAdaptadorDispositivosBluetooth adaptadorBluetooth;

    private ListView listaDispositivosView;

    private TextView estadoBluetooth;

    private TextView conectadoADispositivo;

    private Bluetooth bluetoothLocalInstance;

    private static BluetoothDevice pairDevice;

    private boolean operacionExistosa;

    private boolean emparejamientoExitoso;

    //Se crea un BroadcastReceiver para las acciones del Bluetooth
    private final BroadcastReceiver miBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Cuando se encuentra una acción del bt y es que cambió
            if ( action.equals(miAdaptadorBluetooth.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, miAdaptadorBluetooth.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("", "onReceive: STATE OFF");
                        operacionExistosa = false;
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("", "onReceive: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d("", "onReceive: STATE ON");
                        operacionExistosa = true;
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d("", "onReceive: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    //Se crea un BroadcastReceiver para las listas de los dispositivos encontados del Bluetooth
    private final BroadcastReceiver miBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            //Si la acción es que encontró algo entonces lo agrego a la lista de dispositivos.
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                //Creo una instancia de un dispositivo bluetooth.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("", "[BroadcasReceiver]: Se agrega un dispositivo a la lista:" + device.getName());
                listaDispositivos.add(device);
                //Creo el adaptador con esa lista.
                adaptadorBluetooth = new MiAdaptadorDispositivosBluetooth(context, listaDispositivos);
                listaDispositivosView.setAdapter(adaptadorBluetooth);
            }
        }
    };

    //Se crea un BroadcastReceiver para los eventos por ejemplo de paring de Bluetooth
    private final BroadcastReceiver miBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            //Si la acción es que encontró algo entonces lo agrego a la lista de dispositivos.
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //Una vez que se tiene el dispositivo, infomo el estado de la acción.
                //Bonded es si la conexión se estableció.
                if (device.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d("", "[BroadcasReceiver]: Emparejado");
                    mostrarNotificacionBT("Emparejamiento existoso!");
                    conectadoADispositivo.setText(device.getName().isEmpty() ? "Nombre protegido" : device.getName());
                    emparejamientoExitoso = true;
                }
                //Bonding es en proceso.
                if (device.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d("", "[BroadcasReceiver]: Emparejando");
                    mostrarNotificacionBT("Emparejando...");
                }
                //None es si la conexión se rompió por algún motivo.
                if (device.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d("", "[BroadcasReceiver]: Algo salió mal!");
                    mostrarNotificacionBT("Oops.. Emparejamiento fallido.");
                    emparejamientoExitoso = false;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("", "[OnDestoy]: Dejo de verificar por los receivers");
        try {
            unregisterReceiver(miBroadcastReceiver1);
        }catch(Exception ex){}
        try {
            unregisterReceiver(miBroadcastReceiver2);
        }catch(Exception ex){}
        try {
            unregisterReceiver(miBroadcastReceiver3);
        }catch(Exception ex){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtengo la instancia del main para modificarla.
        bluetoothLocalInstance = getIntent().getExtras().getParcelable("btInstance");

        setContentView(R.layout.activity_bluetooth_settings);
        Button btnActivarBt = (Button) findViewById(R.id.activarBluetooth);
        Button btnDesactivarBt = (Button) findViewById(R.id.desactivarBluetooth);
        Button btnMostrarDispositivos = (Button) findViewById(R.id.mostrarDispositivos);
        Button btnVolver = (Button) findViewById(R.id.volver);
        estadoBluetooth = (TextView) findViewById(R.id.textView6);
        conectadoADispositivo = (TextView) findViewById(R.id.textView9);

        listaDispositivosView = (ListView) findViewById(R.id.listaDispositivos);
        listaDispositivos = new ArrayList<BluetoothDevice>();

        //Broadcast para detectar cambiar por ejemplo paring con otro dispositivo
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(miBroadcastReceiver3, filter);

        miAdaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
        operacionExistosa = false;
        emparejamientoExitoso = false;

        if (bluetoothLocalInstance != null ){
            if (miAdaptadorBluetooth.isEnabled()) {
                estadoBluetooth.setText("Encendido");
                if (bluetoothLocalInstance.getPairDevice() != null){
                    conectadoADispositivo.setText( bluetoothLocalInstance.getPairDevice().getName());
                } else {
                    conectadoADispositivo.setText("");
            }
                bluetoothLocalInstance.setMyBluetoothStatus(true);
            }else {
                estadoBluetooth.setText("Apagado");
                conectadoADispositivo.setText("");
                bluetoothLocalInstance.setMyBluetoothStatus(false);
            }

            listaDispositivosView.setOnItemClickListener(BluetoothSettings.this);

            //Se activa el bluetooth en el dispositivo
            btnActivarBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activarBT();
                }
            });

            //Se desactiva el bluetooth en el dispositivo
            btnDesactivarBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    desactivarBT();
                }
            });

            //Se muestran los dispositivos que están con el bt activado.
            btnMostrarDispositivos.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    listaDispositivos.clear();

                    //Se verifica que se pueda usar el bt en el dispositivo
                    checkBTPermissions();

                    mostrarNotificacionBT("Buscando dispositivos...");
                    miAdaptadorBluetooth.startDiscovery();
                    IntentFilter dispositivosEncontradosIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(miBroadcastReceiver2, dispositivosEncontradosIntent);
                }
            });

            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Intent backToFather = new Intent();
                    backToFather.putExtra("btInstanceBack", bluetoothLocalInstance);
                    if (emparejamientoExitoso){
                        setResult(RESULT_OK, backToFather);
                    }else{
                        setResult(RESULT_CANCELED, backToFather);
                    }
                    finish();
                }
            });
        }
    }

    public void activarBT(){
        if ( miAdaptadorBluetooth == null ){
            Log.d("", "[activarBT]: No es posible activar el bluetooth devido a problemas con el dispositivo.");
        }
        if ( !miAdaptadorBluetooth.isEnabled()){
            Intent activarBTIntent = new Intent( miAdaptadorBluetooth.ACTION_REQUEST_ENABLE);
            startActivity(activarBTIntent);

            //Con el IntentFilter espero a que me llegue un bradcast informando el cambio en el bt para seguir.
            IntentFilter BTIntent = new IntentFilter(miAdaptadorBluetooth.ACTION_STATE_CHANGED);
            registerReceiver(miBroadcastReceiver1, BTIntent);

            mostrarNotificacionBT("Bluetooth activado!");
            estadoBluetooth.setText("Encendido");
            bluetoothLocalInstance.setMyBluetoothStatus(true);
        }
    }

    public void desactivarBT(){
        if ( miAdaptadorBluetooth == null ){
            Log.d("", "[activarBT]: No es posible activar el bluetooth devido a problemas con el dispositivo.");
        }
        if ( miAdaptadorBluetooth.isEnabled()){
            miAdaptadorBluetooth.disable();

            //Con el IntentFilter espero a que me llegue un bradcast informando el cambio en el bt para seguir.
            IntentFilter BTIntent = new IntentFilter(miAdaptadorBluetooth.ACTION_STATE_CHANGED);
            registerReceiver(miBroadcastReceiver1, BTIntent);

            mostrarNotificacionBT("Bluetooth desactivado!");
            estadoBluetooth.setText("Apagado");
            conectadoADispositivo.setText("");
            listaDispositivos.clear();
            bluetoothLocalInstance.setMyPairDevice(null);
            bluetoothLocalInstance.setMyBluetoothStatus(false);
            adaptadorBluetooth = new MiAdaptadorDispositivosBluetooth(this, listaDispositivos);
            listaDispositivosView.setAdapter(adaptadorBluetooth);
        }
    }

    //Se encarga de mostrar una leve notificación de lo que está pasando por atrás con el bluetooth.
    public void mostrarNotificacionBT(String mensaje){
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        mensaje, Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER,0,0);

        toast.show();
    }

    //Esto verifica la versión de android para ver si tiene bluetooth.
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions(){
        if ( Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifiest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifiest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0){
                this.requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Dejo de buscar dispositivos por ahorro de recursos.
        miAdaptadorBluetooth.cancelDiscovery();

        Log.d("", "[OnDeviceClick]: Dispositivo seleccionado");
        String deviceName = listaDispositivos.get(i).getName();
        if (deviceName == null || deviceName.equals("")) {
            deviceName = "Nombre protegido";
        }
        String deviceAdress = listaDispositivos.get(i).getAddress();

        Log.d("", "[OnDeviceClick]: Dispositivo:" + deviceName + " - " + deviceAdress);

        //Creamos un bond para el receiver.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {

            if ( listaDispositivos.get(i).getBondState() == BluetoothDevice.BOND_BONDED ){
                //Le asigno el dispositivo emparejado a la instancia del bt.
                bluetoothLocalInstance.setPairDevice(listaDispositivos.get(i));

                Log.d("", "[BroadcasReceiver]: Emparejado");
                mostrarNotificacionBT("Emparejamiento existoso!");
                conectadoADispositivo.setText(listaDispositivos.get(i).getName().isEmpty() ? "Nombre protegido" : listaDispositivos.get(i).getName());
                emparejamientoExitoso = true;

            }else {
                Log.d("", "[OnDeviceClick]: Inicializando paring con el dispositivo: " + deviceName);
                listaDispositivos.get(i).createBond();

                //Le asigno el dispositivo emparejado a la instancia del bt.
                bluetoothLocalInstance.setPairDevice(listaDispositivos.get(i));
            }
        }
    }
}