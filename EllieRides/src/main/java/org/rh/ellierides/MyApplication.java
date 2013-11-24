package org.rh.ellierides;

import android.app.Application;
import android.content.Context;

/**
 * Created by ryanheitner on 10/1/13.
 */
public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
