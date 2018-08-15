package com.example.yifanzhu.udpsendpackage.interfaces;

import java.net.DatagramPacket;

public interface IPacketNetwork{
    void sendPacket(DatagramPacket packet);
    void setSendInterval(int ms);
    void startLoop();
    void stopLoop();
}
