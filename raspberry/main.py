import socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((192.168.0.15), 2727)
data = sock.rec(16)
print(data)