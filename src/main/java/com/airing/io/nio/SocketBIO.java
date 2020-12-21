package com.airing.io.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketBIO {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9001, 20);
        System.out.println("server bind to 9001");
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("accept client");
            new Thread(() -> {
                InputStream in = null;
                try {
                    in = client.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            System.out.println(readLine);
                        } else {
                            client.close();
                            break;
                        }
                    }
                    System.out.println("client close");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

}
