package com.example.yifanzhu.udpsendpackage.interfaces;

import com.example.yifanzhu.udpsendpackage.packet.PacketHeader;

import java.net.DatagramPacket;

public interface IPacketConsumer {
    void onSendPacket(DatagramPacket packet, PacketHeader header);
    void onReceivePacket(DatagramPacket packet, PacketHeader header);
}
