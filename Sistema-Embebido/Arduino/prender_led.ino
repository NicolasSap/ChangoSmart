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
  apagarLEDs();
}

void loop() {
  // put your main code here, to run repeatedly:
  // Preparamos el sensor de ultrasonidos
  secuencia_leds1();
  delay(2000);
  // Apagamos todos los LEDs
  apagarLEDs();
  secuencia_ledsVAR();
  delay(2000);
  // Apagamos todos los LEDs
  apagarLEDs();
  secuencia_ledsRAV();
  delay(2000);
  // Apagamos todos los LEDs
  apagarLEDs();
  secuencia_leds2();
  delay(2000);
  apagarLEDs();
}

// Apaga todos los LEDs

void apagarLEDs() {
  // Apagamos todos los LEDs
  digitalWrite(LEDVERDE, LOW);
  digitalWrite(LEDAMARILLO, LOW);
  digitalWrite(LEDROJO, LOW);
}

//Función que comprueba si hay que lanzar alguna alerta visual

void secuencia_leds1() {
  // Encendemos el LED verde
  digitalWrite(LEDVERDE, HIGH);
  // Encendemos el LED amarillo
  digitalWrite(LEDAMARILLO, HIGH);
  // Encendemos el LED rojo
  digitalWrite(LEDROJO, HIGH);
}

void secuencia_ledsVAR() {
  // Encendemos el LED verde
  digitalWrite(LEDVERDE, HIGH);
  delay(RETARDO);
  // Encendemos el LED amarillo
  digitalWrite(LEDAMARILLO, HIGH);
  delay(RETARDO);
  // Encendemos el LED rojo
  digitalWrite(LEDROJO, HIGH);
}

void secuencia_ledsRAV() {
  // Encendemos el LED rojo
  digitalWrite(LEDROJO, HIGH);
  delay(RETARDO);
  // Encendemos el LED amarillo
  digitalWrite(LEDAMARILLO, HIGH);
  delay(RETARDO);
  // Encendemos el LED verde
  digitalWrite(LEDVERDE, HIGH);  
}

void secuencia_leds2() {
  // Encendemos el LED verde
  digitalWrite(LEDVERDE, HIGH);
  delay(RETARDO);
  digitalWrite(LEDVERDE, LOW);
  // Encendemos el LED amarillo
  digitalWrite(LEDAMARILLO, HIGH);
  delay(RETARDO);
  digitalWrite(LEDAMARILLO, LOW);
  // Encendemos el LED rojo
  digitalWrite(LEDROJO, HIGH);
  delay(RETARDO);
  digitalWrite(LEDROJO, LOW);
  // Encendemos el LED amarillo
  digitalWrite(LEDAMARILLO, HIGH);
  delay(500);
  digitalWrite(LEDAMARILLO, LOW);
  digitalWrite(LEDVERDE, HIGH);
  delay(RETARDO);
  // Encendemos el LED verde
  digitalWrite(LEDVERDE, LOW);
  // Encendemos el LED amarillo
  digitalWrite(LEDAMARILLO, HIGH);
  delay(RETARDO);
  digitalWrite(LEDAMARILLO, LOW);
  // Encendemos el LED rojo
  digitalWrite(LEDROJO, HIGH);
  delay(RETARDO);
  digitalWrite(LEDROJO, LOW);
  // Encendemos el LED amarillo
  digitalWrite(LEDAMARILLO, HIGH);
  delay(RETARDO);
  digitalWrite(LEDAMARILLO, LOW);
  digitalWrite(LEDVERDE, HIGH);
  delay(RETARDO);
  digitalWrite(LEDVERDE, LOW);
  
}
*/
