#include <Servo.h>
#include <SoftwareSerial.h>   // Incluimos la librería  SoftwareSerial  

SoftwareSerial BT(2,3);    // Definimos los pines RX y TX del Arduino conectados al Bluetooth
// El pin 2 (RX) del arduino va conectado a TX del BT
// El pin 3 (TX) del arduino va conectado a RX del BT 
// Sensor Temperatura Analógico PIN A0
#define SENSOR_TEMP 0
// Pines utilizados ULTRASONIDO
#define LED_VERDE 4
// A2 LED SENSOR CELULAR
#define TRIGGER 5
#define ECHO 6
// Barreras
//BARRERA_OUT ES A1
//BARRERA_IN ES A3
// Servo
#define PSERVO 9
// Pines utilizados MOTORES
#define RETROCEDE_IZQ 10
#define AVANZA_IZQ 11
#define RETROCEDE_DER 12
#define AVANZA_DER 13

// COMANDOS DEL BT
#define AVANZAR 'A'
#define DERECHA 'D'
#define IR_DERECHO 'F' // DE FORDWARD
#define IZQUIERDA 'I'
#define RETROCEDER 'B' // DE BACKWARD
#define STOP 'S'
#define LED_FRONT 'L' // ACTIVADO POR SENSOR LUMINICO ANDROID

// Constantes y Variables
Servo servo;

int backward = 0;
int forward = 0;
int stopped = 0;
int saco = 0;
int puso = 0;
float temperatura = 0; //variable para la temperatura

char comando = 'X';

const float sonido = 34300.0; // Velocidad del sonido en cm/s
const float distanciaStop = 30.0;    // distancia en Cm, donde el ultrasonido informa que paren los motores

unsigned long pTimeUltra = 0; // Tiempo ultra anterior 0
unsigned long cTimeUltra = 0;  // Tiempo ultra actual 0
unsigned long timeToUltra = 100;  // Tiempo de espera entre ultras, 1000ms = 1 segs

unsigned long previousMillis = 0; // Tiempo Barrera anterior 0
unsigned long currentMillis = 0;  // Tiempo Barrera actual 0
unsigned long timeToSense = 300;  // Tiempo de espera entre senso de barrera, 1000ms = 1 segs

unsigned long pTimeTemp = 0; // Tiempo Temperatura anterior 0
unsigned long cTimeTemp = 0;  // Tiempo Temperatura actual 0
unsigned long timeToTemp = 10000; // Tiempo de espera entre senso de temperatura


void setup() {
  // Iniciamos el monitor serie
  Serial.begin(9600); // baudios con que emite
  BT.begin(9600);       // Inicializamos el puerto serie BT
  
  // Modo entrada/salida de los pines
  pinMode(A2, OUTPUT); // LED SENSOR CELULAR
  pinMode(LED_VERDE, OUTPUT);
  pinMode(ECHO, INPUT);     // recibe
  pinMode(TRIGGER, OUTPUT); // emite
  pinMode(A3, INPUT); // BARRERA_IN
  pinMode(A1, INPUT); // BARRERA OUT
  servo.attach(PSERVO); // Vincular servo con el pin 9
  pinMode(AVANZA_DER, OUTPUT);
  pinMode(RETROCEDE_DER, OUTPUT);
  pinMode(AVANZA_IZQ, OUTPUT);
  pinMode(RETROCEDE_IZQ, OUTPUT);
  
  // Servo - Posicion Inicial Derecho
  irDerecho();
  digitalWrite(LED_VERDE,LOW);
  digitalWrite(A2,LOW); // LED FRONT
}

void loop() {
  
  cTimeUltra = millis();
  
  if(cTimeUltra - pTimeUltra >= timeToUltra) {
    // Preparamos el sensor de ultrasonido
    iniciarTrigger();
    // Obtenemos la distancia
    float distancia = calcularDistancia();
    // Lanzamos alerta si estamos dentro del rango de peligro
    if (distancia <= distanciaStop && stopped == 0 && forward == 1) { 
      // PARAMOS MOTOR, RUEDA DERECHA, ENCIENDO LED PARADO Y AVISA APP
      stopped = 1;
      digitalWrite(LED_VERDE,HIGH);
      BT.print("N");
      pararMotor();
      if (servo.read() != 90) {
        irDerecho();
      }
    } else if (distancia >= distanciaStop && stopped == 1) {
      // APAGO LED PARADO Y AVISA APP
      digitalWrite(LED_VERDE,LOW);
      BT.print("G");
      forward = 0;
      stopped = 0;    
    }
    pTimeUltra = cTimeUltra; 
  }
  
  if (forward == 0 || backward == 0) { // Si no se esta moviendo, sensa la temperatura y las barreras
    currentMillis = millis();
    cTimeTemp = millis();
    // Si ha pasado el tiempo establecido, ejecutamos la acción
    if(currentMillis - previousMillis >= timeToSense) {
      // Codigo para ver si la barrera_OUT fue cortada o no
      int valorIn = digitalRead(A3);
      int valorOut = digitalRead(A1);
      if(valorIn == 1 && puso == 0) {
        BT.print("E");// SE CORTO LA BARRERA
        puso = 1;
      } else if (valorIn == 0 && puso == 1) {
        puso = 0;
      }
      if(valorOut == 1 && saco == 0) {
        BT.print("O");// SE CORTO LA BARRERA
        saco = 1;
      } else if (valorOut == 0 && saco == 1) {
        saco = 0;
      }
      // Almacenamos el último momento en que hemos actuado 
      previousMillis = currentMillis; 
    } 
    
    // Si ha pasado el tiempo establecido, medimos la temperatura
    if(cTimeTemp - pTimeTemp >= timeToTemp) {
      temperatura = calcularTemperatura(); 
      String stringOne =  String(temperatura, 0); // convierte a string  
      BT.print(stringOne[0]);  
      BT.print(stringOne[1]);  
      // Almacenamos el último momento en que hemos actuado 
      pTimeTemp = cTimeTemp; 
    }
  } else { // Si se esta moviendo reseteo contadores
    currentMillis = 0;
    cTimeTemp = 0;
  }
  
  if(BT.available()) {    // Si llega un dato por el puerto BT, me fijo que accion realizar
    comando = BT.read();
    switch (comando) {
        case AVANZAR:
                    avanzar();
                    forward = 1;
                    break;
        case RETROCEDER:
                    retroceder();
                    backward = 1;
                    break;
        case STOP:
                    pararMotor();
                    backward = 0;
                    forward = 0;
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
        case LED_FRONT:
                    if (digitalRead(A2)) {
                      digitalWrite(A2,LOW); // Si esta prendido, apago
                    } else {
                      digitalWrite(A2,HIGH); // Si esta apagado, prendo
                    }
                    break;
        default:
                Serial.write("ERROR COMANDO\n");
                break;
    }
  }
}

float calcularTemperatura () {
  // Calcula la temperatura usando como referencia 5v, Multiplica por 100, porque 1 grado centigrado
  // es igual a 10mV (0.01V), por lo tanto el valor obtenido debo dividirlo por 0.01 que es lo mismo que multiplicarlo por 100
  // y es 1023 porque se pueden obtener de la lectura analógica 1024 valores posibles, osea desde 0 a 1023
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
float calcularDistancia() {
  // La función pulseIn obtiene el tiempo que tarda en cambiar entre estados, en este caso a HIGH
  unsigned long tiempo = pulseIn(ECHO, HIGH);

  // Obtenemos la distancia en cm, hay que convertir el tiempo en segudos ya que está en microsegundos
  // por eso se multiplica por 0.000001, dividio 2 porque porque va y viene (se manda y despues rebota y vuelve)
  float distancia = tiempo * 0.000001 * sonido / 2.0;
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
