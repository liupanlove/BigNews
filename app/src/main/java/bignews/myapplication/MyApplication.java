package bignews.myapplication;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by lazycal on 2017/9/11.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Fresco.initialize(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
