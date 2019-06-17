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


#define NUM_LOADCELLS 2         // num of connected loadcells
#define LIGHT_SENSOR_DELAY 1000 // default interval for light sensor (in ms)
#define INBETWEEN_WEIGHT_SENSOR_DELAY 50  // default interval between each weight sensor (in ms)

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

/* SERVER ADDRESS HIER EINTRAGEN */
IPAddress server(192,168,43,190);  // address of npm server(/host device)
int port = 3000;                 

// Set the static IP address to use if the DHCP fails to assign
IPAddress ip(192, 168, 178, 20);
IPAddress myDns(192, 168, 178, 1); // default values for my FritzBox

//IPAddress ip(192, 168, 43, 189);
//IPAddress myDns(192, 168, 43, 1); // default values for Wessams Laptop

EthernetClient client;

// Variables to measure the throughput (ethernet)
// TODO: DELETE THIS?
unsigned long beginMicros, endMicros;
unsigned long byteCount = 0;
bool printWebData = true;  // set to false for better speed measurement

/* LOAD CELL */
// (dt pin, sck pin)  
// (4     , 5      ) --  (orange, red)
// (2     , 3      ) --  (gray, white) ? not sure, please check
HX711_ADC load_cell_1(4,5);
HX711_ADC load_cell_2(2,3);

HX711_ADC load_cells[NUM_LOADCELLS] = {load_cell_1, load_cell_2};

// time in ms to smooth the value, may want to delete this
long t;
float current[NUM_LOADCELLS], last[NUM_LOADCELLS], last_sent[NUM_LOADCELLS];

/* LIGHT SENSOR */
BH1750 lightMeter;

float lux;

/* TIMED ACTION Pseudothread */
// Allows light to be read with a different delay than the weight sensor
// Otherwise (for unknown reasons) the lightMeter would read the light
// 3 times in between each weight-read (although it was sequential)
TimedAction timedLightMeter = TimedAction(LIGHT_SENSOR_DELAY, updateLight);


// main setup function -- initialisation of objects and variables
void setup() {
  delayval = 1000;
  delay(delayval);

  // Used to configure the Ethernet-Shield Pin
  Ethernet.init(10);  

  // read serial port in interval of 9600 baud
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // TODO: MAYBE DEFINE THIS AS EXTRA FUNCTION?
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
  // calibration value may be gotten from the calibration program found unter
  // file > examples > HX711_ADC > calibration
  float calibrationValue;
  calibrationValue = 391.86;

  // TODO: MAYBE DEFINE THIS AS EXTRA FUNCTION?
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
  
  // initialise lightMeter
  Wire.begin();
  lightMeter.begin();
  lux = lightMeter.readLightLevel();

  Serial.println("BH1750 initialized");
}

void loop() 
{
  // TODO: mixed up the delay values while testing, cleanup would be great
  timedLightMeter.check();
  
    if (lux > 5)
    {
      for (int i = 0; i < NUM_LOADCELLS; i++) {
        updateWeight(i);
        delay(INBETWEEN_WEIGHT_SENSOR_DELAY);
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
  // TODO: DELETE THIS ? (smoothing)
  if (millis() > t + 1000)
  {
    current[i] = load_cells[i].getData();
    Serial.print("LoadCell ");
    Serial.print(i);
    Serial.print(" measured value: ");
    Serial.print(current[i]);
    Serial.println("g");
    t = millis();
    
    // compare currently read value with last read value
    // as soon as the value converges and was not sent already > send to server
    if ((round(current[i] - last[i]) == 0) && (abs(current[i] - last_sent[i]) > 5))
    {
      connectToServer(i);
      // TODO: DELETE receiveFromServer()? copy paste function
      receiveFromServer();
      last_sent[i] = current[i];
    }
  }
     //receive from serial terminal
  if (Serial.available() > 0) {
    float i;
    char inByte = Serial.read();
    // TODO: disabled this for testing, enabling it should be fine
    //if (inByte == 't') 
      //load_cells[i].tareNoDelay();
  }
     //check if last tare operation is complete
  if (load_cells[i].getTareStatus() == true) {
    Serial.println("Tare complete");
  }
  
  // set last to current, for next iteration
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
    client.print(abs(current[load_cell_id]));
    // TODO: change to HTTP/2?
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
