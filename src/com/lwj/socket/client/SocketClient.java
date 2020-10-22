package com.lwj.socket.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 客户端
 * @author lwj
 */
public class SocketClient extends Socket{

//    private static String SERVER_IP = "192.168.0.103";
    private static String SERVER_IP = "localhost";
    private static String SERVER_PORT = "9804";
    private Socket client;
    private FileInputStream fis;
    private DataOutputStream dos;
    private static String performance = "1,2,3";
    private static String timeout = "60000";
    private static String size = "1024";
    private static String tcpNoDelay = "true";
    private static String filepath = "D:\\ProgramFiles\\百度云盘\\下载文件\\datagrip2020pj_167078\\datagrip-2020.1.exe";


    public SocketClient() throws UnknownHostException, IOException {
        super(SERVER_IP, Integer.parseInt(SERVER_PORT));
        this.client = this;
        boolean flag = true;
        if("false".equalsIgnoreCase(tcpNoDelay)) {
            flag = false;
        }
        this.client.setTcpNoDelay(flag);
        this.client.setSendBufferSize(Integer.parseInt(size));
        this.client.setSoTimeout(Integer.parseInt(timeout));
        String[] split = performance.split(",");
        this.client.setPerformancePreferences(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        System.out.println("客户端成功连接服务器IP："+SERVER_IP+"\t端口号："+SERVER_PORT);
    }

    private void sendFile() {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                System.out.println("文件不存在："+file);
            } else {

                fis = new FileInputStream(file);
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF(file.getName());
                dos.flush();
                dos.writeLong(file.length());
                dos.flush();
                System.out.println("========开始传输文件=======");
                long start = System.currentTimeMillis();
                byte[] bytes = new byte[1024];
                int length = 0;
//				long progress = 0;
                while((length = fis.read(bytes,0,bytes.length))!=-1){
                    dos.write(bytes,0,length);
                    dos.flush();
//					progress+=length;
//					System.out.println("======= 已传输 "+(100*progress/file.length())+"% =======");
                }
                long end = System.currentTimeMillis();
                System.out.println();
                System.out.println("=======文件传输成功=======");
                System.out.println("=======文件传输时间："+((end-start)/1000)+"."+((end-start)%1000)+"s"+"=======");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos!=null) {
                    dos.close();
                }
                if (fis!=null) {
                    fis.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args!=null) {
            if (args.length>0) {
                SERVER_IP = args[0];
            }
            if (args.length>1) {
                SERVER_PORT = args[1];
            }
            if (args.length>2) {
                filepath = args[2];
            }
            if (args.length>3) {
                tcpNoDelay = args[3];
            }
            if (args.length>4) {
                performance = args[4];
            }
            if (args.length > 5) {
                timeout = args[5];
            }
            if (args.length > 6) {
                size = args[6];
            }
        }
        SocketClient cSocketClient = null;
        try {
            cSocketClient = new SocketClient();
            cSocketClient.sendFile();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cSocketClient != null) {
                try {
                    cSocketClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}