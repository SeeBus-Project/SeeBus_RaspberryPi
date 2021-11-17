package com.example.seebus_stopbell;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.SocketHandler;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    String response;
    EditText pt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pt = findViewById(R.id.pt);
    }

    ClientThread thread;


    public void clickStartButton(View v) {
        thread = new ClientThread();


        thread.signal = "server on";
        String data = pt.getText().toString();
        try{
            thread.getPort = Integer.parseInt(data);
        }catch(NumberFormatException e)
        {
            thread.getPort = 9999;
        }
        Toast.makeText(this, "port("+data + "): server on", Toast.LENGTH_LONG).show();
        thread.start();
    }

    public void clickOnButton(View v) {
        ClientThread thread = new ClientThread();
        thread.signal = "on";
        String data = pt.getText().toString();
        try{
            thread.getPort = Integer.parseInt(data);
        }catch(NumberFormatException e)
        {
            thread.getPort = 9999;
        }
        thread.start();
        Toast.makeText(this, "port("+data + "): LED on" , Toast.LENGTH_LONG).show();
    }

    public void clickOffButton(View v) {
        thread = new ClientThread();
        thread.signal = "off";
        String data = pt.getText().toString();
        try{
            thread.getPort = Integer.parseInt(data);
        }catch(NumberFormatException e)
        {
            thread.getPort = 9999;
        }
        thread.start();
        Toast.makeText(this, "port("+data+"): LED off", Toast.LENGTH_LONG).show();
    }

    class ClientThread extends Thread {
        String signal;
        int getPort;
        @Override
        public void run() {
            String host2 = "183.101.12.31";
            String host = "ec2-3-35-208-56.ap-northeast-2.compute.amazonaws.com";
            try {
                Socket socket = new Socket(host, getPort);
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream()); //소켓의 출력 스트림 참조
                outstream.writeObject(signal); // 출력 스트림에 데이터 넣기
                outstream.flush(); // 출력


                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
                response = (String)instream.readObject();
                //Log.d("ClientThread", "Received data: " + response);

                outstream.close();
                instream.close();
                socket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}