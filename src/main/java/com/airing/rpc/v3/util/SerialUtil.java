package com.airing.rpc.v3.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerialUtil {

    public static byte[] objToByte(Object obj) {
        ByteArrayOutputStream out = null;
        ObjectOutputStream objOut = null;
        try {
            out = new ByteArrayOutputStream();
            objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objOut != null) {
                try {
                    objOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
