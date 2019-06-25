package BT;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.nio.charset.Charset;

//Clase global de bluetooth, tiene que implementar Parcelable para pasarse a través de las activities.
public class Bluetooth extends Application implements Parcelable {

    //Controlar el dispositivo actual conectado.
    private BluetoothDevice pairDevice;

    //Controlar el estado actual del bluetooth local.
    private boolean bluetoothActivado;

    //Constructor que obtiene el estado por defecto del dispostivo;
    public Bluetooth(){
        pairDevice = null;
        bluetoothActivado = BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    public Bluetooth(Parcel in) {
        this.pairDevice = (BluetoothDevice)in.readValue(BluetoothDevice.class.getClassLoader());
        this.bluetoothActivado = (boolean)in.readValue(Boolean.class.getClassLoader());
    }

    //Retorna el dispositivo que tenga emparejado.
    public BluetoothDevice getMyPairDevice(){
        return pairDevice;
    }

    //Asigna el dispositivo que tenga emparejado.
    public void setMyPairDevice(BluetoothDevice pairDevice ){
        this.pairDevice = pairDevice;
    }

    //Obtiene el estado actual del bluetooth
    public boolean getMyBluetoothStatus(){
        return bluetoothActivado;
    }

    //Setea el estado actual del bluetooth.
    public void setMyBluetoothStatus(boolean status){
        bluetoothActivado = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pairDevice);
        dest.writeValue(bluetoothActivado);
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     *
     * If you don’t do that, Android framework will through exception
     * Parcelable protocol requires a Parcelable.Creator object called CREATOR
     */
    public static final Parcelable.Creator<Bluetooth> CREATOR = new Parcelable.Creator<Bluetooth>() {

        public Bluetooth createFromParcel(Parcel in) {
            return new Bluetooth(in);
        }

        public Bluetooth[] newArray(int size) {
            return new Bluetooth[size];
        }
    };

    public BluetoothDevice getPairDevice() {
        return pairDevice;
    }

    public void setPairDevice(BluetoothDevice device) {
        this.pairDevice = device;
    }

    public boolean getBluetoothStatus() {
        return this.bluetoothActivado;
    }

    public void setBluetoothStatus(boolean status) {
        this.bluetoothActivado = status;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Bluetooth) {
            Bluetooth toCompare = (Bluetooth) obj;
            return (this.pairDevice == (toCompare.getPairDevice()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.getPairDevice()).hashCode();
    }
}
