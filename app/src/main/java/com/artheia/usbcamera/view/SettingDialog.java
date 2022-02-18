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
//    private CheckBox mCheckBox2Colors;//两色
//    private CheckBox mCheckBoxGray;//黑白（灰度）
//    private CheckBox mCheckBoxReverse;//反相
//    private CheckBox mCheckBoxBorder;//描边
//    private CheckBox mCheckBoxCompare;//对比
//    private CheckBox mCheckboxColorize;//伪彩
    private TextView mTvContrast;
    private TextView mTvBrightness;
    private TextView mTvScale;
    private TextView mTvClose;
    private TextView mTvReset;
    private TextView mTvResolution;
    private View mResolutionView;
//    private TextView mTvOriginalColor;
//    private TextView mTvGrayColor;
//    private TextView mTvBrightYellowColor;

    private DialogCallBack mDialogCallBack;

    public void setDialogCallBack(DialogCallBack mDialogCallBack) {
        this.mDialogCallBack = mDialogCallBack;
    }

    private List<CheckBox> mList = new ArrayList<>();

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
//        getDialog().setCancelable(false);
//        getDialog().setCanceledOnTouchOutside(false);
        mSeekBarContrast = view.findViewById(R.id.seekbar_contrast);
        mSeekBarBrightness = view.findViewById(R.id.seekbar_brightness);
        mSeekBarScale = view.findViewById(R.id.seekbar_scale);
//        mCheckBox2Colors = view.findViewById(R.id.cb_2_colors);
//        mCheckBoxGray = view.findViewById(R.id.cb_gray);
//        mCheckBoxReverse = view.findViewById(R.id.cb_reserve);
//        mCheckBoxBorder = view.findViewById(R.id.cb_border);
//        mCheckBoxCompare = view.findViewById(R.id.cb_compare);
//        mCheckboxColorize = view.findViewById(R.id.cb_colorize);
        mTvContrast = view.findViewById(R.id.tv_contrast);
        mTvBrightness = view.findViewById(R.id.tv_brightness);
        mTvScale = view.findViewById(R.id.tv_scale);
        mTvClose = view.findViewById(R.id.tv_close);
        mTvReset = view.findViewById(R.id.tv_reset);
        mResolutionView = view.findViewById(R.id.ll_resolution);
        mTvResolution = view.findViewById(R.id.tv_resolution);
//        mTvOriginalColor = view.findViewById(R.id.tv_original_color);
//        mTvGrayColor = view.findViewById(R.id.tv_gray_color);
//        mTvBrightYellowColor = view.findViewById(R.id.tv_bright_yellow_color);
        mTvResolution.setText(ConfigBean.getInstance().getWidth() + "x" + ConfigBean.getInstance().getHeight());

        mDataBingding.rbBrightYellow.setTag(DATA.BRIGHT_YELLOW);
        mDataBingding.rbDarkTea.setTag(DATA.DARK_TEA);
        mDataBingding.rbOriginal.setTag(DATA.ORIGINAL);
        mDataBingding.rbGray.setTag(DATA.GRAY);
        mDataBingding.rbLightTea.setTag(DATA.LIGHT_TEA);
        mDataBingding.rbReverse.setTag(DATA.REVERSE);
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
        RadioButton button = mDataBingding.rgColor.findViewWithTag(ConfigBean.getInstance().getFilterColor());
        button.setChecked(true);
        initListener();
        initList();
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
/*        //两色
        mCheckBox2Colors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConfigBean.getInstance().setColor2Show(isChecked);
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(buttonView, COLOR_2, 0, isChecked);
                }
            }
        });
        //黑白(灰度)
        mCheckBoxGray.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConfigBean.getInstance().setGrayShow(isChecked);
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(buttonView, COLOR_GRAY, 0, isChecked);
                }
            }
        });
        //反相
        mCheckBoxReverse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConfigBean.getInstance().setColorReverseShow(isChecked);
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(buttonView, COLOR_REVERSE, 0, isChecked);
                }
            }
        });
        //描边
        mCheckBoxBorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConfigBean.getInstance().setColorBorderShow(isChecked);
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(buttonView, COLOR_BORDER, 0, isChecked);
                }
            }
        });
        //对比
        mCheckBoxCompare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConfigBean.getInstance().setColorReverseShow(isChecked);
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(buttonView, COLOR_COMPARE, 0, isChecked);
                }
            }
        });
        //伪彩
        mCheckboxColorize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConfigBean.getInstance().setColorizeShow(isChecked);
                if (mDialogCallBack != null) {
                    mDialogCallBack.onDialogCallBack(buttonView, COLOR_COLORIZE, 0, isChecked);
                }
            }
        });*/
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
//            mCheckBoxGray.setChecked(false);
            mTvScale.setText("x " + AppConstant.CONFIG_SCALE);
//            mTvGrayColor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
//            mTvOriginalColor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            ((RadioButton)mDataBingding.rgColor.findViewWithTag(DATA.ORIGINAL)).setChecked(true);
        });
       /* mCheckBox2Colors.setOnClickListener(v-> {
            updateCheckBoxStatus(mCheckBox2Colors);
        });
        mCheckBoxGray.setOnClickListener(v-> {
            updateCheckBoxStatus(mCheckBoxGray);
        });
        mCheckBoxReverse.setOnClickListener(v-> {
            updateCheckBoxStatus(mCheckBoxReverse);
        });
        mCheckBoxBorder.setOnClickListener(v-> {
            updateCheckBoxStatus(mCheckBoxBorder);
        });
        mCheckBoxCompare.setOnClickListener(v-> {
            updateCheckBoxStatus(mCheckBoxCompare);
        });
        mCheckboxColorize.setOnClickListener(v-> {
            updateCheckBoxStatus(mCheckboxColorize);
        });
        mTvOriginalColor.setOnClickListener(v-> {
            mTvOriginalColor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            mTvGrayColor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
            ConfigBean.getInstance().setGrayShow(false);
            if (mDialogCallBack != null) {
                mDialogCallBack.onDialogCallBack(mTvOriginalColor, COLOR_GRAY, 0, false);
            }
        });
        mTvGrayColor.setOnClickListener(v-> {
            mTvGrayColor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            mTvOriginalColor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
            ConfigBean.getInstance().setGrayShow(true);
            if (mDialogCallBack != null) {
                mDialogCallBack.onDialogCallBack(mTvOriginalColor, COLOR_GRAY, 0, true);
            }
        });

        mTvGrayColor.setOnClickListener(v-> {
            mTvGrayColor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            mTvOriginalColor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
            ConfigBean.getInstance().setGrayShow(true);
            if (mDialogCallBack != null) {
                mDialogCallBack.onDialogCallBack(mTvOriginalColor, COLOR_GRAY, 0, true);
            }
        });*/

    }

       private void initList() {
        mList.clear();
/*        mList.add(mCheckBox2Colors);
        mList.add(mCheckBoxGray);
        mList.add(mCheckBoxCompare);
        mList.add(mCheckboxColorize);
        mList.add(mCheckBoxBorder);
        mList.add(mCheckBoxReverse);*/
    }

    private void updateCheckBoxStatus(CheckBox originalCheckBox) {
        for(CheckBox checkBox : mList) {
            if (checkBox.getId() == originalCheckBox.getId()) {
                checkBox.setChecked(originalCheckBox.isChecked());
            } else {
                checkBox.setChecked(false);
            }
        }
    }

    /**
     * 显示当前的分辨率
     * @param resolution
     */
    public void setResolution(String resolution) {
        mTvResolution.setText(resolution);
    }

    public void updateBrightness(int progress) {
        mSeekBarBrightness.setProgress(progress);
        mTvBrightness.setText(progress + "%");
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
