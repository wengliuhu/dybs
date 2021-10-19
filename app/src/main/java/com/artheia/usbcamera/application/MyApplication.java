package com.artheia.usbcamera.application;

import android.app.Application;

import com.artheia.usbcamera.utils.CrashHandler;
import com.artheia.usbcamera.utils.TtsSpeaker;

/**application class
 *
 * Created by jianddongguo on 2017/7/20.
 */

public class MyApplication extends Application {
    public static MyApplication mInstance;
    private CrashHandler mCrashHandler;
    // File Directory in sd card
    public static final String DIRECTORY_NAME = "USBCamera";

    @Override
    public void onCreate() {
        super.onCreate();
        mCrashHandler = CrashHandler.getInstance();
        mCrashHandler.init(getApplicationContext(), getClass());
        mInstance = this;
        TtsSpeaker.getInstance().init(this);
    }

    public static MyApplication getAPP(){
        return mInstance;
    }

}
