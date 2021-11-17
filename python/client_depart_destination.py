#-*-coding:utf-8-*-


from socket import *
#import RPi.GPIO as GPIO
import time

clientSock = socket(AF_INET, SOCK_STREAM)

target = 'ec2-3-35-208-56.ap-northeast-2.compute.amazonaws.com'
target2 = '183.101.12.31'
port = 17532


#GPIO.setmode(GPIO.BCM)
#GPIO.setup(26,GPIO.OUT)
#GPIO.setup(20,GPIO.OUT)

clientSock.connect((target, port))
try:
    while True:
        #GPIO.output(20,False)
        #GPIO.output(26,False)

        #라즈베리파이 - 서버 연결 체크 
        data = clientSock.recv(4)
        length = int.from_bytes(data, "little")
        data = clientSock.recv(length)
        clientSock.sendall(length.to_bytes(4, byteorder="little"))
        clientSock.sendall(data)

       
        data = clientSock.recv(4)
        length = int.from_bytes(data, "little")
        data = clientSock.recv(length)
        msg = data.decode()
        
        if msg == "out":
            #GPIO.output(20,True)
            #time.sleep(1)
            
            length = len(data)
            clientSock.sendall(length.to_bytes(4, byteorder="little"))
            clientSock.sendall(data)


            

        elif msg == "in":
            
            #GPIO.output(26,True)
            #time.sleep(1)
            
            length = len(data)
            clientSock.sendall(length.to_bytes(4, byteorder="little"))
            clientSock.sendall(data)
            #출발지 데이터
            data = clientSock.recv(4)
            length = int.from_bytes(data, "little")
            data = clientSock.recv(length)
            departure = data.decode()
            length = len(data)
            clientSock.sendall(length.to_bytes(4, byteorder="little"))
            clientSock.sendall(data)
            #목적지 데이터
            data = clientSock.recv(4)
            length = int.from_bytes(data, "little")
            data = clientSock.recv(length)
            destination = data.decode()
            length = len(data)
            clientSock.sendall(length.to_bytes(4, byteorder="little"))
            clientSock.sendall(data)
            #출력
            
            
            
            
            
except:
    GPIO.cleanup()
