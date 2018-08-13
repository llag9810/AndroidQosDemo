package com.example.yifanzhu.udpsendpackage.network;

public class PacketHeader {

    public static final int FLAG_DATA = 0x01;
    public static final int FLAG_REPLY_LOSS = 0x02;
    public static final int FLAG_REPLY_ACK = 0x04;
    public static final int FLAG_REQUEST_ACK = 0x08;
    public static final int FLAG_START = 0x10;
    public static final int FLAG_END = 0x20;

    private short length;
    private short seq;
    private byte flag;
    private byte packetData;
    private byte packetFec;
    private byte packetIdx;
    private int frameSerial;
    private short packetRecv;
    private short packetSend;

    public short getPacketRecv() {
        return packetRecv;
    }

    public void setPacketRecv(short packetRecv) {
        this.packetRecv = packetRecv;
    }

    public short getPacketSend() {
        return packetSend;
    }

    public void setPacketSend(short packetSend) {
        this.packetSend = packetSend;
    }

    public short getLength() {
        return length;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public short getSeq() {
        return seq;
    }

    public void setSeq(short seq) {
        this.seq = seq;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public byte getPacketData() {
        return packetData;
    }

    public void setPacketData(byte packetData) {
        this.packetData = packetData;
    }

    public byte getPacketFec() {
        return packetFec;
    }

    public void setPacketFec(byte packetFec) {
        this.packetFec = packetFec;
    }

    public byte getPacketIdx() {
        return packetIdx;
    }

    public void setPacketIdx(byte packetIdx) {
        this.packetIdx = packetIdx;
    }

    public int getFrameSerial() {
        return frameSerial;
    }

    public void setFrameSerial(int serial) {
        this.frameSerial = serial;
    }
}
