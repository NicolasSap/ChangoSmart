// Pines utilizados ULTRASONIDO
#define TRIGGER 5
#define ECHO 6
// Pines utilizados MOTORES
#define RETROCEDE_IZQ 10
#define AVANZA_IZQ 11
#define RETROCEDE_DER 12
#define AVANZA_DER 13

// Constantes
const float sonido = 34300.0; // Velocidad del sonido en cm/s
const float distanciaStop = 10.0;    // distancia en Cm, donde el ultrasonido informa que paren los motores

void setup() {
  // Iniciamos el monitor serie
  Serial.begin(9600); // baudios con que emite

  // Modo entrada/salida de los pines
  pinMode(ECHO, INPUT);
  pinMode(TRIGGER, OUTPUT);
  pinMode(AVANZA_DER, OUTPUT);
  pinMode(RETROCEDE_DER, OUTPUT);
  pinMode(AVANZA_IZQ, OUTPUT);
  pinMode(RETROCEDE_IZQ, OUTPUT);
  
  // Arranca motor
  avanzar();
}

void loop() {
  // Preparamos el sensor de ultrasonidos
  iniciarTrigger();

  // Obtenemos la distancia
  float distancia = calcularDistancia();

  // Lanzamos alerta si estamos dentro del rango de peligro
  if (distancia <= distanciaStop) { 
    // PARAMOS MOTOR y reversa
    pararMotor();
    delay(3000);
    retroceder();
  }
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
  Serial.print(distancia);
  Serial.print("cm");
  Serial.println();
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
