package com.example.yifanzhu.udpsendpackage.data;

import com.example.yifanzhu.udpsendpackage.interfaces.IPacketConsumer;
import com.example.yifanzhu.udpsendpackage.interfaces.IPacketNetwork;
import com.example.yifanzhu.udpsendpackage.packet.PacketHeader;;

import java.net.DatagramPacket;

public class QosServerThread extends Thread implements IPacketConsumer {
    private static final String TAG = "QosServerThread";
    private IPacketNetwork network = null;

    // for packet generation
    private int packetSize = 1024;
    private short seq = 0;
    private byte packetData = 0;
    private byte packetFec = 0;
    private byte packetIdx = 0;
    private int frameSerial = 0;

    // for loss rtt
    private float loss = 0;
    private short rtt = 0;

    // for qos control
    private int bandwithTotal = 0;
    private int bandwithData = 0;
    private int bandwithFec = 0;

    public QosServerThread() {
        super();
    }

    private void init(){

    }

    public void setNetwork(IPacketNetwork network) {
        this.network = network;
    }

    @Override
    public void onSendPacket(DatagramPacket packet, PacketHeader header) {

    }

    @Override
    public void onReceivePacket(DatagramPacket packet, PacketHeader header) {

    }

    @Override
    public void run() {
        super.run();
    }
}
