package com.airing.io.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 9001);
            client.setSendBufferSize(20);
            client.setTcpNoDelay(true);

            OutputStream out = client.getOutputStream();
            InputStream in = System.in;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            while (true) {
                String line = reader.readLine();
                line = line.concat("\n");
                if (line != null) {
                    byte[] data = line.getBytes();
                    for (byte b : data) {
                        out.write(b);
                    }
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
