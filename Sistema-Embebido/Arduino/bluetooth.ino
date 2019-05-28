/*
#include <SoftwareSerial.h>   // Incluimos la librería  SoftwareSerial  
SoftwareSerial BT(2,3);    // Definimos los pines RX y TX del Arduino conectados al Bluetooth

// el pin 2 (RX) del arduino va conectado a TX del BT
// el pin 3 (TX) del arduino va conectado a RX del BT 

void setup() {
  BT.begin(9600);       // Inicializamos el puerto serie BT (Para Modo AT 2)
  Serial.begin(9600);   // Inicializamos  el puerto serie  
}
 
void loop() {
  if(BT.available()) {    // Si llega un dato por el puerto BT se envía al monitor serial
    Serial.write(BT.read());
  }
 
  if(Serial.available()) {  // Si llega un dato por el monitor serial se envía al puerto BT
     BT.write(Serial.read());
  }
}
*/
