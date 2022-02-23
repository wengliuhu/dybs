package com.artheia.usbcamera.gl;


import com.artheia.usbcamera.application.MyApplication;
import com.artheia.usbcamera.gl.core.Renderer;
import com.artheia.usbcamera.gl.filter.WhiteBorderFilter;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/18 10:34
 * Describe： 用于组合滤镜显示
 */
public class CusGlDrawer {
    private WrapRenderer wrapRenderer;
    public CusGlDrawer(Renderer renderer) {
//        this(isOES, "filter/default_fragment.sh");
//        this.mTexTarget = isOES ? '赥' : 3553;
//        wrapRenderer = new WrapRenderer(new BaseFuncFilter(MyApplication.getAPP().getResources(), "filter/default_fragment.sh"));
        wrapRenderer = new WrapRenderer(renderer);
//        wrapRenderer = new WrapRenderer(new WhiteBorderFilter(MyApplication.getAPP().getResources()));
        wrapRenderer.create();
    }

    public void sizeChanged(int width, int height){
        wrapRenderer.sizeChanged(width, height);
    }

    public int createTexId(int oldTexId){
        return wrapRenderer.createTexId(oldTexId);
    }

    public void release() {
        wrapRenderer.destroy();
    }

    public synchronized void draw(int texId, float[] tex_matrix, int offset) {
        wrapRenderer.draw(texId, tex_matrix, offset);
    }

    public synchronized void updateShader(Renderer renderer, int width, int height) {
        wrapRenderer.destroy();
        wrapRenderer = new WrapRenderer(renderer);
        wrapRenderer.create();
        wrapRenderer.sizeChanged(width,height);
    }
}
