package com.artheia.usbcamera.gl.filter;

import android.content.res.Resources;
import android.opengl.GLES20;

/**
 * @author : kimweng
 * @version : 0.1
 * @since :
 * Describe：
 */
public class HueFilter extends BaseFuncFilter{
    private float hue;
    private int hueLocation;
    private float hueAdjust;
    public HueFilter(Resources resource, float colorHue) {
        super(resource, "filter/hue_fragment.sh");
//        setHue(60.0f);
        hue = colorHue;
        hueAdjust = (this.hue % 360.0f) * (float) Math.PI / 180.0f;
    }

    @Override
    protected void onCreate() {
        setVertexCo(new float[]{
                -1.0f, 1.0f,
                -1.0f, -1.0f,
                1.0f, 1.0f,
                1.0f, -1.0f,
        });
        super.onCreate();
        hueLocation = GLES20.glGetUniformLocation(mGLProgram, "hueAdjust");
    }

    @Override
    protected void onBindTexture(int textureId) {
        super.onBindTexture(textureId);
//        float hueAdjust = (this.hue % 360.0f) * (float) Math.PI / 180.0f;
//        GLES20.glUniform1f(hueLocation,hueAdjust);
    }

    @Override
    protected void onSetExpandData(int texId, float[] tex_matrix, int offset) {
        super.onSetExpandData(texId, tex_matrix, offset);
//        float hueAdjust = (this.hue % 360.0f) * (float) Math.PI / 180.0f;
        GLES20.glUniform1f(hueLocation,hueAdjust);
    }
}
