import serial, time

class SerialManager():
    def __init__(self):        
        self.arduino = serial.Serial("/dev/ttyAMA0", 9600)
        time.sleep(1)

    def sendLine(self, line):
        self.arduino.write((line + "\n").encode())