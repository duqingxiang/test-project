package com.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerTest {

    public static void main(String[] args) throws Exception {
        // 监听指定的端口
        int port = 55533;
        ServerSocket server = new ServerSocket(port);


        Socket socket = server.accept();
        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取


        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg;
        while ((msg = br.readLine()) != null) {

            System.out.println("get message from client: " + msg);
        }

//        br.close();
//        socket.close();
//        server.close();
    }

}
