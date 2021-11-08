#-*-coding:utf-8-*-


from socket import *
import RPi.GPIO as GPIO
import time

clientSock = socket(AF_INET, SOCK_STREAM)

target = 'ec2-3-35-208-56.ap-northeast-2.compute.amazonaws.com'
target2 = '183.101.12.31'
port = 17532

GPIO.setmode(GPIO.BCM)
GPIO.setup(26,GPIO.OUT)
GPIO.setup(20,GPIO.OUT)

clientSock.connect((target, port))
try:
    while True:
        GPIO.output(20,False)
        GPIO.output(26,False)
        
        data = clientSock.recv(4)
        length = int.from_bytes(data, "little")
        data = clientSock.recv(length)
        clientSock.sendall(length.to_bytes(4, byteorder="little"))
        clientSock.sendall(data)

       
        data = clientSock.recv(4)
        length = int.from_bytes(data, "little")
        data = clientSock.recv(length)
        
        msg = data.decode()
        if msg == "on":
            GPIO.output(20,True)
            time.sleep(1)
            print(msg)
            data = msg.encode()
            length = len(data)
            clientSock.sendall(length.to_bytes(4, byteorder="little"))
            clientSock.sendall(data)

        elif msg == "off":
            print(msg)
            GPIO.output(26,True)
            time.sleep(1)
            data = msg.encode()
            length = len(data)
            clientSock.sendall(length.to_bytes(4, byteorder="little"))
            clientSock.sendall(data)
except:
    GPIO.cleanup()
