import subprocess
import socket

ps = subprocess.Popen("/opt/vc/bin/raspivid -t 0 -w 1280 -h 720 -hf -fps 20 -o -".split(), stdout=subprocess.PIPE)
output = subprocess.check_output("nc -u 192.168.0.15 2222".split(), stdin=ps.stdout)

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("192.168.0.15", 2727))
message = ""
while True:
    data = sock.recv(1)
    if data != "\n":
        message += data
    else:
        message = ""