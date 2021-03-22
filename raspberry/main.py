import socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(("192.168.0.15", 2727))
while True:
    data = sock.recv(1)
    print(data)