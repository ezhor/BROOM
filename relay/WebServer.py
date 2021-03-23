from http.server import BaseHTTPRequestHandler, HTTPServer
from FileManager import FileManager

class WebServer(BaseHTTPRequestHandler):
    fileManager = FileManager()

    def do_GET(self):
        ip = self.fileManager.readIp()
        self.protocol_version = "HTTP/1.1"
        self.send_response(200)
        self.send_header("Content-Length", len(ip))
        self.end_headers()        
        self.wfile.write(bytes(ip, "utf-8"))

    def do_POST(self):
        self.protocol_version = "HTTP/1.1"
        self.send_response(200)
        self.send_header("Content-Length", 0)
        self.end_headers()
        self.fileManager.saveIp(self.client_address[0])

def run():
    server = ('', 80)
    httpd = HTTPServer(server, WebServer)
    httpd.serve_forever()
run()
