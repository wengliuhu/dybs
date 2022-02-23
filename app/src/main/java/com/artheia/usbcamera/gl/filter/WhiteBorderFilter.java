package com.artheia.usbcamera.gl.filter;

import android.content.res.Resources;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022-02-22 14:09
 * Describe：白色描边
 */
public class WhiteBorderFilter extends GroupFilter {

    public WhiteBorderFilter(Resources resource) {
        super(resource);
    }

    @Override
    protected void initBuffer() {
        super.initBuffer();
        addFilter(new GrayFilter(mRes));
        addFilter(new BaseFuncFilter(mRes,BaseFuncFilter.FILTER_GAUSS));
        addFilter(new BaseFuncFilter(mRes,BaseFuncFilter.FILTER_SOBEL));

    }
}
