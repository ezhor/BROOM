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
    try:    
        connection, addr = s.accept()
        print("Client connected: ", addr)
        connected = True
        data = ""
        while connected:
            data = connection.recv(1)
            if data != "":
                if data != "\n":
                    message += data
                else:
                    serialManager.sendLine(message)
                    message = ""
            else:
                connected = False
    except e:
        print("Exception:", e)
if(s != null):
    s.close();
