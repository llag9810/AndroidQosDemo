package com.example.yifanzhu.udpsendpackage.data;

import com.example.yifanzhu.udpsendpackage.interfaces.IPacketConsumer;
import com.example.yifanzhu.udpsendpackage.network.PacketNetwork;
import com.example.yifanzhu.udpsendpackage.packet.PacketUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class PacketReceiveThread extends Thread {
    private PacketNetwork mPacketNetwork;
    private DatagramSocket mSocket;

    public PacketReceiveThread(PacketNetwork packetNetwork) {
        this.mPacketNetwork = packetNetwork;
        mSocket = mPacketNetwork.getSocket();
    }

    @Override
    public void run() {
        while (mPacketNetwork.isStartLooping()) {
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                mSocket.receive(packet);
                IPacketConsumer consumer = mPacketNetwork.getPacketConsumer();
                if (consumer != null) {
                    consumer.onReceivePacket(packet, PacketUtil.parseHeader(packet));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
