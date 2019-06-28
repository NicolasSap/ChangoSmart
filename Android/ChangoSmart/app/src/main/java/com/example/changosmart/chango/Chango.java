package com.example.changosmart.chango;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.changosmart.R;

import java.nio.charset.Charset;
import java.util.Objects;

import BT.Bluetooth;
import BT.BluetoothConnectionService;

//Clase para manejar el movimiento del chango
public class Chango extends AppCompatActivity {
    private Button buttonUp;

    private Button buttonRight;

    private Button buttonDown;

    private Button buttonLeft;

    private char establecerComandoMovimiento;

    private char establecerComandoLuz;

    private char establecerComandoMovimientoServo;

    private Bluetooth bluetoothInstance;

    private BluetoothConnectionService bluetoothConnection;

    private byte[] commandInBytes;

    private float prevLuz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_chango);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Instancias de los sensores
        //Se le asigna 15 por que se da por entendido que se va a probar en lugares con luz, de igual forma se valida debajo.
        prevLuz = 15;
        SensorManager mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        Sensor myLightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (myProximitySensor != null) {
            mySensorManager.registerListener(proximitySensorEventListener,myProximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (myLightSensor != null) {
            mySensorManager.registerListener(lightSensorEventListener,myLightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

        //Inicializo los botones con los correspondientes controles
        buttonUp = findViewById(R.id.buttonUp);
        buttonRight = findViewById(R.id.buttonRight);
        buttonDown = findViewById(R.id.buttonDown);
        buttonLeft = findViewById(R.id.buttonLeft);

        //Instancio el bt actual en el activity
        bluetoothInstance = Objects.requireNonNull(getIntent().getExtras()).getParcelable("btInstance");

        if (Objects.requireNonNull(bluetoothInstance).getPairDevice() != null){
            bluetoothConnection = new BluetoothConnectionService(getApplicationContext());
            //Se declara un receiver para obtener los datos que envíe el embebebido.
            LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter("IncomingMessage"));
            //Si tengo un disposito conectado comienzo la conexión.
            bluetoothConnection.startClient(bluetoothInstance.getPairDevice(), bluetoothConnection.getDeviceUUID());

        }

        //Se establecen los comandos iniciales para el embebido
        establecerComandoMovimiento = 'S';
        establecerComandoMovimientoServo = 'F';

        //Si se presiona el boton avanzar
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                //Si el estado anterior era una A, significa que debe detenerse ( S ), sino, avance (A)
                if ( establecerComandoMovimiento == 'S') {
                    establecerComandoMovimiento = 'A';
                    enviarInformacionMovimiento(establecerComandoMovimiento);
                    establecerComandoMovimientoServo = 'F';
                    enviarInformacionMovimiento(establecerComandoMovimientoServo);
                } else if ( establecerComandoMovimiento == 'B') {
                    establecerComandoMovimiento = 'S';
                    enviarInformacionMovimiento(establecerComandoMovimiento);
                } else if ( establecerComandoMovimientoServo == 'I' || establecerComandoMovimientoServo == 'D') {
                    establecerComandoMovimientoServo = 'F';
                    enviarInformacionMovimiento(establecerComandoMovimientoServo);
                }
            }
        });

        //Si se presiona el boton izquierdo
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                establecerComandoMovimientoServo = 'I';
                enviarInformacionMovimiento(establecerComandoMovimientoServo);
            }
        });

        //Si se presiona el boton retroceder
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                //Si el estado anterior era una A, significa que debe detenerse ( S ), sino, retrocedo (b)
                if ( establecerComandoMovimiento == 'S') {
                    establecerComandoMovimiento = 'B';
                    enviarInformacionMovimiento(establecerComandoMovimiento);
                    establecerComandoMovimientoServo = 'F';
                    enviarInformacionMovimiento(establecerComandoMovimientoServo);
                } else if ( establecerComandoMovimiento == 'A') {
                    establecerComandoMovimiento = 'S';
                    enviarInformacionMovimiento(establecerComandoMovimiento);
                } else if ( establecerComandoMovimientoServo == 'I' || establecerComandoMovimientoServo == 'D') {
                    establecerComandoMovimientoServo = 'F';
                    enviarInformacionMovimiento(establecerComandoMovimientoServo);
                }
            }
        });

        //Si se presiona el boton derecho
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                establecerComandoMovimientoServo = 'D';
                enviarInformacionMovimiento(establecerComandoMovimientoServo);
            }
        });

    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Acá se recibe el mensaje del arduino y se evalua si es In o Out
            String text = intent.getStringExtra("theMessage");

            Log.d("temperatura",text);

            //Si recibe avanzar habilite el avance.
            if (text.equals("G")){
                buttonUp.setVisibility(View.VISIBLE);
                buttonUp.setBackgroundColor(getResources().getColor(R.color.mainTitlesColor));

                Toast toast1 =
                        Toast.makeText(getApplicationContext(), "ME PUEDO MOVER", Toast.LENGTH_SHORT);

                toast1.setGravity(Gravity.CENTER,0,0);

                toast1.show();

                //Si recibe no avanzar, deshabilite el avance y envie un freno al embebido.
            }else if (text.equals("N")){
                buttonUp.setVisibility(View.INVISIBLE);
                buttonUp.setBackgroundColor(Color.LTGRAY);
                //Seteo variables (la accion la hace el arduino).
                establecerComandoMovimiento = 'S';
                establecerComandoMovimientoServo = 'F';

                Toast toast1 =
                        Toast.makeText(getApplicationContext(), "ULTRASONIDO ACTIVADO", Toast.LENGTH_SHORT);

                toast1.setGravity(Gravity.CENTER,0,0);

                toast1.show();
            }
        }
    };

    //Método para envíar información al arduino.
    public void enviarInformacionMovimiento(char direccionMovimiento){
        if (bluetoothInstance.getPairDevice() == null){
            //Si no estoy conectado al bluetooth o se pierde la señal
            // Pido reconectarse
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No estás conectado a ningún dispositivo. Conectate vía bluetooth por favor... ", Toast.LENGTH_SHORT);

            toast1.setGravity(Gravity.CENTER,0,0);

            toast1.show();
        } else {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(), "Caracter a enviar: " + direccionMovimiento, Toast.LENGTH_SHORT);

            toast1.setGravity(Gravity.CENTER,0,0);

            toast1.show();
            byte[] commandInBytes = String.valueOf(direccionMovimiento).getBytes(Charset.defaultCharset());

            bluetoothConnection.write(commandInBytes);
        }
    }

    SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0) {
                    //El sensor detecta algo próximo
                    //Envía un mensaje al arduino para que frene.
                    establecerComandoMovimiento = 'S';
                    enviarInformacionMovimiento(establecerComandoMovimiento);
                    establecerComandoMovimientoServo = 'F';
                    enviarInformacionMovimiento(establecerComandoMovimientoServo);

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), "Sensor proximidad celular activado.", Toast.LENGTH_SHORT);

                    toast1.setGravity(Gravity.CENTER,0,0);

                    toast1.show();

                } else {
                    buttonUp.setEnabled(true);
                    buttonUp.setBackgroundColor(getResources().getColor(R.color.mainTitlesColor));

                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), "Sensor proximidad celular desactivado", Toast.LENGTH_SHORT);

                    toast1.setGravity(Gravity.CENTER,0,0);

                    toast1.show();
                }
            }
        }
    };

    SensorEventListener lightSensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            //Se le agrega una verificación por un valor previo del sensor, por que sino cambia constantemente.
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                if (event.values[0] < 10 && prevLuz > 10) {
                    //El sensor no detecta luz y estaba apagada la luz del carrito
                    prevLuz=event.values[0];
                    //Encender luz carrito
                    establecerComandoLuz = 'L';
                    enviarInformacionMovimiento(establecerComandoLuz);
                } else {
                    if (event.values[0] >= 10 && prevLuz < 10) {
                        //El sensor detecta luz y estaba encedida la luz del carrito
                        prevLuz=event.values[0];
                        //Apagar luz carrito
                        establecerComandoLuz = 'L';
                        enviarInformacionMovimiento(establecerComandoLuz);
                    }
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 18) {
            if(resultCode == RESULT_OK) {
                bluetoothInstance = Objects.requireNonNull(data.getExtras()).getParcelable("btInstanceBack");
            }
        }
    }
}
