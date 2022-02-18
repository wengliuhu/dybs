package com.artheia.usbcamera.bean;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/20
 * Describe
 */
public class DATA
{

    public static final int ORIGINAL = 0;//原始
    public static final int LIGHT_TEA = 1;//浅茶色
    public static final int DARK_TEA = 2;//深茶色
    public static final int BRIGHT_YELLOW = 3;//亮黄色
    public static final int REVERSE = 4;//反色
    public static final int GRAY = 5;//灰度

    @IntDef({ORIGINAL,  LIGHT_TEA, DARK_TEA, BRIGHT_YELLOW, REVERSE, GRAY})
    @Retention(RetentionPolicy.SOURCE)//源码级别
    public @interface COLOR{}


}
