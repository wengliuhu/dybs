package com.artheia.usbcamera.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;


import java.util.HashMap;
import java.util.Locale;

/**
 *
 */
public class TtsSpeaker{


    private TextToSpeech mTextToSpeech;
    private static TtsSpeaker mSpeaker = null;

    HashMap ttsOptions = new HashMap<>();

    public void init(Context context){

        init(context,Locale.CHINESE);
    }

    private void init(Context context, final Locale locale){
        if(null == ttsOptions){
            ttsOptions = new HashMap<>();
            ttsOptions.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utterance");
        }

        if(locale!=null){

            release();

            mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {

                        new Thread(){
                            @Override
                            public void run() {
                                super.run();

                                int result = mTextToSpeech.setLanguage(locale);


                                if (result == TextToSpeech.LANG_MISSING_DATA
                                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    release();
                                } else {
                                    // 设置音调，值越大声音越尖（女生），值越小则变成男声,1. 0是常规
                                    mTextToSpeech.setPitch(1.0f);
                                    //设定语速 ，默认1.0正常语速
                                    mTextToSpeech.setSpeechRate(1f);

                                    mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                                        @Override
                                        public void onStart(String utteranceId) {
                                        }

                                        @Override
                                        public void onDone(String utteranceId) {
                                        }

                                        @Override
                                        public void onError(String utteranceId) {
                                        }

                                        @Override
                                        public void onError(String utteranceId, int errorCode) {
                                            super.onError(utteranceId, errorCode);
                                        }

                                        @Override
                                        public void onStop(String utteranceId, boolean interrupted) {
                                            super.onStop(utteranceId, interrupted);

                                        }

                                        @Override
                                        public void onBeginSynthesis(String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {
                                            super.onBeginSynthesis(utteranceId, sampleRateInHz, audioFormat, channelCount);

                                        }

                                        @Override
                                        public void onAudioAvailable(String utteranceId, byte[] audio) {
                                            super.onAudioAvailable(utteranceId, audio);

                                        }

                                        @Override
                                        public void onRangeStart(String utteranceId, int start, int end, int frame) {
                                            super.onRangeStart(utteranceId, start, end, frame);

                                        }
                                    });
                                }
                            }
                        }.start();
                    } else {
                        release();
                    }
                }
            });
        }
    }

    private TtsSpeaker() {
    }

    /**
     * @param message
     * @param
     */
    public void addMessage(String message) {
        if (mTextToSpeech == null) return;

//        logger.debug("addMessage begin speak");

        mTextToSpeech.speak(message, TextToSpeech.QUEUE_ADD, ttsOptions);

//        logger.debug("addMessage end speak");
    }

    /**
     * @param message
     * @param
     */
    public void addMessageFlush(String message) {
        if (TextUtils.isEmpty(message)) return;
        Log.d("kim", "----addMessageFlush--" + message);
        if (mTextToSpeech == null) return;
        mTextToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, ttsOptions);

        //        logger.debug("addMessage end speak");
    }

    public static TtsSpeaker getInstance() {
        if (mSpeaker == null) {
            synchronized (TtsSpeaker.class) {
                if (mSpeaker == null) {
                    mSpeaker = new TtsSpeaker();
                }
            }
        }

        return mSpeaker;
    }

    public void release() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            mTextToSpeech=null;
        }
    }
}
