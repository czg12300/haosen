package com.heneng.heater.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2015/11/11.
 */
public class TCPUtils {
    public static void sendMsg(String ip, int port, byte[] msg) {

        Socket socket = null;
        try {
            // 产生socket对象，制定服务器地址和服务器监听的端口号
            socket = new Socket(ip, port);
            // 从标准输入（键盘）读取内容，获取socket的输出流，将读取到的内容放入输出流中
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
