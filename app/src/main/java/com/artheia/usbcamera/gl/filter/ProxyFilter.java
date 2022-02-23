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

import java.nio.FloatBuffer;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022-02-22 14:15
 * Describe：
 */
public class ProxyFilter extends BaseFilter {

    private BaseFilter base;

    public ProxyFilter(BaseFilter filter) {
        super(null,"none","none");
        this.base=filter;
    }

    @Override
    protected void initBuffer() {

    }

    @Override
    protected void onCreate() {
        base.create();
    }

    @Override
    public void draw(int texId, float[] tex_matrix, int offset) {
        base.draw(texId, tex_matrix, offset);
    }

    @Override
    public int drawToTexture(int texId, float[] tex_matrix, int offset) {
        return base.drawToTexture(texId, tex_matrix, offset);
    }

    @Override
    public float[] getTextureMatrix() {
        return base.getTextureMatrix();
    }

    @Override
    public void setVertexMatrix(float[] matrix) {
        base.setVertexMatrix(matrix);
    }

    @Override
    public void setTextureMatrix(float[] matrix) {
        base.setTextureMatrix(matrix);
    }

    @Override
    public void setVertexBuffer(FloatBuffer vertexBuffer) {
        base.setVertexBuffer(vertexBuffer);
    }

    @Override
    public void setTextureBuffer(FloatBuffer textureBuffer) {
        base.setTextureBuffer(textureBuffer);
    }

    @Override
    protected void onSetExpandData(int texId, float[] tex_matrix, int offset) {
        base.onSetExpandData(texId, tex_matrix, offset);
    }

    @Override
    protected void onDraw(int texId) {
        base.onDraw(texId);
    }

    @Override
    protected void onBindTexture(int textureId) {
        base.onBindTexture(textureId);
    }

    @Override
    protected void onClear() {
        base.onClear();
    }

    @Override
    protected void onSizeChanged(int width, int height) {
        base.onSizeChanged(width, height);
    }

    @Override
    protected void onUseProgram() {
        base.onUseProgram();
    }

    @Override
    public void runOnGLThread(Runnable runnable) {
        base.runOnGLThread(runnable);
    }

    @Override
    protected void shaderNeedTextureSize(boolean need) {
        base.shaderNeedTextureSize(need);
    }

    @Override
    public void destroy() {
        base.destroy();
    }

    @Override
    public void setTextureCo(float[] textureCo) {
        base.setTextureCo(textureCo);
    }

    @Override
    public void setVertexCo(float[] vertexCo) {
        base.setVertexCo(vertexCo);
    }

    @Override
    public float[] getVertexMatrix() {
        return base.getVertexMatrix();
    }

    @Override
    public void sizeChanged(int width, int height) {
        base.sizeChanged(width, height);
    }

}
