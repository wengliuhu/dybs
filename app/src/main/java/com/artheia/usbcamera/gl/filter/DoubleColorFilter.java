package com.artheia.usbcamera.gl.filter;

import android.content.res.Resources;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/23 14:18
 * Describe：两色
 */
public class DoubleColorFilter extends GroupFilter
{
    static final String FILTER_RED_GREEN="filter/red_green_fg.sh";

    public DoubleColorFilter(Resources resource) {
        super(resource);
    }

    @Override
    protected void initBuffer() {
        super.initBuffer();
        addFilter(new BaseFuncFilter(mRes,BaseFuncFilter.FILTER_GAUSS));
        addFilter(new BaseFuncFilter(mRes, FILTER_RED_GREEN));

    }
}
