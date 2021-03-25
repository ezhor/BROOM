import subprocess
import socket
from SerialManager import SerialManager

print("Starting serial communication...")
serialManager = SerialManager()
print("Serial communication started successfully")

print("Starting control socket...")
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("192.168.0.15", 2727))
print("Control socket started successfully")

message = ""
while True:
    data = sock.recv(1)
    if data != "\n":
        message += data
    else:
        serialManager.sendLine(message)
        message = ""