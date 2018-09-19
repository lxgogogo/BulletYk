package com.example.ykaccessibility;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.ykaccessibility.AutoSendAccessibility.SEND_STATUS;
import static com.example.ykaccessibility.AutoSendAccessibility.SEND_SUCCESS;
import static com.example.ykaccessibility.AutoSendAccessibility.hasSend;

import static com.example.ykaccessibility.BulletUtils.CONTENT;

public class MainActivity extends AppCompatActivity {

    private TextView mSendStatus;
    private EditText mContent;
    private Button mSendContent;
    private AccessibilityManager accessibilityManager;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVIew();
    }

    private void initVIew() {

        mContent = findViewById(R.id.edit_View);
        mSendStatus = findViewById(R.id.sendStatus);
        mSendContent = findViewById(R.id.button_view);

    }

    //打开子弹短信
    public void openBullet(View v) {

        CheckAndStartService();

    }

    private void CheckAndStartService() {
        accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);

        content = mContent.getText().toString();

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(MainActivity.this, "内容不能为空", Toast.LENGTH_SHORT);
        }

        if (!accessibilityManager.isEnabled()) {
            openService();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    statusHandler.sendEmptyMessage(goBullet());
                }
            }).start();
        }
    }

    private int goBullet() {
        try {
            setValue(content);
            hasSend = false;
            Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(BulletTextWrapper.BULLET_PACKAGENAME, BulletTextWrapper.BulletClass.BULLET_CLASS_LAUNCHUI);
            startActivity(intent);

            while (true) {
                if (hasSend) {
                    return SEND_STATUS;
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        openService();
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SEND_STATUS;
        }

    }

    private void setValue(String content) {
            CONTENT = content;
            hasSend = false;

    }

    //打开系统设置中辅助功能
    private void openService() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "找到ykaccessibility，然后开启服务即可", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler statusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setSendStatusText(msg.what);
        }
    };

    private void setSendStatusText(int status) {
        if (status == SEND_SUCCESS) {
            mSendStatus.setText("发送成功");
        } else {
            mSendStatus.setText("发送失败");
        }
    }
}