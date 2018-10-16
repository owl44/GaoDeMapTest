package com.map.gaodemaptest;

import android.app.Application;

import com.mob.MobSDK;

/**
 * Created by Administrator on 2018/10/15.
 */

public class GDApplicationConfig extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
    }
}
