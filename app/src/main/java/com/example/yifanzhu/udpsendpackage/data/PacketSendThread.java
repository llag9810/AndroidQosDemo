package com.example.yifanzhu.udpsendpackage.data;

import com.example.yifanzhu.udpsendpackage.interfaces.IPacketConsumer;
import com.example.yifanzhu.udpsendpackage.network.PacketNetwork;
import com.example.yifanzhu.udpsendpackage.packet.PacketUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class PacketSendThread extends Thread {
    private PacketNetwork mPacketNetwork;
    private DatagramSocket mSocket;

    public PacketSendThread(PacketNetwork packetNetwork) {
        this.mPacketNetwork = packetNetwork;
        mSocket = mPacketNetwork.getSocket();
    }

    @Override
    public void run() {
        long interval;
        long start, end;
        while (mPacketNetwork.isStartLooping()) {
            try {
                start = System.nanoTime();
                DatagramPacket packet = mPacketNetwork.getPacketFromQueue();
                mSocket.send(packet);

                IPacketConsumer consumer = mPacketNetwork.getPacketConsumer();
                if (consumer != null) {
                    consumer.onSendPacket(packet, PacketUtil.parseHeader(packet));
                }

                end = System.nanoTime();
                interval = mPacketNetwork.getSendInterval() - (end - start) / 1000000;
                if (interval < 0) {
                    interval = 0;
                }
                sleep(interval);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
