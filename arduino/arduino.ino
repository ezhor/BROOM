#include <Servo.h>

Servo motorPWM;
Servo steeringServo;
Servo cameraRotationXServo;
Servo cameraRotationYServo;

int motorPower;
int steering;
int cameraRotationX;
int cameraRotationY;

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
}

void sendPWM() {
  motorPWM.writeMicroseconds(1500 + 5.0 * motorPower);
  steeringServo.write(steering);
  cameraRotationXServo.write(cameraRotationX);
  cameraRotationYServo.write(cameraRotationY);
}
