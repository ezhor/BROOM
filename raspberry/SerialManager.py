import serial, time

class SerialManager():
    def __init__(self):        
        self.arduino = serial.Serial("/dev/ttyUSB0", 9600)
        time.sleep(1)

    def sendLine(self, line):
        self.arduino.write((line + "\n").encode())
    
    def sendData(self, line):
        self.arduino.write(data)