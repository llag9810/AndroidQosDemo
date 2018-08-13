package com.example.yifanzhu.udpsendpackage.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yifanzhu.udpsendpackage.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText mIpEditText;
    private EditText mPortEditText;
    private EditText mDurationEditText;
    private EditText mSizeEditText;
    private Button mButton;
    private String ip = "134.175.51.67";
    private int port = 2333;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        mIpEditText = findViewById(R.id.Edit_text);
        mPortEditText = findViewById(R.id.port_edit_text);
        mButton = findViewById(R.id.button);
        mSizeEditText = findViewById(R.id.packet_size_edit_text);
        mDurationEditText = findViewById(R.id.duration_edit_text);
    }
    private void initEvent() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = mIpEditText.getText().toString();
                String port = mPortEditText.getText().toString();
                String packetSize = mSizeEditText.getText().toString();
                String duration = mDurationEditText.getText().toString();
            }
        });
    }
}
