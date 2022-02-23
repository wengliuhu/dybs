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
package com.artheia.usbcamera.gl;


import android.graphics.SurfaceTexture;

import com.artheia.usbcamera.gl.core.Renderer;
import com.artheia.usbcamera.gl.filter.OesFilter;
import com.artheia.usbcamera.gl.utils.GpuUtils;
import com.serenegiant.glutils.GLHelper;

/**
 * WrapRenderer 用于包装其他Filter渲染OES纹理
 *
 * @author wuwang
 * @version v1.0 2017:10:27 08:53
 */
public class WrapRenderer implements Renderer
{

    private Renderer mRenderer;
    private OesFilter mFilter;

    public static final int TYPE_MOVE=0;
    public static final int TYPE_CAMERA=1;
    private final int mTexTarget;

    public WrapRenderer(Renderer renderer){
        this.mRenderer=renderer;
        mFilter=new OesFilter();
        mTexTarget = '赥';
        setFlag(TYPE_CAMERA);
    }

    public int createTexId(int oldTexId){
        if (oldTexId > 0)
            GLHelper.deleteTex(oldTexId);
        return GLHelper.initTex(this.mTexTarget, 9728);
//        SurfaceTexture temp = new SurfaceTexture(0);
//        EglHelper helper = new EglHelper();
//        helper.createGLESWithSurface(new EGLConfigAttrs(),new EGLContextAttrs(),temp);
//        return GpuUtils.createTextureID(true);
    }

    public void setFlag(int flag){
        if(flag==TYPE_MOVE){
            mFilter.setVertexCo(new float[]{
                    -1.0f,1.0f,
                    -1.0f,-1.0f,
                    1.0f,1.0f,
                    1.0f,-1.0f,
            });
        }else if(flag==TYPE_CAMERA){
     /*       mFilter.setVertexCo(new float[]{
                    -1.0f, -1.0f,
                    1.0f, -1.0f,
                    -1.0f, 1.0f,
                    1.0f, 1.0f,
            });*/
            mFilter.setVertexCo(new float[]{
                    -1.0f, -1.0f,
                    -1.0f, 1.0f,
                    1.0f, -1.0f,
                    1.0f, 1.0f,
            });
        }
    }

    public float[] getTextureMatrix(){
        return mFilter.getTextureMatrix();
    }

    @Override
    public void create() {
        mFilter.create();
        if(mRenderer!=null){
            mRenderer.create();
        }
    }

    @Override
    public void sizeChanged(int width, int height) {
        mFilter.sizeChanged(width, height);
        if(mRenderer!=null){
            mRenderer.sizeChanged(width, height);
        }
    }


    @Override
    public void draw(int texId, float[] tex_matrix, int offset)
    {
        if(mRenderer!=null){
            mRenderer.draw(mFilter.drawToTexture(texId, tex_matrix, offset), tex_matrix, offset);
        }else{
            mFilter.draw(texId, tex_matrix, offset);
        }
    }

    @Override
    public void destroy() {
        if(mRenderer!=null){
            mRenderer.destroy();
        }
        mFilter.destroy();
    }

    public Renderer getRenderer()
    {
        return mRenderer;
    }
}

