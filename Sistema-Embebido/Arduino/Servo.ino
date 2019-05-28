/*
#include <Servo.h>

// Pines utilizados
#define PSERVO 9

Servo servo;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  // Vincular servo con el pin 9
  servo.attach(PSERVO);
  //Posicion Inicial Derecho
  irDerecho();
}

void loop() {
  doblarDerecha();
  delay(1000);
  irDerecho();
  delay(1000);
  doblarIzquierda(); 
  delay(1000);
  irDerecho();
  Serial.print(servo.read());
  delay(1000);
}

void doblarDerecha () {
  servo.write(45); // doblar derecha
}

void doblarIzquierda () {
  servo.write(130); // doblar izquierda
}

void irDerecho () {
  servo.write(90); // va derecho (posicion inicial)
}
*/
