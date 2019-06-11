
#include <Servo.h>
#include <SoftwareSerial.h>   // Incluimos la librería  SoftwareSerial  
SoftwareSerial BT(2,3);    // Definimos los pines RX y TX del Arduino conectados al Bluetooth
// el pin 2 (RX) del arduino va conectado a TX del BT
// el pin 3 (TX) del arduino va conectado a RX del BT 
// Sensor Temperatura Analógico PIN A0
#define SENSOR_TEMP 0
// Pines utilizados ULTRASONIDO
#define LED_AMA 4
#define TRIGGER 5
#define ECHO 6

// Barreras
#define BARRERA_OUT 7
#define BARRERA_IN 8

#define PSERVO 9

// COMANDOS DEL BT
#define AVANZAR 'A'
#define DERECHA 'D'
#define IR_DERECHO 'F' // DE FORDWARD
#define IZQUIERDA 'I'
#define RETROCEDER 'B' // DE BACKWARD
#define STOP 'S'

char comando = 'X';

Servo servo;

// Pines utilizados MOTORES
#define RETROCEDE_IZQ 10
#define AVANZA_IZQ 11
#define RETROCEDE_DER 12
#define AVANZA_DER 13

// Constantes
const float sonido = 34300.0; // Velocidad del sonido en cm/s
const float distanciaStop = 10.0;    // distancia en Cm, donde el ultrasonido informa que paren los motores
int stopped = 0;
int saco = 0;
int puso = 0;
float temperatura = 0; //variable para la temperatura

unsigned long previousMillis = 0; // Tiempo Barrera anterior 0
unsigned long currentMillis = 0;  // Tiempo Barrera actual 0
unsigned long timeToSense = 300;  // Tiempo de espera entre senso de barrera, 1000ms = 1 segs

unsigned long pTimeTemp = 0; // Tiempo Temperatura anterior 0
unsigned long cTimeTemp = 0;  // Tiempo Temperatura actual 0
unsigned long timeToTemp = 5000; // Tiempo e espera entre senso de temperatura

void setup() {
  // Iniciamos el monitor serie
  Serial.begin(9600); // baudios con que emite
  BT.begin(9600);       // Inicializamos el puerto serie BT

  pinMode(LED_AMA, OUTPUT);
  // Modo entrada/salida de los pines
  pinMode(ECHO, INPUT);     // recibe
  pinMode(TRIGGER, OUTPUT); // emite
  pinMode(BARRERA_IN, INPUT);
  pinMode(BARRERA_OUT, INPUT);
   // Vincular servo con el pin 9
  servo.attach(PSERVO);
  pinMode(AVANZA_DER, OUTPUT);
  pinMode(RETROCEDE_DER, OUTPUT);
  pinMode(AVANZA_IZQ, OUTPUT);
  pinMode(RETROCEDE_IZQ, OUTPUT);
  
  
  //Posicion Inicial Derecho
  //irDerecho();
  // Arranca motor
  //avanzar(); // sacar
  digitalWrite(LED_AMA,LOW); //sacar  
  //digitalWrite(BARRERA_OUT,LOW); //sacar 
}

void loop() {
  /*
  currentMillis = millis();
  cTimeTemp = millis();
  
   // Si ha pasado el tiempo establecido, ejecutamos la acción
  if(currentMillis - previousMillis >= timeToSense) {
    //DO SOMETHING IN THIS TIME
      // Codigo para ver si la barrera_OUT fue cortada o no
    int valorIn = digitalRead(BARRERA_IN);
    int valorOut = digitalRead(BARRERA_OUT);
    if(valorIn == 1 && puso == 0) {
      //BT.print("BIN PUSO");// SE CORTO LA BARRERA
      Serial.print("BIN PUSO\n");
      puso = 1;
    } else if (valorIn == 0 && puso == 1) {
      Serial.print("No IN\n");
      puso = 0;
    }
    if(valorOut == 1 && saco == 0) {
      Serial.print("BOUT SACO\n");
      saco = 1;
    } else if (valorOut == 0 && saco == 1) {
      Serial.print("No OUT\n");
      saco = 0;
    }
    // Almacenamos el último momento en que hemos actuado 
    previousMillis = currentMillis; 
  } 

   // Si ha pasado el tiempo establecido, ejecutamos la acción
  if(cTimeTemp - pTimeTemp >= timeToTemp) {
     temperatura = calcularTemperatura(); 
     //String stringOne =  String(temperatura, 2); // convierte a string
   // BT.print(stringOne);  // no puedo hacer esto porque no te deja mandar string
    // Almacenamos el último momento en que hemos actuado 
    pTimeTemp = cTimeTemp; 
  }*/
 
  
  currentMillis = millis();
   // Si ha pasado el tiempo establecido, ejecutamos la acción
  if(currentMillis - previousMillis >= timeToSense) {
    // Codigo para ver si la barrera_OUT fue cortada o no
    int valorOut = digitalRead(BARRERA_OUT);
    Serial.print(valorOut);
    if(valorOut == 1 && saco == 0) {
      //BT.write("O");// SE CORTO LA BARRERA
      //BT.print("BOUT SACO");// SE CORTO LA BARRERA
      Serial.print("BOUT SACO\n");
      saco = 1;
    } else if (valorOut == 0 && saco == 1) {
      //BT.write("N");
      Serial.print("No\n");
      saco = 0;
    }
    // Almacenamos el último momento en que hemos actuado 
    previousMillis = currentMillis; 
  }
  

  /*
  currentMillis = millis();
   // Si ha pasado el tiempo establecido, ejecutamos la acción
  if(currentMillis - previousMillis >= timeToSense) {
    //DO SOMETHING IN THIS TIME
      // Codigo para ver si la barrera_OUT fue cortada o no
    int valorIn = digitalRead(BARRERA_IN);
    Serial.print(valorIn);
    if(valorIn == 1 && puso == 0) {
      //BT.write("I"); // SE CORTO LA BARRERA
      BT.print("BIN PUSO");// SE CORTO LA BARRERA
      Serial.print("BIN PUSO\n");
      puso = 1;
    } else if (valorIn == 0 && puso == 1) {
      //BT.write("N");
      Serial.print("No\n");
      puso = 0;
    }
    // Almacenamos el último momento en que hemos actuado 
    previousMillis = currentMillis; 
  }   
  */
 
  /*
  // Preparamos el sensor de ultrasonidos
  iniciarTrigger();

  // Obtenemos la distancia
  float distancia = calcularDistancia();

  // Lanzamos alerta si estamos dentro del rango de peligro
  if (distancia <= distanciaStop) { 
    // PARAMOS MOTOR y reversa
    stopped = 1;
    digitalWrite(LED_AMA,HIGH);
    pararMotor();
  
  } else if (stopped) {
      digitalWrite(LED_AMA,LOW);
      stopped = 0;
      irDerecho();    
  }

  if(BT.available()) {    // Si llega un dato por el puerto BT se envía al monitor serial
    comando = BT.read();
    Serial.print(comando);
    switch (comando) {
        case AVANZAR:
                    avanzar();
                    break;
        case RETROCEDER:
                    retroceder();
                    break;
        case STOP:
                    pararMotor();
                    break;
        case DERECHA:
                    doblarDerecha();
                    break;
        case IZQUIERDA:
                    doblarIzquierda();
                    break;
        case IR_DERECHO:
                    irDerecho();
                    break;
        default:
                Serial.write("ERROR COMANDO\n");
                break;
    }
  }
 
  if(Serial.available()) {  // Si llega un dato por el monitor serial se envía al puerto BT
     BT.write(Serial.read());
  }
  */
}

float calcularTemperatura () {
  //Calcula la temperatura usando como referencia 5v
  float temp = 0;
  temp = (5.0 * analogRead(SENSOR_TEMP)*100.0)/1023.0;
  Serial.println (temp); //escribe la temperatura en el serial
  return temp;  
}

// Método que inicia la secuencia del trigger para comenzar a medir

void iniciarTrigger() {
  // Ponemos el Triger en estado bajo y esperamos 2 microsegs
  digitalWrite(TRIGGER, LOW);
  delayMicroseconds(2);

  // Ponemos el pin Trigger a estado alto y esperamos 10 microsegs
  digitalWrite(TRIGGER, HIGH);
  delayMicroseconds(10);

  // Comenzamos poniendo el pin Trigger en estado bajo
  digitalWrite(TRIGGER, LOW);
}

// Método que calcula la distancia a la que se encuentra el objeto
// Devuelve una variable tipo float que contiene la distancia

float calcularDistancia() {
  // La función pulseIn obtiene el tiempo que tarda en cambiar entre estados, en este caso a HIGH
  unsigned long tiempo = pulseIn(ECHO, HIGH);

  // Obtenemos la distancia en cm, hay que convertir el tiempo en segudos ya que está en microsegundos
  // por eso se multiplica por 0.000001
  float distancia = tiempo * 0.000001 * sonido / 2.0;
  delay(500);

  return distancia;
}

void avanzar () {
  digitalWrite(RETROCEDE_DER,LOW);
  digitalWrite(AVANZA_DER,HIGH);
  digitalWrite(RETROCEDE_IZQ,LOW);
  digitalWrite(AVANZA_IZQ,HIGH);
}

void retroceder () {
  digitalWrite(AVANZA_DER,LOW);
  digitalWrite(RETROCEDE_DER,HIGH);
  digitalWrite(AVANZA_IZQ,LOW);
  digitalWrite(RETROCEDE_IZQ,HIGH);
}

void pararMotor() {
  digitalWrite(AVANZA_DER,LOW);
  digitalWrite(RETROCEDE_DER,LOW);
  digitalWrite(AVANZA_IZQ,LOW);
  digitalWrite(RETROCEDE_IZQ,LOW);
}

void doblarDerecha () {
  servo.write(130); // doblar derecha
}

void doblarIzquierda () {
  servo.write(45); // doblar izquierda
}

void irDerecho () {
  servo.write(90); // va derecho (posicion inicial)
}
