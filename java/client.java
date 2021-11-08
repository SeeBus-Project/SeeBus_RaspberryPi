import java.io.*;
import java.net.Socket;



public class client {

    public static void main(String[] args ) {

        try {
            //서버 접속
            //ec2-3-35-208-56.ap-northeast-2.compute.amazonaws.com
            Socket socket = new Socket("127.0.0.1", 17532);
            while(true){
            //Server가 보낸 데이터 출력
            ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
            Object input = instream.readObject();
            String message = (String)input;
            System.out.println("Message : " + message );

            if(message.equals("on"))
            {
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());

                outstream.writeObject("이건 라즈베리파이의 신호(ON)입니다.");
                outstream.flush();
            }
            else {
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject("이건 라즈베리파이의 신호(OFF)입니다.");
                outstream.flush();
                }
            }

        }
        catch( Exception e ){
            e.printStackTrace();
        }



    }

}


