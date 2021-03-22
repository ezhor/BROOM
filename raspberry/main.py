import subprocess
import socket

cameraCommand = "/opt/vc/bin/raspivid -t 0 -w 300 -h 300 -hf -fps 20 -o - | nc -u 192.168.0.15 2222"
subprocess.Popen(cameraCommand.split(), stdout=subprocess.PIPE)

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("192.168.0.15", 2727))
message = ""
while True:
    data = sock.recv(1)
    if data != "\n":
        message += data
    else:
        print(message)
        message = ""