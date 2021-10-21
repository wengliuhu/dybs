package com.artheia.usbcamera.view;

import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.artheia.usbcamera.R;
import com.artheia.usbcamera.UVCCameraHelper;
import com.artheia.usbcamera.bean.ConfigBean;
import com.artheia.usbcamera.utils.FileUtils;
import com.artheia.usbcamera.utils.TtsSpeaker;
import com.artheia.usbcamera.utils.Utils;
import com.artheia.usbcamera.view.widget.AppConstant;
import com.artheia.usbcamera.view.widget.AutoScanView;
import com.artheia.usbcamera.view.widget.LightScaleView;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.Size;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;
import com.serenegiant.usb.widget.UVCCameraTextureView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * UVCCamera use demo
 * <p>
 * Created by jiangdongguo on 2017/9/30.
 */

public class USBCameraActivity extends AppCompatActivity implements CameraDialog.CameraDialogParent, CameraViewInterface.Callback {
    private static final String TAG = "Debug";
    @BindView(R.id.camera_view)
    public View mTextureView;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.asv)
    AutoScanView mAutoScanView;
    @BindView(R.id.light_scale)
    LightScaleView mLightScaleView;
    @BindView(R.id.tv_fps)
    public TextView mTvFps;

    private UVCCameraHelper mCameraHelper;
    private UVCCameraTextureView mUVCCameraView;
    private AlertDialog mDialog;
    // 控制视频流的变换
    private Matrix mMatrix;
    private int mCenterX,mCenterY;
    //新增设置弹窗
    private SettingDialog mSettingDialog;

    private boolean isRequest;
    private boolean isPreview;

    private UVCCameraHelper.OnMyDevConnectListener listener = new UVCCameraHelper.OnMyDevConnectListener() {

        @Override
        public void onAttachDev(UsbDevice device) {
            // request open permission
            if (!isRequest) {
                isRequest = true;
                if (mCameraHelper != null) {
                    mCameraHelper.requestPermission(0);
                }
            }
        }

        @Override
        public void onDettachDev(UsbDevice device) {
            // close camera
            if (isRequest) {
                isRequest = false;
                mCameraHelper.closeCamera();
                showShortMsg(device.getDeviceName() + " is out");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvFps.removeCallbacks(mFpsTask);
                        mTvFps.setVisibility(View.GONE);
                        mAutoScanView.setVisibility(View.GONE);
                        if (mSettingDialog != null) {
                            mSettingDialog.dismiss();
                        }
                    }
                });
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            if (!isConnected) {
                showShortMsg("fail to connect,please check resolution params");
                isPreview = false;
            } else {
                isPreview = true;
                showShortMsg("connecting");
                // initialize seekbar
                // need to wait UVCCamera initialize over
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Looper.prepare();
                        if(mCameraHelper != null && mCameraHelper.isCameraOpened()) {
                            runOnUiThread(mFpsTask);
                            //2021.10.19 14：30添加
                            mMatrix = new Matrix(mUVCCameraView.getMatrix());
                            int scale = ConfigBean.getInstance().getScale();
                            int light = ConfigBean.getInstance().getBrightness();
                            mMatrix.setScale(scale, scale, mCenterX, mCenterY);
                            mCenterX = (mUVCCameraView.getRight() + mUVCCameraView.getLeft()) / 2;
                            mCenterY = (mUVCCameraView.getBottom() + mUVCCameraView.getTop()) /2;
                            int boxWidth =  Utils.dp2px(USBCameraActivity.this, scale + 40);
                            // 红色矩形的宽度
                            float width = (mUVCCameraView.getHeight() - boxWidth) * (1.0f * scale / 23) + boxWidth;
                            mAutoScanView.freash(mCenterX - width / 2, mCenterY - width / 2, mCenterX + width / 2, mCenterY + width / 2);
                            //获取存储的值给手势view
                            if (mLightScaleView != null) {
                                mLightScaleView.setCurrentLight(light);
                                mLightScaleView.setCurrentScale(scale);
                            }
                        }
                        Looper.loop();
                    }
                }).start();
            }
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            showShortMsg("disconnecting");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usbcamera);
        ButterKnife.bind(this);
        initView();

        // step.1 initialize UVCCameraHelper
        mUVCCameraView = (UVCCameraTextureView) mTextureView;
        mUVCCameraView.setCallback(this);
        mCameraHelper = UVCCameraHelper.getInstance();
        mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.setDefaultPreviewSize(ConfigBean.getInstance().getWidth(), ConfigBean.getInstance().getHeight());
        mCameraHelper.initUSBMonitor(this, mUVCCameraView, listener);

        mCameraHelper.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] nv21Yuv) {
                Log.d(TAG, "onPreviewResult: "+nv21Yuv.length);
            }
        });
        findViewById(R.id.iv_setting).setOnClickListener(v-> {
            showSettingDialog();
        });
        mLightScaleView.setProgressCallBack(new LightScaleView.ProgressCallBack() {
            @Override
            public void onLightChanged(int progress) {
                if (mCameraHelper == null || !mCameraHelper.isCameraOpened()) return;
                mCameraHelper.setModelValue(UVCCameraHelper.MODE_BRIGHTNESS, progress);
            }

            @Override
            public void onLightChangedEnd(int progress) {
                if (mCameraHelper == null || !mCameraHelper.isCameraOpened()) return;
                ConfigBean.getInstance().setBrightness(progress);
            }

            @Override
            public void onVolumeChanged(int progress) {

            }

            @Override
            public void onScaleChanged(int progress) {
                if (mCameraHelper == null || !mCameraHelper.isCameraOpened() || mMatrix == null) return;
                ConfigBean.getInstance().setScale(progress);
                if (mSettingDialog != null) {
                    mSettingDialog.updateScale(progress);
                } else {
                    goScale(progress, 23);
                }
            }

            @Override
            public void onScaleChangedEnd(int progress, boolean playAudio) {
                if (mCameraHelper == null || !mCameraHelper.isCameraOpened() || mMatrix == null) return;
                int showProcess = (progress == 0 ? -1 : progress);
                TtsSpeaker.getInstance().addMessageFlush("[p1500]" + (showProcess > 0 ? "放大 " : "缩小") + Math.abs(showProcess) + "倍");
            }
        });
    }

    private void initView() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // step.2 register USB event broadcast
        if (mCameraHelper != null) {
            mCameraHelper.registerUSB();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // step.3 unregister USB event broadcast
        if (mCameraHelper != null) {
            mCameraHelper.unregisterUSB();
        }
    }

    private void showResolutionListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(USBCameraActivity.this);
        View rootView = LayoutInflater.from(USBCameraActivity.this).inflate(R.layout.layout_dialog_list, null);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_dialog);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(USBCameraActivity.this, android.R.layout.simple_list_item_1, getResolutionList()){
            @NonNull
            @Override
            public View getView(int position,  View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                final String resolution = getItem(position);
                String[] tmp = resolution.split("x");
                if (tmp != null && tmp.length >= 2) {
                    if (Integer.valueOf(tmp[0])== ConfigBean.getInstance().getWidth()
                            && Integer.valueOf(tmp[1]) == ConfigBean.getInstance().getHeight()){
                        view.setBackgroundColor(Color.parseColor("#eaeaea"));
                    }else {
                        view.setBackgroundColor(Color.parseColor("#ffffff"));

                    }
                }
                return view;
            }
        };
        if (adapter != null) {
            listView.setAdapter(adapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mCameraHelper == null || !mCameraHelper.isCameraOpened())
                    return;
                final String resolution = (String) adapterView.getItemAtPosition(position);
                String[] tmp = resolution.split("x");
                if (tmp != null && tmp.length >= 2) {
                    int widht = Integer.valueOf(tmp[0]);
                    int height = Integer.valueOf(tmp[1]);
                    mCameraHelper.updateResolution(widht, height);
                    mUVCCameraView.resetFps();
                    ConfigBean configBean = ConfigBean.getInstance();
                    configBean.setWidth(widht);
                    configBean.setHeight(height);
                    configBean.save();
                    //更新弹窗分辨率数值
                    if (mSettingDialog != null) {
                        mSettingDialog.setResolution(widht + "x" + height);
                    }
                }
                mDialog.dismiss();
            }
        });

        builder.setView(rootView);
        mDialog = builder.create();
        mDialog.show();
    }

    // example: {640x480,320x240,etc}
    private List<String> getResolutionList() {
        List<Size> list = mCameraHelper.getSupportedPreviewSizes();
        List<String> resolutions = null;
        if (list != null && list.size() != 0) {
            resolutions = new ArrayList<>();
            for (Size size : list) {
                if (size != null) {
                    resolutions.add(size.width + "x" + size.height);
                }
            }
        }
        return resolutions;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.releaseFile();
        // step.4 release uvc camera resources
        if (mCameraHelper != null) {
            mCameraHelper.release();
        }
    }

    private void showShortMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public USBMonitor getUSBMonitor() {
        return mCameraHelper.getUSBMonitor();
    }

    @Override
    public void onDialogResult(boolean canceled) {
        if (canceled) {
            showShortMsg("取消操作");
        }
    }

    public boolean isCameraOpened() {
        return mCameraHelper.isCameraOpened();
    }

    @Override
    public void onSurfaceCreated(CameraViewInterface view, Surface surface) {
        if (!isPreview && mCameraHelper.isCameraOpened()) {
            mCameraHelper.startPreview(mUVCCameraView);
            isPreview = true;
        }
    }

    @Override
    public void onSurfaceChanged(CameraViewInterface view, Surface surface, int width, int height) {

    }

    @Override
    public void onSurfaceDestroy(CameraViewInterface view, Surface surface) {
        if (isPreview && mCameraHelper.isCameraOpened()) {
            mCameraHelper.stopPreview();
            isPreview = false;
        }
    }

    private void showSettingDialog() {
        mSettingDialog = SettingDialog.newInstance(new SettingDialog.DialogCallBack() {
            @Override
            public void onDialogCallBack(View view, int type, int progress, boolean isChecked) {
                if(mCameraHelper != null && mCameraHelper.isCameraOpened() && mMatrix != null) {
                    switch (type) {
                        //对比度
                        case AppConstant.CONTRAST:
                            if(mCameraHelper != null && mCameraHelper.isCameraOpened()) {
                                mCameraHelper.setModelValue(UVCCameraHelper.MODE_CONTRAST,progress);
                                ConfigBean.getInstance().setContrast(progress);
                            }
                            break;
                        //亮度
                        case AppConstant.BRIGHTNESS:
                            if(mCameraHelper != null && mCameraHelper.isCameraOpened()) {
                                mCameraHelper.setModelValue(UVCCameraHelper.MODE_BRIGHTNESS, progress);
                                ConfigBean.getInstance().setBrightness(progress);
                                if (mLightScaleView != null) {
                                    mLightScaleView.setCurrentLight(progress);
                                }
                            }
                            break;
                        //缩放
                        case AppConstant.SCALE:
                            goScale(progress, ((SeekBar)view).getMax());
                            break;
                        //分辨率
                        case AppConstant.COLOR_RESOLUTION:
                            showResolutionListDialog();
                            break;
                        //黑白（灰度）
                        case AppConstant.COLOR_GRAY:
                            mUVCCameraView.changeGray(isChecked);
                            break;
                        //重置
                        case AppConstant.COLOR_RESET:
                            mCameraHelper.resetModelValue(UVCCameraHelper.MODE_CONTRAST);
                            mCameraHelper.resetModelValue(UVCCameraHelper.MODE_BRIGHTNESS);
                            mUVCCameraView.changeGray(false);
                            if (mSettingDialog != null) {
                                mSettingDialog.updateScale(AppConstant.CONFIG_SCALE);
                            }
                            List<Size> list = mCameraHelper.getSupportedPreviewSizes();
                            if (Utils.isEmpty(list)){
                                return;
                            }
                            Size defaultSize = getDefaultSize(list);
                            if (defaultSize != null) {
                                updateResolution(defaultSize);
                            } else {
                                updateResolution(list.get(0));
                            }
                            break;
                    }
                }
            }
        });
        mSettingDialog.show(getSupportFragmentManager(), "");
    }

    private void goScale(int progress, int maxProgress) {
        if(mCameraHelper != null && mCameraHelper.isCameraOpened() && mMatrix != null) {
            int scale = progress + 1;
            int scaleX = (1 - scale) * mUVCCameraView.getWidth() / (scale * 2);
            int scaleY = (1 - scale) * mUVCCameraView.getHeight() / (scale * 2);
            Matrix matrix = new Matrix();
            matrix.setValues(new float[]{
                    scale , 0, scale * scaleX,
                    0 , scale, scale * scaleY,
                    0 , 0, 1,
            });
            mMatrix.reset();
            mMatrix.set(matrix);
//                                mMatrix.setScale(scale, scale, scaleX, scaleY);
            mUVCCameraView.setTransform(mMatrix);
            mUVCCameraView.postInvalidate();
            ConfigBean.getInstance().setScale(progress);

            int boxWidth =  Utils.dp2px(USBCameraActivity.this, progress + 40);
            // 红色矩形的宽度
            float width = (mUVCCameraView.getHeight() - boxWidth) * (1.0f * progress / maxProgress) + boxWidth;
            mAutoScanView.freash(mCenterX - width / 2, mCenterY - width / 2, mCenterX + width / 2, mCenterY + width / 2);
        }
    }

    private void updateResolution(Size size) {
        if (size != null){
            int widht = Integer.valueOf(size.width);
            int height = Integer.valueOf(size.height);
            ConfigBean configBean = ConfigBean.getInstance();
            configBean.setWidth(widht);
            configBean.setHeight(height);
            configBean.save();
            mCameraHelper.updateResolution(widht, height);
            mUVCCameraView.resetFps();
            //更新弹窗分辨率数值
            if (mSettingDialog != null) {
                mSettingDialog.setResolution(widht + "x" + height);
            }
        }
    }

    /**
     * 获取默认分辨率的下标
     * @param list
     * @return
     */
    private Size getDefaultSize(List<Size> list) {
        Size resultSize = null;
        for(int i = 0; i < list.size(); i++) {
            Size size = list.get(i);
            if(size.width == UVCCamera.DEFAULT_PREVIEW_WIDTH && size.height == UVCCamera.DEFAULT_PREVIEW_HEIGHT) {
                resultSize = size;
                break;
            }
        }
        return resultSize;
    }

    private final Runnable mFpsTask = new Runnable() {
        @Override
        public void run() {
            float srcFps;
            if (mUVCCameraView != null) {
                mUVCCameraView.updateFps();
                srcFps = mUVCCameraView.getFps();
            } else {
                srcFps = 0.0f;
            }
            String reslut = String.format(Locale.US, "FPS:%4.1f", srcFps);
            if (mTvFps.getVisibility() == View.GONE) {
                mTvFps.setVisibility(View.VISIBLE);
            }
            if (mAutoScanView.getVisibility() == View.GONE) {
                mAutoScanView.setVisibility(View.VISIBLE);
            }
            mTvFps.setText(reslut);
            mTvFps.postDelayed(this, 1000);
        }
    };
}
