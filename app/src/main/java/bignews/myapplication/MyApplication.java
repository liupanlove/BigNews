package bignews.myapplication;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by lazycal on 2017/9/11.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Fresco.initialize(this);
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=59b54aff");
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
