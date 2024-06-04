package com.example.capstone1;

import android.app.Application;
import android.content.Context;

public class ApplicationManager extends Application {
    private static ApplicationManager applicationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationManager = this;
    }

    public static synchronized ApplicationManager getInstance(){
        return applicationManager;
    }

    public static Context getAppContext() {
        ApplicationManager instance = getInstance();
        if (instance == null) {
            throw new IllegalStateException("ApplicationManager is not initialized yet.");
        }
        return instance.getApplicationContext();
    }
}
