package com.artheia.usbcamera.utils;

import android.util.Log;

import com.artheia.usbcamera.bean.ConfigBean;
import com.artheia.usbcamera.bean.DATA;
import com.serenegiant.usb.widget.UVCCameraTextureView;

import java.util.ArrayList;
import java.util.List;

import static com.artheia.usbcamera.bean.DATA.BRIGHT_YELLOW;
import static com.artheia.usbcamera.bean.DATA.DARK_TEA;
import static com.artheia.usbcamera.bean.DATA.LIGHT_TEA;
import static com.artheia.usbcamera.bean.DATA.ORIGINAL;
import static com.artheia.usbcamera.bean.DATA.REVERSE;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/24 13:07
 * Describe：滤镜切换
 */
public class FilterHelper
{
    private List<Integer> filters;

    private static class SingleHolder{
        private final static FilterHelper mIntsance = new FilterHelper();
        private SingleHolder()
        {
        }
    }

    private FilterHelper()
    {
        filters = new ArrayList<Integer>();
        filters.add(ORIGINAL);
        filters.add(DATA.GRAY);
        filters.add(BRIGHT_YELLOW);
        filters.add(DARK_TEA);
        filters.add(LIGHT_TEA);
        filters.add(DATA.RED_GREEN);
        filters.add(REVERSE);
        filters.add(DATA.WHITE_BORDER);
        filters.add(DATA.BLACK_BORDER);
        filters.add(DATA.ALL_COLOR_GREEN_BORDER);
        filters.add(DATA.ALL_COLOR_YELLOW_BORDER);
    }

    public static FilterHelper getInstance(){
        return SingleHolder.mIntsance;
    }

    public boolean changeFilter(UVCCameraTextureView textureView, @DATA.COLOR int filter){
        Log.d("kim", "--------当前filter:" + filter);
        switch (filter){
            // 色彩部分
            case ORIGINAL:
            {
                textureView.changeOriginal();
                return true;
            }
            case DATA.GRAY:
            {
                textureView.changeGray();
                return true;
            }
            case BRIGHT_YELLOW:
            {
                textureView.changeBrightYellow();
                return true;
            }
            case REVERSE:
            {
                textureView.changeReverse();
                return true;
            }
            case DARK_TEA:
            {
                textureView.changeDarkTea();
                return true;
            }
            case LIGHT_TEA:
            {
                textureView.changeLightTea();
                return true;
            }
            case DATA.RED_GREEN:
            {
                textureView.changeRedGreen();
                return true;
            }
            case DATA.WHITE_BORDER:
            {
                textureView.changeWhiteBorder();
                return true;
            }
            case DATA.BLACK_BORDER:
            {
                textureView.changeBlackBorder();
                return true;
            }
            case DATA.ALL_COLOR_GREEN_BORDER:
            {
                textureView.changeColorGreenBorder();
                return true;
            }
            case DATA.ALL_COLOR_YELLOW_BORDER:
            {
                textureView.changeColorYellowBorder();
                return true;
            }
        }

        return false;
    }

    /**
     * 获取下个滤镜
     * @return
     */
    public  void changeNextFilter(UVCCameraTextureView textureView){
        int nowPosition = ConfigBean.getInstance().getFilterColor();
        int nextFilter = ++nowPosition % filters.size();
        ConfigBean.getInstance().setFilterColor(nextFilter );
        changeFilter(textureView, nextFilter);
    }
}
