/* 
 *  !ARDUINO ARM (32bit) BOARDS library notwendig
 *  
 *  
 *  SSID: Fridge
 *  Password: !qaz2wsx
 *  Admin Password: 123456
*/

// TODO: wenn keine Verbindung zum Server, haengt sich alles auf


#include <SPI.h>
#include <Ethernet.h>

#include <HX711_ADC.h>

#include <Wire.h>
#include <BH1750.h>

#include <math.h>

#include <TimedAction.h>

#define NUM_LOADCELLS 2
#define LIGHT_SENSOR_DELAY 1000

void updateLight();
void updateWeight(int);

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

/* SERVER ADDRESS HIER EINTRAGEN */
IPAddress server(192,168,43,190);  // numeric IP for Google (no DNS)
//char server[] = "www.google.com";    // name address for Google (using DNS)

// Set the static IP address to use if the DHCP fails to assign
IPAddress ip(192, 168, 178, 20);
IPAddress myDns(192, 168, 178, 1);

IPAddress ip(192, 168, 43, 189);
IPAddress myDns(192, 168, 43, 1);

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
//                        (gray, black)
HX711_ADC load_cell_1(4,5);
HX711_ADC load_cell_2(2,3);

HX711_ADC load_cells[NUM_LOADCELLS] = {load_cell_1, load_cell_2};


long t;
float current[NUM_LOADCELLS], last[NUM_LOADCELLS], last_sent[NUM_LOADCELLS];

/* LIGHT SENSOR */
BH1750 lightMeter;

float lux;

/* TIMED ACTION Pseudothread */
TimedAction timedLightMeter = TimedAction(LIGHT_SENSOR_DELAY, updateLight);

void setup() {
  delayval = 1000;
  delay(1000);
  // You can use Ethernet.init(pin) to configure the CS pin
  Ethernet.init(10);  // Most Arduino shields

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
  Serial.println("Starting Loadcells...");
  for (int i = 0; i < NUM_LOADCELLS; i++) {
    load_cells[i].begin();
    long stabilizingtime = 2000;
    load_cells[i].start(stabilizingtime);
    if (load_cells[i].getTareTimeoutFlag()) {
      Serial.println("Tare timeout, check MCU>HX711 wiring and pin designation!");
    } else {
      load_cells[i].setCalFactor(calibrationValue); // Calibration
      last[i] = -10000;
      last_sent[i] = -99;
      Serial.print("LoadCell ");
      Serial.print(i);
      Serial.println(" Startup + Tare completed"); 
    }

  }
  
  Wire.begin();
  lightMeter.begin();
  // to initialise lux because the light update in the main loop is delayed()
  lux = lightMeter.readLightLevel();

  Serial.println("BH1750 initialized");
}

void loop() 
{
  timedLightMeter.check();
  
    if (lux > 5)
    {
      for (int i = 0; i < NUM_LOADCELLS; i++) {
        updateWeight(i);
        delay(50);
      }
      delayval = 10;
    } else {
      delayval = 1600;  
    }
    delay(delayval);
}


void updateLight()
{
  lux = lightMeter.readLightLevel();
  Serial.print("Light: ");
  Serial.print(lux);
  Serial.println(" lx");
}

void updateWeight(int i)
{
  load_cells[i].update();

  // smoothing the values
  if (millis() > t + 1000)
  {
    current[i] = load_cells[i].getData();
    Serial.print("LoadCell ");
    Serial.print(i);
    Serial.print(" measured value: ");
    Serial.print(current[i]);
    Serial.println("g");
    t = millis();
    if ((round(current[i] - last[i]) == 0) && (abs(current[i] - last_sent[i]) > 5))
    {
      connectToServer(i);
      receiveFromServer();
      last_sent[i] = current[i];
    }
  }
     //receive from serial terminal
  if (Serial.available() > 0) {
    float i;
    char inByte = Serial.read();
    //if (inByte == 't') 
      //load_cells[i].tareNoDelay();
  }
     //check if last tare operation is complete
  if (load_cells[i].getTareStatus() == true) {
    Serial.println("Tare complete");
  }
  
  last[i] = current[i];
}

void connectToServer(int load_cell_id)
{
  Serial.print("connecting to ");
  Serial.print(server);
  Serial.println("...");

  // if you get a connection, report back via serial:
  if (client.connect(server, 3000)) {
    Serial.print("connected to ");
    Serial.println(client.remoteIP());
    // Make a HTTP request:
    client.print("GET /api/addproduct/controller/");
    client.print(load_cell_id + 1);
    client.print("/");
     // TODO Indizierung richtig machen
    client.print(abs(current[load_cell_id]));
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

void sendWeight()
{
  
}
