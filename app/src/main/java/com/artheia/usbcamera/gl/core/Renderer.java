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
package com.artheia.usbcamera.gl.core;

import java.io.Serializable;

/**
 * Renderer
 *
 * @author wuwang
 * @version v1.0 2017:10:31 11:40
 */
/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022-02-10 10:11
 * Describe：渲染接口，渲染的四个接口应该在同一个GL线程中调用
 */
public interface Renderer extends Serializable
{

    /**
     * 创建
     */
    void create();

    /**
     * 大小改变
     * @param width 宽度
     * @param height 高度
     * @return 返回生成的textId
     */
    void sizeChanged(int width, int height);

    /**
     * 渲染
     * @param texId 输入纹理
     */
    void draw(int texId, float[] tex_matrix, int offset);

    /**
     * 销毁
     */
    void destroy();

}

