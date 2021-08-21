#include <Servo.h>

Servo motorPWM;
Servo steeringServo;
Servo cameraRotationXServo;
Servo cameraRotationYServo;

byte motorPower;
byte steering;
byte cameraRotationX;
byte cameraRotationY;
bool led;
byte data[5];

void setup() {
  motorPWM.attach(3);
  steeringServo.attach(5);
  cameraRotationXServo.attach(6);
  cameraRotationYServo.attach(9);
  

  motorPWM.writeMicroseconds(1500);
  steeringServo.write(90);
  cameraRotationXServo.write(90);
  cameraRotationYServo.write(90);
  
  Serial.begin(9600);
  while (!Serial) {
    ;
  }
}

void loop() {
  if (Serial.available()) {
    Serial.readBytesUntil('\n', data, 5);
    parseData();
    sendPWM();
  }
}

void parseData() {
  motorPower = data[0];
  steering = data[0];
  cameraRotationX = data[0];
  cameraRotationY = data[0];  
  led = data[0] == 1;
}

void sendPWM() {
  motorPWM.writeMicroseconds(map(motorPower, -100, 100, 1000, 2000));
  steeringServo.write(map(steering, -100, 100, 180, 0));
  cameraRotationXServo.write(map(cameraRotationX, -100, 100, 180, 0));
  cameraRotationYServo.write(map(cameraRotationY, -100, 100, 180, 0));
  digitalWrite(12, led ? HIGH : LOW);
}
