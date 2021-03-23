import os

class FileManager:
    filePath = os.path.dirname(__file__) + "/files/ip"

    def saveIp(self, ip):
        self.removeFileIfExists(self.filePath)
        file = open(self.filePath, "x")
        file.write(ip)
        file.close()

    def readIp(self):
        file = open(self.filePath, "r")
        ip = file.readlines()[0]
        file.close()
        return ip

    def removeFileIfExists(self, filePath):
        if os.path.exists(filePath):
            os.remove(filePath)
