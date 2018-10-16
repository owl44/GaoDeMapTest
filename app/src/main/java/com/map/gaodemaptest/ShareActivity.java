package com.map.gaodemaptest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        findViewById(R.id.qq).setOnClickListener(this);
        findViewById(R.id.we_chat).setOnClickListener(this);
        findViewById(R.id.we_share).setOnClickListener(this);
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("分享标题");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.qq:
                QQUp();
                showShare();
                break;
            case R.id.we_chat:
                WeChatUp();
                break;
            case R.id.we_share:
                WeShare();
                break;
        }
    }

    private void WeShare() {

    }

    private void WeChatUp() {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(this);
        wechat.SSOSetting(false);
        if (!wechat.isClientValid()) {
            Toast.makeText(this, "微信未安装,请先安装微信", Toast.LENGTH_SHORT).show();
        }
        authorize(wechat);
    }

    private void QQUp() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(this);
        qq.SSOSetting(false);
        if (!qq.isClientValid()) {
            Toast.makeText(this, "QQ未安装,请先安装QQ", Toast.LENGTH_SHORT).show();
        }
        authorize(qq);
    }

    /**
     * 授权
     *
     * @param platform
     */
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        if (platform.isAuthValid()) {  //如果授权就删除授权资料
            platform.removeAccount(true);
        }

        platform.showUser(null); //授权并获取用户信息
    }

    /**
     * 授权成功的回调
     *
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message message = Message.obtain();
        message.what = 1;
        message.obj = platform;
        mHandler.sendMessage(message);
    }

    /**
     * 授权错误的回调
     *
     * @param platform
     * @param i
     * @param throwable
     */
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = platform;
        mHandler.sendMessage(message);
    }

    /**
     * 授权取消的回调
     *
     * @param platform
     * @param i
     */
    @Override
    public void onCancel(Platform platform, int i) {
        Message message = Message.obtain();
        message.what = 3;
        message.obj = platform;
        mHandler.sendMessage(message);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(ShareActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();
                    Platform platform = (Platform) msg.obj;
                    String userId = platform.getDb().getUserId();//获取用户账号
                    String userName = platform.getDb().getUserName();//获取用户名字
                    String userIcon = platform.getDb().getUserIcon();//获取用户头像
                    String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                    Toast.makeText(ShareActivity.this, "用户名：" + userName + "  性别：" + userGender, Toast.LENGTH_SHORT).show();

                    //下面就可以利用获取的用户信息登录自己的服务器或者做自己想做的事啦!
                    //。。。
                    break;
                case 2:
                    Toast.makeText(ShareActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(ShareActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
