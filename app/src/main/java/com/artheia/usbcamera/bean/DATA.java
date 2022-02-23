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
    public static final int WHITE_BORDER = 6;//白色描边
    public static final int BLACK_BORDER = 7;//黑色描边
    public static final int ALL_COLOR_GREEN_BORDER = 8;//全彩绿边
    public static final int ALL_COLOR_YELLOW_BORDER = 9;//全彩黄边

    @IntDef({ORIGINAL,  LIGHT_TEA, DARK_TEA, BRIGHT_YELLOW, REVERSE, GRAY, WHITE_BORDER, BLACK_BORDER, ALL_COLOR_GREEN_BORDER, ALL_COLOR_YELLOW_BORDER})
    @Retention(RetentionPolicy.SOURCE)//源码级别
    public @interface COLOR{}


}
