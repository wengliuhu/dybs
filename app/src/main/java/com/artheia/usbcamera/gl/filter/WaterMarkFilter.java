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

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.artheia.usbcamera.gl.utils.GpuUtils;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022-02-22 14:17
 * Describe：
 */
public class WaterMarkFilter extends LazyFilter{

    private int[] viewPort=new int[4];
    private int[] markPort=new int[4];
    private final LazyFilter mark=new LazyFilter(){

        @Override
        protected void onClear() {

        }
    };
    private int markTextureId=-1;

    public WaterMarkFilter setMarkPosition(final int x, final int y, final int width, final int height){
        markPort[0]=x;
        markPort[1]=y;
        markPort[2]=width;
        markPort[3]=height;
        runOnGLThread(new Runnable() {
            @Override
            public void run() {
                mark.sizeChanged(width, height);
            }
        });
        return this;
    }

    public WaterMarkFilter setMark(final Bitmap bmp){
        runOnGLThread(new Runnable() {
            @Override
            public void run() {
                if(bmp!=null){
                    if(markTextureId==-1){
                        markTextureId= GpuUtils.createTextureID(false);
                    }else{
                        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,markTextureId);
                    }
                    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bmp,0);
                    bmp.recycle();
                }else{
                    if(markTextureId!=-1){
                        GLES20.glDeleteTextures(1,new int[]{markTextureId},0);
                    }
                }
            }
        });
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mark.create();
    }


    @Override
    protected void onDraw(int texId) {
        //todo change blend and viewport
        super.onDraw(texId);
//        if(markTextureId!=-1){
//            GLES20.glGetIntegerv(GLES20.GL_VIEWPORT,viewPort,0);
//            GLES20.glViewport(markPort[0],mHeight-markPort[3]-markPort[1],markPort[2],markPort[3]);
//
//            GLES20.glEnable(GLES20.GL_BLEND);
//            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
//            GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
//            // TODO: 2022/2/18 此处先不需要水印
//            mark.draw(markTextureId);
//            GLES20.glDisable(GLES20.GL_BLEND);
//
//            GLES20.glViewport(viewPort[0],viewPort[1],viewPort[2],viewPort[3]);
//        }
        //todo reset blend and view port
    }


}
