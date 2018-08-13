package com.example.yifanzhu.udpsendpackage.network;

import java.net.DatagramPacket;

public class PacketNetwork {
    public void setSendInterval(int ms) {
        // TODO: Set send interval between two packets.
    }

    public void addPacket(DatagramPacket packet) {
        // TODO: Add packet to queue.
    }

    public interface OnSendListener {
        void onSend(DatagramPacket packet);
    }

    public interface OnReceiveListener {
        void onReceive(DatagramPacket packet);
    }
}
