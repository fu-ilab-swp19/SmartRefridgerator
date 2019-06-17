/*
  Web client

 This sketch connects to a website (http://www.google.com)
 using an Arduino Wiznet Ethernet shield.

 Circuit:
 * Ethernet shield attached to pins 10, 11, 12, 13

 created 18 Dec 2009
 by David A. Mellis
 modified 9 Apr 2012
 by Tom Igoe, based on work by Adrian McEwen

 */
#include <SPI.h>
#include <Ethernet.h>

#include <HX711_ADC.h>

#include <Wire.h>
#include <BH1750.h>

#include <math.h>

int delayval;
// To Change poll intervall
// If no light is sensed -> check for light every 10s
// If light is present   -> check weight every second

/* NETWORKING STUFF */
// Enter a MAC address for your controller below.
// Newer Ethernet shields have a MAC address printed on a sticker on the shield
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

// if you don't want to use DNS (and reduce your sketch size)
// use the numeric IP instead of the name for the server:
IPAddress server(192,168,43,188);  // numeric IP for Google (no DNS)
//char server[] = "www.google.com";    // name address for Google (using DNS)

// Set the static IP address to use if the DHCP fails to assign
//IPAddress ip(192, 168, 178, 20);
//IPAddress myDns(192, 168, 178, 1);
IPAddress ip(192,168,43,1);
IPAddress myDns(192,168,43,188);


// Initialize the Ethernet client library
// with the IP address and port of the server
// that you want to connect to (port 80 is default for HTTP):
EthernetClient client;

// Variables to measure the speed
unsigned long beginMicros, endMicros;
unsigned long byteCount = 0;
bool printWebData = true;  // set to false for better speed measurement

/* LOAD CELL */
// (dt pin, sck pin)  --  (orange, red)
HX711_ADC LoadCellRO(4,5);
HX711_ADC LoadCellBW(5,6);  // black white power

HX711_ADC load_cells[2] = {LoadCellRO, LoadCellBW};

long t;
float current, last, last_sent;

/* LIGHT SENSOR */
BH1750 lightMeter;

float lux;

void setup() {
  delayval = 1000;
  
  // You can use Ethernet.init(pin) to configure the CS pin
  Ethernet.init(10);  // Most Arduino shields
  //Ethernet.init(5);   // MKR ETH shield
  //Ethernet.init(0);   // Teensy 2.0
  //Ethernet.init(20);  // Teensy++ 2.0
  //Ethernet.init(15);  // ESP8266 with Adafruit Featherwing Ethernet
  //Ethernet.init(33);  // ESP32 with Adafruit Featherwing Ethernet

  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // start the Ethernet connection:
  Serial.println("Initialize Ethernet with DHCP:");
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // Check for Ethernet hardware present
    if (Ethernet.hardwareStatus() == EthernetNoHardware) {
      Serial.println("Ethernet shield was not found.  Sorry, can't run without hardware. :(");
      while (true) {
        delay(1); // do nothing, no point running without Ethernet hardware
      }
    }
    if (Ethernet.linkStatus() == LinkOFF) {
      Serial.println("Ethernet cable is not connected.");
    }
    // try to congifure using IP address instead of DHCP:
    Ethernet.begin(mac, ip, myDns);
  } else {
    Serial.print("  DHCP assigned IP ");
    Serial.println(Ethernet.localIP());
  }

  /* LOAD CELL */

  float calibrationValue;
  calibrationValue = 391.86;

  Serial.println();
  Serial.println("Starting Loadcell...");
  LoadCellRO.begin();
  long stabilizingtime = 2000;
  LoadCellRO.start(stabilizingtime);

  if (LoadCell.getTareTimeoutFlag())
  {
    Serial.println("Tare timeout, check MCU>HX711 wiring and pin designation!");
  } 
  else 
  {
   LoadCell.setCalFactor(calibrationValue); // Calibration
   last = -10000;
   last_sent = -99;
   Serial.println("Startup + tare completed"); 
  }

  Wire.begin();
  lightMeter.begin();

  Serial.println("BH1750 initialized");
}

void loop() {
  lux = lightMeter.readLightLevel();
  Serial.print("Light: ");
  Serial.print(lux);
  Serial.println(" lx");

  if (lux > 5)
  {
    updateWeight();
    delayval = 500;
  } else {
    delayval = 10000;  
  }
  delay(delayval);
}

void updateWeight()
{
  LoadCellRO.update();

  // smoothing the values
  if (millis() > t + 1000)
  {
    current = LoadCellRO.getData();
    Serial.print("LoadCellRO measured value: ");
    Serial.print(current);
    Serial.println("g");
    t = millis();
    if ((round(current - last) == 0) && (abs(current - last_sent) > 5))
    {
      connectToServer();
      receiveFromServer();
      last_sent = current;
    }
  }
     //receive from serial terminal
  if (Serial.available() > 0) {
    float i;
    char inByte = Serial.read();
    if (inByte == 't') LoadCellRO.tareNoDelay();
  }
     //check if last tare operation is complete
  if (LoadCellRO.getTareStatus() == true) {
    Serial.println("Tare complete");
  }
  
  last = current;
}

void connectToServer()
{
  Serial.print("connecting to ");
  Serial.print(server);
  Serial.println("...");

  // if you get a connection, report back via serial:
  if (client.connect(server, 3000)) {
    Serial.print("connected to ");
    Serial.println(client.remoteIP());
    // Make a HTTP request:
    client.print("GET /api/addproduct/controller/1/");
    client.print(abs(current));
    client.println(" HTTP/1.1");
    //client.println("Host: ");
    client.println("Connection: close");
    client.println();
  } else {
    // if you didn't get a connection to the server:
    Serial.println("connection failed");
  }
  beginMicros = micros();
}

void receiveFromServer()
{
  // if there are incoming bytes available
  // from the server, read them and print them:
  int len = client.available();
  if (len > 0) {
    byte buffer[80];
    if (len > 80) len = 80;
    client.read(buffer, len);
    if (printWebData) {
      Serial.write(buffer, len); // show in the serial monitor (slows some boards)
    }
    byteCount = byteCount + len;
  }

  // if the server's disconnected, stop the client:
  if (!client.connected()) {
    endMicros = micros();
    Serial.println();
    Serial.println("disconnecting.");
    client.stop();
    Serial.print("Received ");
    Serial.print(byteCount);
    Serial.print(" bytes in ");
    float seconds = (float)(endMicros - beginMicros) / 1000000.0;
    Serial.print(seconds, 4);
    float rate = (float)byteCount / seconds / 1000.0;
    Serial.print(", rate = ");
    Serial.print(rate);
    Serial.print(" kbytes/second");
    Serial.println();

    // do nothing forevermore:
    while (true) {
      delay(1);
    }
  }
  delay(500);
}
