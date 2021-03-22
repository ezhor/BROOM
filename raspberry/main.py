import subprocess
import socket

print("Starting video streaming...")

ps = subprocess.Popen("/opt/vc/bin/raspivid -t 0 -w 1280 -h 720 -hf -fps 20 -o -".split(), stdout=subprocess.PIPE)
output = subprocess.check_output("nc -u 192.168.0.15 2222".split(), stdin=ps.stdout)

print("Video streaming started successfully")
print("Starting control socket streaming...")

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("192.168.0.15", 2727))

print("Control socket started successfully")

message = ""
while True:
    data = sock.recv(1)
    if data != "\n":
        message += data
    else:
        message = ""