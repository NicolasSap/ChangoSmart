/*
float temperatura = 0; //variable para la temperatura
 
void setup(){

 Serial.begin (9600); //inicia comunicacion serial
}
 
void loop(){
//Calcula la temperatura usando como referencia 5v
temperatura = (5.0 * analogRead(0)*100.0)/1023.0;
Serial.println (temperatura); //escribe la temperatura en el serial
delay (500); //espera 3 segundos para la siguiente medicion
}
*/
