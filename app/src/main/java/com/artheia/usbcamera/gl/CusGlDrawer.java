package com.artheia.usbcamera.gl;


import com.artheia.usbcamera.gl.core.Renderer;
import com.artheia.usbcamera.gl.filter.BaseFilter;
import com.artheia.usbcamera.gl.filter.GroupFilter;
import com.serenegiant.glutils.GLHelper;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/18 10:34
 * Describe： 用于组合滤镜显示
 */
public class CusGlDrawer {
//    private static final float[] VERTICES = new float[]{1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, -1.0F};
//    private static final float[] TEXCOORD = new float[]{1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F};
//    private static final int FLOAT_SZ = 4;
//    private int hProgram;
//    int maPositionLoc;
//    int maTextureCoordLoc;
//    int muMVPMatrixLoc;
//    int muTexMatrixLoc;
//    private final int VERTEX_NUM;
//    private final int VERTEX_SZ;
//    private final FloatBuffer pVertex;
//    private final FloatBuffer pTexCoord;
//    private final int mTexTarget;
//    private final float[] mMvpMatrix;

    private final int mTexTarget;

    private String fragmentFilePath;

    private WrapRenderer wrapRenderer;

    public WrapRenderer getWrapRenderer() {
        return wrapRenderer;
    }

    public void setWrapRenderer(WrapRenderer wrapRenderer) {
        this.wrapRenderer = wrapRenderer;
    }

    public CusGlDrawer(boolean isOES) {
//        this(isOES, "filter/default_fragment.sh");
        this.mTexTarget = isOES ? '赥' : 3553;
    }

    public void init(){
        wrapRenderer.create();
    }

    public boolean isOES() {
        return this.mTexTarget == 36197;
    }

    public void release() {
        wrapRenderer.destroy();
    }

    public synchronized void draw(int texId, float[] tex_matrix, int offset) {
        wrapRenderer.draw(texId, tex_matrix, offset);
    }

    public int initTex() {
        return GLHelper.initTex(this.mTexTarget, 9728);
    }

    public void deleteTex(int hTex) {
        GLHelper.deleteTex(hTex);
    }

    public synchronized void updateShader(Renderer renderer) {
        release();
        wrapRenderer = new WrapRenderer(renderer);
        wrapRenderer.create();
    }
}
