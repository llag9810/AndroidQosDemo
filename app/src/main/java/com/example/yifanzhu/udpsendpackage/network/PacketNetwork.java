package com.example.yifanzhu.udpsendpackage.network;

import com.example.yifanzhu.udpsendpackage.data.PacketReceiveThread;
import com.example.yifanzhu.udpsendpackage.data.PacketSendThread;
import com.example.yifanzhu.udpsendpackage.interfaces.IPacketConsumer;
import com.example.yifanzhu.udpsendpackage.interfaces.IPacketNetwork;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketNetwork implements IPacketNetwork {

    private LinkedBlockingQueue<DatagramPacket> mBlockingQueue;

    private int sendInterval;
    private boolean isStartLooping;
    private PacketSendThread mPacketSendThread;
    private PacketReceiveThread mPacketReceiveThread;
    private IPacketConsumer mIPacketConsumer;
    private DatagramSocket mSocket;

    public PacketNetwork() {
        mBlockingQueue = new LinkedBlockingQueue<>();
        try {
            mSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(DatagramPacket packet) {
        mBlockingQueue.offer(packet);
    }

    @Override
    public void setSendInterval(int ms) {
        sendInterval = ms;
    }

    @Override
    public void startLoop() {
        if (!isStartLooping) {
            isStartLooping = true;
            mPacketSendThread = new PacketSendThread(this);
            mPacketSendThread.start();
            mPacketReceiveThread = new PacketReceiveThread(this);
            mPacketReceiveThread.start();
        }
    }

    @Override
    public void stopLoop() {
        if (isStartLooping) {
            isStartLooping = false;
        }
    }

    public int getSendInterval() {
        return sendInterval;
    }

    public boolean isStartLooping() {
        return isStartLooping;
    }

    public DatagramPacket getPacketFromQueue() {
        return mBlockingQueue.poll();
    }

    public void SetPacketConsumer(IPacketConsumer consumer) {
        this.mIPacketConsumer = consumer;
    }

    public IPacketConsumer getPacketConsumer() {
        return mIPacketConsumer;
    }

    public DatagramSocket getSocket() {
        return mSocket;
    }
}
