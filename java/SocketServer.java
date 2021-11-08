import java.io.*;
import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    public static void main(String[] args) {
        int androidPortNumber = 5000; // 안드로이드 - 서버
        int raspberryPortNumber = 17532; // 라즈베리파이 - 서버
        String msg;
        byte[] data;
        ByteBuffer b;
        while (true) {
            try {
                System.out.println("서버를 시작합니다...");
                System.out.println("라즈베리 포트 " + raspberryPortNumber + "에서 요청 대기중...");
                ServerSocket raspberryServerSocket = new ServerSocket(raspberryPortNumber);
                Socket raspberrySocket = raspberryServerSocket.accept();

                //클라이언트가 접근했을 때 accept() 메소드를 통해 클라이언트 소켓 객체 참조
                InetAddress rasperryHost = raspberrySocket.getLocalAddress();
                int rasperryPort = raspberrySocket.getPort();
                System.out.println("라즈베리파이 클라이언트 연결됨. 호스트 : " + rasperryHost + ", 포트 : " + rasperryPort);

                // 안드로이드 소켓 설정
                try {
                    while (true) {
                        byte[] byteArr = new byte[100];
                        ServerSocket androidServerSocket = new ServerSocket(androidPortNumber); //포트번호를 매개변수로 전달하면서 서버 소켓 열기
                        System.out.println("안드로이드 포트 " + androidPortNumber + "에서 요청 대기중...");
                        Socket androidSocket = androidServerSocket.accept();
                        InetAddress androidHost = androidSocket.getLocalAddress();
                        int androidPort = androidSocket.getPort();

                        System.out.println("안드로이드 클라이언트 연결됨. 호스트 : " + androidHost + ", 포트 : " + androidPort);
                        if (raspberryServerSocket.isBound() && !raspberryServerSocket.isClosed() == false) {
                            System.out.println("라즈베리파소켓 닫힘");
                            break;
                        }
                        OutputStream raspberrySender = raspberrySocket.getOutputStream();  // 라즈베리파이 데이터 전송
                        InputStream raspberryReceiver = raspberrySocket.getInputStream();  // 라즈베리파이 데이터 수신
                        ObjectOutputStream androidOutstream = new ObjectOutputStream(androidSocket.getOutputStream()); // 안드로이드 데이터 전송
                        ObjectInputStream instream = new ObjectInputStream(androidSocket.getInputStream()); // 안드로이드 데이터 수신


                        try {
                            String first = "good";
                            data = first.getBytes();
                            b = ByteBuffer.allocate(4);
                            b.order(ByteOrder.LITTLE_ENDIAN);
                            b.putInt(data.length);
                            raspberrySender.write(b.array(), 0, 4);
                            raspberrySender.write(data);
                            raspberrySocket.setSoTimeout(2000);
                            data = new byte[4];
                            raspberryReceiver.read(data, 0, 4);
                            b = ByteBuffer.wrap(data);
                            b.order(ByteOrder.LITTLE_ENDIAN);
                            int length1 = b.getInt();
                            data = new byte[length1];
                            raspberryReceiver.read(data, 0, length1);
                            String msg1 = new String(data, "UTF-8");
                            System.out.println("라즈베리파이 클라이언트로부터 받은 데이터 : " + msg1);
                        }
                        catch(Exception e){
                            androidSocket.close();
                            androidServerSocket.close();
                        }

                        Object input = instream.readObject();
                        String androidData = (String) input;

                        System.out.println("안드로이드 클라이언트로부터 받은 데이터 : " + androidData); // 안드로이드 신호 서버에 출력

                        if ("on".equals(androidData) == true) {
                            System.out.println("정답");
                            msg = "on";
                            data = msg.getBytes();
                            b = ByteBuffer.allocate(4);
                            b.order(ByteOrder.LITTLE_ENDIAN);
                            b.putInt(data.length);
                            raspberrySender.write(b.array(), 0, 4);
                            raspberrySender.write(data);

                        } else {
                            System.out.println("다름");
                            msg = "off";
                            data = msg.getBytes();
                            b = ByteBuffer.allocate(4);
                            b.order(ByteOrder.LITTLE_ENDIAN);
                            b.putInt(data.length);
                            raspberrySender.write(b.array(), 0, 4);
                            raspberrySender.write(data);

                        }

                        data = new byte[4];
                        raspberryReceiver.read(data, 0, 4);
                        b = ByteBuffer.wrap(data);
                        b.order(ByteOrder.LITTLE_ENDIAN);
                        int length = b.getInt();
                        data = new byte[length];
                        raspberryReceiver.read(data, 0, length);
                        msg = new String(data, "UTF-8");
                        System.out.println("라즈베리파이 클라이언트로부터 받은 데이터 : " + msg);
                        //소켓의 출력 스트림 객체 참조
                        androidOutstream.writeObject((Object) msg); //출력 스트림에 응답 넣기
                        androidOutstream.flush(); // 출력
                        androidSocket.close();
                        androidServerSocket.close();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    raspberryServerSocket.close();
                    raspberrySocket.close();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}




