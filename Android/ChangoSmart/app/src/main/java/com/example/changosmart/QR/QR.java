package com.example.changosmart.QR;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.changosmart.R;
import com.example.changosmart.compras.express.AnadirProductoExpress;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Objects;

import BT.Bluetooth;

public class QR extends AppCompatActivity {


    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String token = "";
    private String tokenanterior = "";

    private Bluetooth bluetoothInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        initQR();
    }

    public void initQR() {

        // creo el detector qr
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        // creo la camara
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build();

        // listener de ciclo de vida de la camara
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                bluetoothInstance = Objects.requireNonNull(getIntent().getExtras()).getParcelable("btInstance");

                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(QR.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA);
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                } else {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // preparo el detector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0) {

                    // obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString();

                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior)) {

                        // guardamos el ultimo token proceado

                        tokenanterior = token;
                        Log.e("MENSAJE A ENVIAR", tokenanterior);
                        Intent intentQR = new Intent(QR.this, AnadirProductoExpress.class);
                        intentQR.putExtra("nombreProducto",tokenanterior);
                        intentQR.putExtra("btInstance", bluetoothInstance);
                        setResult(RESULT_OK, intentQR);
                        finish();
                    }
                }
            }
        });
    }
}