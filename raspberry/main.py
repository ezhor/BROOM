import subprocess
import socket
from SerialManager import SerialManager

HOST = ""
PORT = 2727

print("Starting serial communication...")
serialManager = SerialManager()
print("Serial communication started successfully")

print("Starting control socket...")
message = ""
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)
while True:    
    connection, addr = s.accept()
    connected = True
    with connection:
        print("Connected by ", addr)
        while connected:
            data = connection.recv(1)
            if isinstance(data, str):
                print(type(data))
                if data != "\n":
                    message += data
                else:
                    serialManager.sendLine(message)
                    message = ""
            else:
                connected = False
