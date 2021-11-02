#-*- coding: utf-8 -*-
from socket import *

serverSock  = socket(AF_INET,SOCK_STREAM)
target = 'ec2-3-35-208-56.ap-northeast-2.compute.amazonaws.com'
port = 5000

serverSock.bind((target,port))

serverSock.listen(1)

print('%d번 포트로 접속 대기중...'%port)

connectionSock, addr = serverSock.accept()

print(str(addr), '에서 접속되었습니다.')

while True:
    sendData = input('>>>')
    connectionSock.send(sendData.encode('utf-8'))

    recvData = connectionSock.recv(65535)
    print('상대방 :', recvData.decode('utf-8'))
