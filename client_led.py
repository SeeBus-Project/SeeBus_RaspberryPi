#-*-coding:utf-8-*-

from socket import *
import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)
GPIO.setup(17,GPIO.OUT)
clientSock = socket(AF_INET, SOCK_STREAM)

target3 = '3.35.208.56'
target = 'ec2-3-35-208-56.ap-northeast-2.compute.amazonaws.com'
port = 5000

clientSock.connect((target, port))

print('접속 완료')

while True:
    GPIO.output(17,False)
    recvData = clientSock.recv(65535)
    print('상대방 :', recvData.decode('utf-8'))
    if(recvData.decode('utf-8') == 'stop'):
        GPIO.output(17,True)
        time.sleep(10)
        sendData = 'ok'
    else:
        sendData = 'no'
    clientSock.send(sendData.encode('utf-8'))
