from socket import *

clientSock = socket(AF_INET, SOCK_STREAM)

target3 = '3.35.208.56'
target = 'ec2-3-35-208-56.ap-northeast-2.compute.amazonaws.com'
port = 5000

clientSock.connect((target, port))

print('접속 완료')

while True:
    recvData = clientSock.recv(65535)
    print('상대방 :', recvData.decode('utf-8'))
    if(recvData.decode('utf-8') == 'stop'):
        sendData = 'ok'
    else:
        sendData = 'no'
    clientSock.send(sendData.encode('utf-8'))
