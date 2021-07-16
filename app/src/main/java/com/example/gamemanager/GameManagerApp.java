package com.example.gamemanager;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

public class GameManagerApp extends Application {
    public static Context context;
    public static final Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
