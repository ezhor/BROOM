import serial, time

class SerialManager():
    def __init__(self):        
        self.arduino = serial.Serial("/dev/ttyUSB0", 9600)
        time.sleep(1)

    def sendLine(self, line):
        print("Line send: " + line[0:4] + "|" + line[4:8] + "|"+ line[8:12] + "|"+ line[12:16] + "|"+ line[16:17])
        self.arduino.write((line + "\n").encode())