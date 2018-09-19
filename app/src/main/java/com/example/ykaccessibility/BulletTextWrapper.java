package com.example.ykaccessibility;

/**
 * Created by Clearlee on 2017/12/22 0023.
 * 微信版本6.6.0
 */

public class BulletTextWrapper {

    public static final String BULLET_PACKAGENAME = "com.bullet.messenger";


    public static class BulletClass{
        //子弹短信首页
        public static final String BULLET_CLASS_LAUNCHUI = "com.smartisan.flashim.main.activity.MainActivity";
        //点击发送子弹短信页面
        public static final String BULLET_CLASS_SENDUI = "com.smartisan.flashim.contact.activity.PhoneContactActivity";
        //编辑发送页面
        public static final String BULLET_CLASS_INPUTUI= "com.netease.nim.uikit.business.session.activity.UnactivedMessageActivity";
    }


    public static class BulletId{
        /**
         * 通讯录界面
         */
        public static final String BULLETID_CONTACTUI_LISTVIEW_ID = "com.bullet.messenger:id/contact_list_view";
        public static final String BULLETID_CONTACTUI_ITEM_ID = "com.bullet.messenger:id/contacts_info_container";

        /**
         * 聊天界面
         */
        //输入框id
        public static final String BULLETID_CHATUI_EDITTEXT_ID = "com.bullet.messenger:id/editTextMessage";
        //发送按钮id
        public static final String BULLETID_CHATUI_IMAGEVIEW_ID = "com.bullet.messenger:id/buttonSendMessage";

    }
    public static class Message{
        /**
         * 系统短信界面
         */
        public static final String BULLETID_SYSTEM_MESSAGE = "com.android.mms.ui.ComposeMessageActivity";
    }

}

