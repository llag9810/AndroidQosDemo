package com.example.yifanzhu.udpsendpackage.network;

import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ArrayBlockingQueue;

public class PackageUtil {

    private static final String TAG = "PackageUtil";

    private String ip;
    private int port;
    private boolean isStartSend = false;
    private boolean isStartReceive = false;
    private byte[] sendBuf = new byte[65535];
    private byte[] recvBuf = new byte[65535];
    ByteBuffer buffer = ByteBuffer.wrap(sendBuf).order(ByteOrder.nativeOrder());
    private volatile int seq = 1;
    DatagramSocket socket;
    ArrayBlockingQueue<DatagramPacket> queue = new ArrayBlockingQueue<>(1024);
    private volatile long duration;
    private int packetSize = 1024;
    private OnReceiveListener listener;

    public PackageUtil() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public PackageUtil(String ip, int port, long packetSendDuration) {
        this();
        this.ip = ip;
        this.port = port;
        this.duration = packetSendDuration;
    }

    public static PackageHeader parsePacketHeader(DatagramPacket packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet.getData()).order(ByteOrder.nativeOrder());
        buffer.position(0);
        PackageHeader header = new PackageHeader();
        header.setLength(buffer.getShort());
        header.setSeq(buffer.getShort());
        header.setFlag(buffer.get());
        header.setPacketData(buffer.get());
        header.setPacketFec(buffer.get());
        header.setPacketIdx(buffer.get());
        if ((header.getFlag() & PackageHeader.FLAG_VIDEO) != 0) {
            header.setFrameSerial(buffer.getInt());
        } else {
            header.setPacketRecv(buffer.getShort());
            header.setPacketSend(buffer.getShort());
        }

        return header;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setPacketSize(int size) {
        this.packetSize = size;
    }


    public void setOnReceiveListener(OnReceiveListener listener) {
        this.listener = listener;
    }

    public void startSend() {
        isStartSend = true;
        new SendThread().start();
    }

    public void stopSend() {
        isStartSend = false;
    }

    public void startReceive() {
        isStartReceive = true;
        new RecvThread().start();
    }


    public void stopReceive() {
        isStartReceive = false;
    }

    class SendThread extends Thread {
        private long start, end;
        private long latency = 0;
        @Override
        public void run() {
            try {
                ByteBuffer buffer = ByteBuffer.wrap(sendBuf).order(ByteOrder.nativeOrder());
                while (isStartSend) {
                    start = System.nanoTime();
                    //Log.d(TAG, "latency = " + latency);
                    //if (latency >= duration * 1e6) {
                        buffer.position(2);
                        buffer.putShort((short) seq++);
                        DatagramPacket packet = new DatagramPacket(sendBuf, packetSize, InetAddress.getByName(ip), port);
                        socket.send(packet);
                        // Log.d(TAG, "run: send success");

                    end = System.nanoTime();
                    long error = end - start > 0 ? (end - start) / 1000000 : 0;
                    // Log.d(TAG, "run: error = " + error);
                    try {
                        sleep(duration - error);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class RecvThread extends Thread {
        @Override
        public void run() {
            try {
                while (isStartReceive) {
                    DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                    socket.receive(packet);
                    listener.onReceive(PackageUtil.parsePacketHeader(packet), packet.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnReceiveListener {
        void onReceive(PackageHeader header, byte[] data);
    }
}
