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
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    while True:    
        conn, addr = s.accept()
        with conn:
            print("Connected by ", addr)
            while True:
                data = conn.recv(1)
                if data != "\n":
                    message += data
                else:
                    serialManager.sendLine(message)
                    message = ""