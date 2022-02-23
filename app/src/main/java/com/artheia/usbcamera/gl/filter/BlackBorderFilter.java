package com.artheia.usbcamera.gl.filter;

import android.content.res.Resources;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/22 14:10
 * Describe：黑色描边
 */
public class BlackBorderFilter extends GroupFilter
{
    public BlackBorderFilter(Resources resource) {
        super(resource);
    }

    @Override
    protected void initBuffer() {
        super.initBuffer();
        addFilter(new GrayFilter(mRes));
        addFilter(new BaseFuncFilter(mRes,BaseFuncFilter.FILTER_GAUSS));
        addFilter(new BaseFuncFilter(mRes,BaseFuncFilter.FILTER_SOBEL_REVERSE));
    }
}
