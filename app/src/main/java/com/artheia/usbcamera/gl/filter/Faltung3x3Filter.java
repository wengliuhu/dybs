package com.artheia.usbcamera.gl.filter;

import android.content.res.Resources;
import android.opengl.GLES20;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022-02-22 14:13
 * Describe：
 */
public class Faltung3x3Filter extends BaseFilter {

    private float[] mFaltung;
    private int mGLFaltung;

    public static final float[] FILTER_SHARPEN=new float[]{0,-1,0,-1,5,-1,0,-1,0};
    public static final float[] FILTER_BORDER=new float[]{0,1,0,1,-4,1,0,1,0};
    public static final float[] FILTER_CAMEO=new float[]{2,0,2,0,0,0,3,0,-6};

    public Faltung3x3Filter(Resources res,float[] faltung){
        super(res,"shader/base.vert","shader/func/faltung3x3.frag");
        shaderNeedTextureSize(true);
        this.mFaltung=faltung;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mGLFaltung= GLES20.glGetUniformLocation(mGLProgram,"uFaltung");
    }

    @Override
    protected void onSetExpandData(int texId, float[] tex_matrix, int offset) {
        super.onSetExpandData(texId, tex_matrix, offset);
        GLES20.glUniformMatrix3fv(mGLFaltung,1,false,mFaltung,0);
    }
}
