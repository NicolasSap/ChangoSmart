/*
// Pines utilizados
#define LEDVERDE 2
#define LEDAMARILLO 3
#define LEDROJO 4
#define RETARDO 500

void setup() {
  // put your setup code here, to run once:
  // Iniciamos el monitor serie
  Serial.begin(9600);

  // Modo entrada/salida de los pines
  pinMode(LEDVERDE, OUTPUT);
  pinMode(LEDAMARILLO, OUTPUT);
  pinMode(LEDROJO, OUTPUT);

  // Apagamos todos los LEDs
  encenderLEDs();
}

void loop() {
}

void encenderLEDs() {
  // Apagamos todos los LEDs
  digitalWrite(LEDVERDE, HIGH);
  digitalWrite(LEDAMARILLO, HIGH);
  digitalWrite(LEDROJO, HIGH);
}
*/
