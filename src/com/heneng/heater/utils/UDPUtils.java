package com.heneng.heater.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Administrator on 2015/10/27.
 */
public class UDPUtils {
    public static class UDPListener {
        public void onCompleted(String msg) {
        }

        public void onStart() {
        }
    }

    public static void listenPort(int port, UDPListener listener) {

        DatagramSocket ds = null;
        DatagramPacket dp = null;
        byte[] buf = new byte[1024];//存储发来的消息
        try {
            //绑定端口的
            ds = new DatagramSocket(port);
            dp = new DatagramPacket(buf, buf.length);
            if (listener != null) {
                listener.onStart();
            }
            ds.receive(dp);
            ds.close();
            ds.disconnect();
            String resultMsg = new String(dp.getData()).trim();
            if (listener != null) {
                listener.onCompleted(resultMsg);
            }
//            System.out.println("收到广播消息：" + sbuf.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void sendBrocast(String host, int port, String msg) {
        try {
            InetAddress adds = InetAddress.getByName(host);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(msg.getBytes(),
                    msg.length(), adds, port);
            ds.send(dp);
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
