package com.example.yifanzhu.udpsendpackage.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketUtil {
    public static int HEADER_LENGTH = 12;

    public static DatagramPacket makePacket(PacketHeader header, byte[] data, String ip, int port)
            throws UnknownHostException {

        byte[] sendBuf = new byte[HEADER_LENGTH + data.length];
        ByteBuffer buffer = ByteBuffer.wrap(sendBuf);
        buffer.order(ByteOrder.nativeOrder()).position(0);

        buffer.putShort(header.getLength());  // length      - 2 bytes
        buffer.putShort(header.getSeq());     // seq         - 2 bytes
        byte flag = header.getFlag();
        buffer.put(flag);                     // flag        - 1 byte
        buffer.put(header.getPacketData());   // packetData  - 1 byte
        buffer.put(header.getPacketFec());    // packetFEC   - 1 byte
        buffer.put(header.getPacketIdx());    // packetIndex - 1 byte
        // The last 4 byte of Header are shared by frameSerial or loss info.
        // Write different data for different packet type.
        if ((flag & PacketHeader.FLAG_DATA) != 0) {
            buffer.putInt(header.getFrameSerial());
        }

        return new DatagramPacket(sendBuf, sendBuf.length, InetAddress.getByName(ip), port);
    }

    public static PacketHeader parseHeader(DatagramPacket packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet.getData()).order(ByteOrder.nativeOrder());
        buffer.position(0);
        PacketHeader header = new PacketHeader();

        header.setLength(buffer.getShort());        // length      - 2 bytes
        header.setSeq(buffer.getShort());           // seq         - 2 bytes
        header.setFlag(buffer.get());               // flag        - 1 byte
        header.setPacketData(buffer.get());         // packetData  - 1 byte
        header.setPacketFec(buffer.get());          // packetFEC   - 1 byte
        header.setPacketIdx(buffer.get());          // packetIndex - 1 byte
        // The last 4 byte of Header are shared by frameSerial or loss info.
        // set different fields for different packet type.
        if ((header.getFlag() & PacketHeader.FLAG_DATA) != 0) {
            // set frame serial for data packet.
            header.setFrameSerial(buffer.getInt());
        } else if ((header.getFlag() & PacketHeader.FLAG_REPLY_LOSS) != 0) {
            // Write loss info for loss reply packet.
            header.setPacketRecv(buffer.getShort());
            header.setPacketSend(buffer.getShort());
        }
        // for other type(e.g: ack), no need to set.

        return header;
    }
}
