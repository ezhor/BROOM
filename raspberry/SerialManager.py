import serial, time

class SerialManager():
    def __init__(self):        
        self.arduino = serial.Serial("/dev/serial", 9600)
        time.sleep(1)

    def send(self, data):
        self.arduino.write(data.encode())
        print("Sent:" + data);