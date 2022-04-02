import subprocess
import socket
from SerialManager import SerialManager

HOST = ""
PORT = 2727
PACKET_LENGTH = 18

print("Starting serial communication...")
serialManager = SerialManager()
print("Serial communication started successfully")

print("Starting control socket...")
message = ""
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind((HOST, PORT))
print("Control socket started")
while True:
    try:    
        data, address = s.recvfrom(1024)
        print("Length: " + str(len(data)))
        if(len(data) == PACKET_LENGTH ):
            serialManager.sendLine(message)
    except Exception as e:
        print(e)
if(s != null):
    s.close();
