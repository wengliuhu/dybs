package com.artheia.usbcamera.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.artheia.usbcamera.R;
import com.artheia.usbcamera.bean.ConfigBean;
import com.artheia.usbcamera.bean.DATA;
import com.artheia.usbcamera.databinding.DioalogSettingLayoutBinding;
import com.artheia.usbcamera.utils.TtsSpeaker;
import com.artheia.usbcamera.view.widget.AppConstant;

import java.util.ArrayList;
import java.util.List;

import static com.artheia.usbcamera.view.widget.AppConstant.BRIGHTNESS;
import static com.artheia.usbcamera.view.widget.AppConstant.COLOR_RESET;
import static com.artheia.usbcamera.view.widget.AppConstant.COLOR_RESOLUTION;
import static com.artheia.usbcamera.view.widget.AppConstant.CONTRAST;
import static com.artheia.usbcamera.view.widget.AppConstant.SCALE;

/**
 * Created by hujingjing on 2021/10/9 10:27
 */
public class SettingDialog extends DialogFragment {
    private DioalogSettingLayoutBinding mDataBingding;

    public interface DialogCallBack {
        void onDialogCallBack(View view, int type, int progress, boolean isChecked);
    }

    public static SettingDialog newInstance(DialogCallBack dialogCallBack) {
        SettingDialog myDialogFragment = new SettingDialog();
        myDialogFragment.setDialogCallBack(dialogCallBack);
        return myDialogFragment;
    }

    private SeekBar mSeekBarContrast;//对比度
    private SeekBar mSeekBarBrightness;//亮度
    private SeekBar mSeekBarScale;//缩放

    private TextView mTvContrast;
    private TextView mTvBrightness;
    private TextView mTvScale;
    private TextView mTvClose;
    private TextView mTvReset;
    private TextView mTvResolution;
    private View mResolutionView;

    private DialogCallBack mDialogCallBack;

    public void setDialogCallBack(DialogCallBack mDialogCallBack) {
        this.mDialogCallBack = mDialogCallBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBingding = DataBindingUtil.inflate(inflater, R.layout.dioalog_setting_layout, container, false);
        return mDataBingding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mSeekBarContrast = view.findViewById(R.id.seekbar_contrast);
        mSeekBarBrightness = view.findViewById(R.id.seekbar_brightness);
        mSeekBarScale = view.findViewById(R.id.seekbar_scale);

        mTvContrast = view.findViewById(R.id.tv_contrast);
        mTvBrightness = view.findViewById(R.id.tv_brightness);
        mTvScale = view.findViewById(R.id.tv_scale);
        mTvClose = view.findViewById(R.id.tv_close);
        mTvReset = view.findViewById(R.id.tv_reset);
        mResolutionView = view.findViewById(R.id.ll_resolution);
        mTvResolution = view.findViewById(R.id.tv_resolution);

        mTvResolution.setText(ConfigBean.getInstance().getWidth() + "x" + ConfigBean.getInstance().getHeight());

        mDataBingding.rbBrightYellow.setTag(DATA.BRIGHT_YELLOW);
        mDataBingding.rbDarkTea.setTag(DATA.DARK_TEA);
        mDataBingding.rbOriginal.setTag(DATA.ORIGINAL);
        mDataBingding.rbGray.setTag(DATA.GRAY);
        mDataBingding.rbLightTea.setTag(DATA.LIGHT_TEA);
        mDataBingding.rbRedGreen.setTag(DATA.RED_GREEN);
        mDataBingding.rbReverse.setTag(DATA.REVERSE);
        mDataBingding.rbWhiteBorder.setTag(DATA.WHITE_BORDER);
        mDataBingding.rbBlackBorder.setTag(DATA.BLACK_BORDER);
        mDataBingding.rbColorGreenBorder.setTag(DATA.ALL_COLOR_GREEN_BORDER);
        mDataBingding.rbColorYellowBorder.setTag(DATA.ALL_COLOR_YELLOW_BORDER);
        RadioButton button = mDataBingding.rgColor.findViewWithTag(ConfigBean.getInstance().getFilterColor());
        mDataBingding.rgColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton button = group.findViewById(checkedId);
                Object tag = button.getTag();
                if (mDialogCallBack != null && tag instanceof Integer){
                    ConfigBean.getInstance().setFilterColor((Integer) tag);
                    mDialogCallBack.onDialogCallBack(button, (Integer) tag, 0, true);
                }
            }
        });
        button.setChecked(true);
        initListener();
    }

    private void initListener() {
        mSeekBarContrast.setMax(100);
        mSeekBarContrast.setProgress(ConfigBean.getInstance().getContrast());
        mTvContrast.setText(ConfigBean.getInstance().getContrast() + "%");
        mSeekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvContrast.setText("" + (progress) + "%");
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(seekBar, CONTRAST, progress, false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ConfigBean.getInstance().setContrast(seekBar.getProgress());
            }
        });
        mSeekBarBrightness.setMax(100);
        mSeekBarBrightness.setProgress(ConfigBean.getInstance().getBrightness());
        mTvBrightness.setText(ConfigBean.getInstance().getBrightness() + "%");
        mSeekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvBrightness.setText("" + (progress) + "%");
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(seekBar, BRIGHTNESS, progress, false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ConfigBean.getInstance().setBrightness(seekBar.getProgress());
            }
        });
        mSeekBarScale.setMax(23);
        int scale = ConfigBean.getInstance().getScale();
        String showText = "x " + (scale == 0 ? -1 : scale);
        mTvScale.setText(showText);
        mSeekBarScale.setProgress(scale);
        mSeekBarScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String showText = "x " + (progress == 0 ? -1 : progress);
                mTvScale.setText(showText);
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(seekBar, SCALE, progress, false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int showProcess = (progress == 0 ? -1 : progress);
                TtsSpeaker.getInstance().addMessageFlush("[p1500]" + (showProcess > 0 ? "放大 " : "缩小") + Math.abs(showProcess) + "倍");
                ConfigBean.getInstance().setScale(seekBar.getProgress());
            }
        });

        //分辨率
        mResolutionView.setOnClickListener(v-> {
            if (mDialogCallBack != null) {
                mDialogCallBack.onDialogCallBack(mResolutionView, COLOR_RESOLUTION, 0, false);
            }
        });
        //关闭
        mTvClose.setOnClickListener(v-> {
            dismiss();
        });
        //重置
        mTvReset.setOnClickListener(v-> {
            if (mDialogCallBack != null) {
                mDialogCallBack.onDialogCallBack(mResolutionView, COLOR_RESET, 0, false);
            }
            ConfigBean.getInstance().setContrast(AppConstant.CONFIG_CONTRAST);
            ConfigBean.getInstance().setBrightness(AppConstant.CONFIG_BRIGHTNESS);
            ConfigBean.getInstance().setScale(AppConstant.CONFIG_SCALE);
            ConfigBean.getInstance().setGrayShow(false);
            mSeekBarContrast.setProgress(AppConstant.CONFIG_CONTRAST);
            mSeekBarBrightness.setProgress(AppConstant.CONFIG_BRIGHTNESS);
            mSeekBarScale.setProgress(AppConstant.CONFIG_SCALE);
            mTvScale.setText("x " + AppConstant.CONFIG_SCALE);
            ((RadioButton)mDataBingding.rgColor.findViewWithTag(DATA.ORIGINAL)).setChecked(true);
        });
    }

    /**
     * 显示当前的分辨率
     * @param resolution
     */
    public void setResolution(String resolution) {
        mTvResolution.setText(resolution);
    }

    public void updateScale(int progress) {
        String showText = "x " + (progress == 0 ? -1 : progress);
        mTvScale.setText(showText);
        mSeekBarScale.setProgress(progress);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        window.setAttributes(windowParams);
    }

}
