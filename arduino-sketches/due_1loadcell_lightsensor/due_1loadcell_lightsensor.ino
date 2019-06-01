//-------------------------------------------------------------------------------------
// HX711_ADC.h
// Arduino master library for HX711 24-Bit Analog-to-Digital Converter for Weigh Scales
// Olav Kallhovd sept2017
// Tested with      : HX711 asian module on channel A and YZC-133 3kg load cell
// Tested with MCU  : Arduino Nano, ESP8266
//-------------------------------------------------------------------------------------
// This is an example sketch on how to use this library
// Settling time (number of samples) and data filtering can be adjusted in the config.h file

#include <HX711_ADC.h>
#include <Wire.h>
#include <BH1750.h>

//HX711 constructor (dout pin, sck pin):
HX711_ADC LoadCellRO(4,5); // red orange power

//BH1750 LightMeter Contructor
// 5V = VCC
// GND= GND, ADDR
// A4 = SDA
// A5 = SDL
BH1750 lightMeter;

long t;

void setup() {
  
  float calValue; // calibration value
  calValue = 696.0; // uncomment this if you want to set this value in the sketch 
  
  Serial.begin(9600); delay(10);
  Wire.begin();
  Serial.println();
  Serial.println("Starting...");
  LoadCellRO.begin();
  lightMeter.begin();
  long stabilisingtime = 2000; // tare preciscion can be improved by adding a few seconds of stabilising time
  LoadCellRO.start(stabilisingtime);
  if(LoadCellRO.getTareTimeoutFlag()) {
    Serial.println("Tare timeout, check MCU>HX711 wiring and pin designations -- RO");
  }
  else {
    LoadCellRO.setCalFactor(calValue); // set calibration value (float)
    Serial.println("Startup + tare is complete");
  }
}

void loop() {
  //update() should be called at least as often as HX711 sample rate; >10Hz@10SPS, >80Hz@80SPS
  //use of delay in sketch will reduce effective sample rate (be carefull with use of delay() in the loop)
  LoadCellRO.update();
  float lux = lightMeter.readLightLevel();

  //get smoothed value from data set
  if (lux > 5){
    float i = LoadCellRO.getData();
    Serial.print("Load_cell RO output val: ");
    Serial.print(i);
    Serial.print("\tLux: ");
    Serial.println(lux);
  }

  //receive from serial terminal
  if (Serial.available() > 0) {
    float i;
    char inByte = Serial.read();
    if (inByte == 't') {
      LoadCellRO.tareNoDelay();
    }
  }

  //check if last tare operation is complete
  if (LoadCellRO.getTareStatus() == true) {
    Serial.println("Tare complete");
  }

  delay(500);

}
