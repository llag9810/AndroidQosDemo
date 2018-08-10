package com.example.yifanzhu.udpsendpackage.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yifanzhu.udpsendpackage.R;
import com.example.yifanzhu.udpsendpackage.network.PackageHeader;
import com.example.yifanzhu.udpsendpackage.network.PackageUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText mIPEditText;
    private EditText mPortEditText;
    private EditText mDurationEditText;
    private EditText mSizeEditText;
    private Button mButton;
    private boolean isStart = false;
    private String ip = "134.175.51.67";
    private int port = 2333;
    private byte[] buf = new byte[65535];
    private byte[] recvbuf = new byte[65535];
    private int seq = 0;

    private PackageUtil mPackageUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPackageUtil = new PackageUtil();
        mPackageUtil.setOnReceiveListener(new PackageUtil.OnReceiveListener() {
            @Override
            public void onReceive(PackageHeader header, byte[] data) {
                if ((header.getFlag() & PackageHeader.FLAG_LOSS) != 0) {
                    short packetSend = header.getPacketSend();
                    short packetRecv = header.getPacketRecv();
                    double loss;
                    if (packetSend == 0) {
                        loss = 0;
                    } else {
                        loss = packetRecv / packetSend;
                    }
                    Log.d(TAG, "onReceive: send, recv = " + packetSend +", " + packetRecv);
                }
            }
        });
        initView();
        initEvent();
    }

    private void initView() {
        mIPEditText = findViewById(R.id.Edit_text);
        mPortEditText = findViewById(R.id.port_edit_text);
        mIPEditText.setText(ip);
        mPortEditText.setText("" + port);
        mButton = findViewById(R.id.button);
        mSizeEditText = findViewById(R.id.packet_size_edit_text);
        mDurationEditText = findViewById(R.id.duration_edit_text);
    }
    private void initEvent() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = mIPEditText.getText().toString();
                String port = mPortEditText.getText().toString();
                String packetSize = mSizeEditText.getText().toString();
                String duration = mDurationEditText.getText().toString();

                mPackageUtil.setIp(ip);
                mPackageUtil.setPort(Integer.parseInt(port));
                mPackageUtil.setPacketSize(Integer.parseInt(packetSize));
                mPackageUtil.setDuration(Integer.parseInt(duration));
                isStart = !isStart;
                if (isStart) {
                    mButton.setText("停止发包");
                    mPackageUtil.startSend();
                    mPackageUtil.startReceive();
                } else {
                    mButton.setText("开始发包");
                    mPackageUtil.stopSend();
                    mPackageUtil.stopReceive();
                    return;
                }
            }
        });
    }
}
