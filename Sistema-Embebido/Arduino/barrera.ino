/*
// Pines utilizados
#define LEDVERDE 7
#define BARRERA1 8

void setup() {
  // put your setup code here, to run once:
  // Iniciamos el monitor serie
  Serial.begin(9600);

  // Modo entrada/salida de los pines
  pinMode(LEDVERDE, OUTPUT);
  pinMode(BARRERA1, INPUT);

  // Apagar led por las dudas
  digitalWrite(LEDVERDE, LOW);
}

void loop() {
  int valor = digitalRead(BARRERA1);
  Serial.print(valor);
  if(valor == 1) {
    digitalWrite(LEDVERDE, LOW);
  } else {
    digitalWrite(LEDVERDE, HIGH);
  }
  delay(500);
}
*/
