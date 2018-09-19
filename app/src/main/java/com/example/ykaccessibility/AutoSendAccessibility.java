package com.example.ykaccessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.List;

public class AutoSendAccessibility extends AccessibilityService {

    public static boolean hasSend;
    public static final int SEND_SUCCESS = 1;
    public static int SEND_STATUS;

    private int i = 0;//记录已发送的人数
    private int page = 1;//记录通讯录的人列表页码,初始页码为1
    private int prepos = -1;//记录页面跳转来源，0--反向跳转 ，1--正向跳转 ：通讯录-->联系人详情-->编辑短信-->系统短信


    //界面发生变化
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {

                String currentActivity = event.getClassName().toString();

                //点击通讯录，跳转到通讯录页面
                if (currentActivity.equals(BulletTextWrapper.BulletClass.BULLET_CLASS_LAUNCHUI)) {
                    handleFlow_LaunchUI();

                    prepos = 1;
                    i++;
                    Log.e("num:", "滚动到第" + i + "个");
                    //点击发送子弹短信，跳转到编辑发送内容页面
                } else if (currentActivity.equals(BulletTextWrapper.BulletClass.BULLET_CLASS_SENDUI)) {
                    if (prepos == 1) {
                        handleFlow_SendUI();
                    } else {
                        BulletUtils.performBack(this);
                    }
                    //编辑发送页面，发送输入框内容
                } else if (currentActivity.equals(BulletTextWrapper.BulletClass.BULLET_CLASS_INPUTUI)) {
                    if (prepos == 1) {
                        handleFlow_InputUI();
                    } else {
                        BulletUtils.performBack(this);
                    }
                    BulletUtils.performBack(this);
                    BulletUtils.performBack(this);
                    prepos = 0;
                    //不是子弹好友，跳转到发送系统短信界面,需要返回到通讯录
                } else if (currentActivity.equals(BulletTextWrapper.Message.BULLETID_SYSTEM_MESSAGE)) {
                    BulletUtils.performBack(this);
                    prepos = 0;
                }
            }
            break;
        }
    }


    private void handleFlow_LaunchUI() {

        try {
            //点击进入通讯录
            Thread.sleep(500);
            BulletUtils.findTextAndClick(this, "通讯录");

            prepos = 0;
            //当前在通讯录界面就点选人
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(BulletTextWrapper.BulletId.BULLETID_CONTACTUI_ITEM_ID);
            List<AccessibilityNodeInfo> listview = nodeInfo.findAccessibilityNodeInfosByViewId(BulletTextWrapper.BulletId.BULLETID_CONTACTUI_LISTVIEW_ID);

            Log.e("name", "列表人数: " + list.size());

            if (i < (list.size() * page)) {
                list.get(i % list.size()).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                list.get(i % list.size()).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

                Log.e("i", "列表人数: " + i);
            } else if (i == list.size() * page) {


                //本页已全部发送，所以下滑列表加载下一页，每次下滑的距离是一屏
                for (int i = 0; i < nodeInfo.getChild(0).getChildCount(); i++) {


                    listview.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

                    page++;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException mE) {
                                mE.printStackTrace();
                            }

                            //todo nodeInfo_ 为NULL 目前不知道怎么办
                            AccessibilityNodeInfo nodeInfo_ = getRootInActiveWindow();
                            List<AccessibilityNodeInfo> list_ = nodeInfo_.findAccessibilityNodeInfosByViewId(BulletTextWrapper.BulletId.BULLETID_CONTACTUI_ITEM_ID);
                            Log.e("name+", "列表人数: " + list_.size());
                            //滑动之后，上一页的最后一个item为当前的第一个item，所以从第二个开始发送
                            list_.get(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            list_.get(1).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }).start();

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleFlow_SendUI() {
        try {
            //点击发送子弹短信控件
            Thread.sleep(500);
            BulletUtils.findTextAndClick(this, "发送子弹短信");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //BulletUtils.findViewIdAndClick(this, "com.bullet.messenger:id/phone_contact_detail_sendbullet");
    }

    private void handleFlow_InputUI() {

        try {
            //点击粘贴输入框内容到EditText
            Thread.sleep(500);
            BulletUtils.findViewByIdAndPasteContent(this, BulletTextWrapper.BulletId.BULLETID_CHATUI_EDITTEXT_ID, BulletUtils.CONTENT);

            //点击图标控件发送内容
            Thread.sleep(1000);
            BulletUtils.findViewIdAndClick(this, "com.bullet.messenger:id/buttonSendMessage");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInterrupt() {

    }
}
