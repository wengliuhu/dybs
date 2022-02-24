package com.artheia.usbcamera.wakeup;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yanantec.ynbus.message.YnMessageManager;

import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/24 10:53
 * Describe：
 */
public class WakeUpHelper implements EventListener
{
    private final String TAG = getClass().toString();
    public final static String KEY_WAKE_UP = "KEY_WAKE_UP";
    private EventManager wakeup;
    private static class SingerHolder{
        private static WakeUpHelper mInstance = new WakeUpHelper();
        private SingerHolder()
        {}
    }

    private WakeUpHelper()
    {}

    public static WakeUpHelper getInstance(){
        return SingerHolder.mInstance;
    }

    public void init(Context context){
        // 基于SDK唤醒词集成1.1 初始化EventManager
        wakeup = EventManagerFactory.create(context, "wp");
        // 基于SDK唤醒词集成1.3 注册输出事件
        wakeup.registerListener(this); //  EventListener 中 onEvent方法
    }

    public void start(Context context){
        // 基于SDK唤醒词集成第2.1 设置唤醒的输入参数
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        InFileStream.setContext(context);
        String json = null; // 这里可以替换成你需要测试的json
        json = new JSONObject(params).toString();
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    // 基于SDK唤醒词集成第4.1 发送停止事件
    public void stop() {
        wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0); //
    }

    //  基于SDK唤醒词集成1.2 自定义输出事件类 EventListener  回调方法
    // 基于SDK唤醒3.1 开始回调事件
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        Log.d(TAG, "-----------onEvent----name:" + name + ";;;params:" + params);
        if (SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS.equals(name)) { // 识别唤醒词成功
            WakeUpResult result = WakeUpResult.parseJson(name, params);
            int errorCode = result.getErrorCode();
            if (result.hasError()) { // error不为0依旧有可能是异常情况
//                listener.onError(errorCode, "", result);
                Log.d(TAG, "------errorCode--" + errorCode);
            } else {
                String word = result.getWord();
                YnMessageManager.getInstance().sendMessage(KEY_WAKE_UP, result.getWord());
            }
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR.equals(name)) { // 识别唤醒词报错
            WakeUpResult result = WakeUpResult.parseJson(name, params);
            int errorCode = result.getErrorCode();
            if (result.hasError()) {
                Log.d(TAG, "------errorCode--" + errorCode);

            }
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_STOPED.equals(name)) { // 关闭唤醒词
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_AUDIO.equals(name)) { // 音频回调
        }
    }

}
