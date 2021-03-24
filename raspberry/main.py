import subprocess
import socket

print("Starting control socket streaming...")
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("192.168.0.28", 2727))
print("Control socket started successfully")

message = ""
while True:
    data = sock.recv(1)
    if data != "\n":
        message += data
    else:
        message = ""