package com.example.cloud_forecast_test1.base;

import android.app.Application;

import com.example.cloud_forecast_test1.db.DBManager;

import org.xutils.x;

public class UniteApp extends Application {
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        DBManager.iniDB(this);
    }
}
