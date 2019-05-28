/*
// Pines utilizados
#define LEDVERDE 2
#define LEDAMARILLO 3
#define LEDROJO 4
#define TRIGGER 5
#define ECHO 6

// Constantes
const float sonido = 34300.0; // Velocidad del sonido en cm/s
const float umbral1 = 30.0;
const float umbral2 = 20.0;
const float umbral3 = 10.0;

void setup() {
  // put your setup code here, to run once:
  // Iniciamos el monitor serie
  Serial.begin(9600);

  // Modo entrada/salida de los pines
  pinMode(LEDVERDE, OUTPUT);
  pinMode(LEDAMARILLO, OUTPUT);
  pinMode(LEDROJO, OUTPUT);
  pinMode(ECHO, INPUT);
  pinMode(TRIGGER, OUTPUT);

  // Apagamos todos los LEDs
  apagarLEDs();
}

void loop() {
  // put your main code here, to run repeatedly:
  // Preparamos el sensor de ultrasonidos
  iniciarTrigger();

  // Obtenemos la distancia
  float distancia = calcularDistancia();

  // Apagamos todos los LEDs
  apagarLEDs();

  // Lanzamos alerta si estamos dentro del rango de peligro
  if (distancia < umbral1) {
    // Lanzamos alertas
    alertas(distancia);
  }
}

// Apaga todos los LEDs

void apagarLEDs() {
  // Apagamos todos los LEDs
  digitalWrite(LEDVERDE, LOW);
  digitalWrite(LEDAMARILLO, LOW);
  digitalWrite(LEDROJO, LOW);
}

// Método que inicia la secuencia del trigger para comenzar a medir

void iniciarTrigger() {
  // Ponemos el Triiger en estado bajo y esperamos 2 microsegs
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
  Serial.print(distancia);
  Serial.print("cm");
  Serial.println();
  delay(500);

  return distancia;
}

//Función que comprueba si hay que lanzar alguna alerta visual

void alertas(float distancia) {
  if (distancia < umbral1 && distancia >= umbral2) {
    // Encendemos el LED verde
    digitalWrite(LEDVERDE, HIGH);
  } else if (distancia < umbral2 && distancia > umbral3) {
    // Encendemos el LED amarillo
    digitalWrite(LEDAMARILLO, HIGH);
  } else if (distancia <= umbral3) {
    // Encendemos el LED rojo
    digitalWrite(LEDROJO, HIGH);
  }
}
*/
