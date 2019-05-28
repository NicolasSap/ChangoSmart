/*
// Pines utilizados
#define AVANZA_DER 13
#define RETROCEDE_DER 12
#define RETROCEDE_IZQ 10
#define AVANZA_IZQ 11

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  
  pinMode(AVANZA_DER, OUTPUT);
  pinMode(RETROCEDE_DER, OUTPUT);
  pinMode(AVANZA_IZQ, OUTPUT);
  pinMode(RETROCEDE_IZQ, OUTPUT);
  digitalWrite(AVANZA_DER,LOW);
  digitalWrite(RETROCEDE_DER,LOW);
  digitalWrite(AVANZA_IZQ,LOW);
  digitalWrite(RETROCEDE_IZQ,LOW);
}

void loop() {
  avanzar();
  delay(3000);
  retroceder();
  delay(3000);
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
*/
