package com.artheia.usbcamera.view.widget;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.artheia.usbcamera.R;
import com.artheia.usbcamera.bean.ConfigBean;


/**
 * Created by hujingjing on 2021/10/11 15:30
 */
public class LightScaleView extends FrameLayout implements GestureDetector.OnGestureListener {

    public interface ProgressCallBack {
        void onLightChanged(int progress);

        void onLightChangedEnd(int progress);

        void onVolumeChanged(int progress);

        void onScaleChanged(int progress);

        void onScaleChangedEnd(int progress, boolean playAudio);
    }

    private ProgressCallBack mProgressCallBack;

    public void setProgressCallBack(ProgressCallBack progressCallBack) {
        this.mProgressCallBack = progressCallBack;
    }

    private int mScreenWidth;
    private GestureDetector mGestureDetector;
    private int flag_light = 22;
    private int flag_volume = 33;
    private int flag_scale = 44;
    int flag = 0;

    private AudioManager audiomanager;
    private int maxVolume, currentVolume;
    private int maxScale = 23;
    private int currentScale;
    private int currentLight;
    private int maxLight = 100;
    private ImageView mIvLight;
    private ImageView mIvScale;
    private TextView tv_light;
    private View mContentView;
    private boolean isPlayAudio = true;
    private long downTime;

    public LightScaleView(Context context) {
        super(context);
        init();
    }

    public LightScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_light_layout, this);
        mIvLight = view.findViewById(R.id.iv_light);
        mIvScale = view.findViewById(R.id.iv_scale);
        tv_light = view.findViewById(R.id.tv_txt);
        mContentView = view.findViewById(R.id.ll_content);
        mScreenWidth = ScreenUtils.getWidth(getContext());
        mGestureDetector = new GestureDetector(getContext(), this);
        audiomanager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
        currentScale = ConfigBean.getInstance().getScale();
        currentLight = ConfigBean.getInstance().getBrightness();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 手势里除了singleTapUp，没有其他检测up的方法
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mContentView.setVisibility(GONE);
            if (flag == flag_light) {
                if (mProgressCallBack != null) {
                    mProgressCallBack.onLightChangedEnd(currentLight);
                }
            } else if (flag == flag_scale) {
                if (mProgressCallBack != null) {
                    mProgressCallBack.onScaleChangedEnd(currentScale, isPlayAudio);
                }
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        downTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        long scrollTime = System.currentTimeMillis();
        float startX = motionEvent.getRawX();
        float startY = motionEvent.getRawY();
        float endX = motionEvent1.getRawX();
        float endY = motionEvent1.getRawY();
        if (startX > mScreenWidth * 3.0 / 5) {// 缩放
            flag = flag_scale;
        } else if (startX < mScreenWidth * 2.0 / 5) {// 亮度
            flag = flag_light;
        }

        if(flag == flag_light) {
            mContentView.setVisibility(VISIBLE);
            mIvLight.setVisibility(VISIBLE);
            mIvScale.setVisibility(GONE);
            if (distanceY >= DensityUtil.dpToPx(getContext(), 1f)) {
                if (currentLight < maxLight) {
                    currentLight+=3;
                    if (currentLight > maxLight) {
                        currentLight = maxLight;
                    }
                }
            } else if (distanceY <= -DensityUtil.dpToPx(getContext(), 1f)) {// 亮度调小
                if (currentLight > 0) {
                    currentLight-=3;
                }
                if (currentLight < 0) {
                    currentLight = 0;
                }
            }
            mIvLight.setImageResource(R.drawable.ic_bright);
            tv_light.setText(currentLight + "%");
            if (mProgressCallBack != null) {
                mProgressCallBack.onLightChanged(currentLight);
            }
        } else if (flag == flag_scale) {
            mContentView.setVisibility(VISIBLE);
            mIvLight.setVisibility(GONE);
            mIvScale.setVisibility(VISIBLE);
            if (distanceY >= DensityUtil.dpToPx(getContext(), 2f)) {
                if (scrollTime - downTime < 200) {
                    return false;
                }
                downTime = scrollTime;
                if (currentScale < maxScale) {
                    currentScale++;
                }
            } else if (distanceY <= -DensityUtil.dpToPx(getContext(), 2f)) {
                if (scrollTime - downTime < 200) {
                    return false;
                }
                downTime = scrollTime;
                if (currentScale > 0) {
                    currentScale--;
                }
            }
            int showProcess = (currentScale == 0 ? -1 : currentScale);
            if ((distanceY < 0 && currentScale == 0) || (distanceY > 0 && currentScale == 23)) {
                isPlayAudio = false;
            } else {
                isPlayAudio = true;
            }
            mIvScale.setImageResource(showProcess >= 0 ? R.mipmap.ic_add : R.mipmap.ic_reduce);
            tv_light.setText((showProcess > 0 ? "放大 " : "缩小") + Math.abs(showProcess) + "倍");
            if (mProgressCallBack != null) {
                mProgressCallBack.onScaleChanged(currentScale);
            }
        } else if (flag == flag_volume) {
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
            if (currentVolume == 0) {// 静音，设定静音独有的图片
//                gesture_iv_player_volume.setImageResource(R.drawable.mn_player_volume_close);
            }
            if (distanceY >= DensityUtil.dpToPx(getContext(), 2f)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                    currentVolume++;
                }
//                gesture_iv_player_volume.setImageResource(R.drawable.mn_player_volume_open);
            } else if (distanceY <= -DensityUtil.dpToPx(getContext(), 5f)) {// 音量调小
                if (currentVolume > 0) {
                    currentVolume--;
                    if (currentVolume == 0) {// 静音，设定静音独有的图片
//                        gesture_iv_player_volume.setImageResource(R.drawable.mn_player_volume_close);
                    }
                }
            }
            int percentage = (int) ((currentVolume * 100) / maxVolume);
//            geture_tv_volume_percentage.setText(String.valueOf(percentage + "%"));
//            geture_tv_volume_percentage_progress.setProgress(percentage);
            audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) currentVolume, 0);
            if (mProgressCallBack != null) {
                mProgressCallBack.onVolumeChanged(percentage);
            }
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public void setCurrentScale(int currentScale) {
        this.currentScale = currentScale;
    }

    public void setCurrentLight(int currentLight) {
        this.currentLight = currentLight;
    }
}
