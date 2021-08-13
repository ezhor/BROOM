#include <Servo.h>

Servo motorPWM;
Servo steeringServo;
Servo cameraRotationXServo;
Servo cameraRotationYServo;

int motorPower;
int steering;
int cameraRotationX;
int cameraRotationY;
bool led;

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
    parseData(Serial.readStringUntil('\n'));
    sendPWM();
  }
}

void parseData(String data) {
  motorPower = data.substring(0, 4).toInt();
  steering = data.substring(4, 8).toInt();
  cameraRotationX = data.substring(8, 12).toInt();
  cameraRotationY = data.substring(12, 16).toInt();  
  led = data.substring(16, 17) == "1";
}

void sendPWM() {
  motorPWM.writeMicroseconds(map(motorPower, -100, 100, 1000, 2000));
  steeringServo.write(map(steering, 0, 180, 135, 45));
  cameraRotationXServo.write(map(cameraRotationX, 0, 180, 180, 0));
  cameraRotationYServo.write(map(cameraRotationY, 0, 180, 180, 0));
  digitalWrite(12, led ? HIGH : LOW);
}
