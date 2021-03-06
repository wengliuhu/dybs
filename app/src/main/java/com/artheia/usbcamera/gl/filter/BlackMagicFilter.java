/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.artheia.usbcamera.gl.filter;

import android.content.res.Resources;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022-02-22 14:13
 * Describe：黑魔法滤镜，sobel算法实现
 */
public class BlackMagicFilter extends GroupFilter {

    public BlackMagicFilter(Resources resources){
        super(resources);
    }

    @Override
    protected void initBuffer() {
        super.initBuffer();
        addFilter(new GrayFilter(mRes));
        addFilter(new BaseFuncFilter(mRes, BaseFuncFilter.FILTER_GAUSS));
        addFilter(new BaseFuncFilter(mRes, BaseFuncFilter.FILTER_SOBEL));
    }
}

