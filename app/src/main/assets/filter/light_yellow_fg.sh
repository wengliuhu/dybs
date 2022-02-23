#version 100
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTexture;
varying highp vec2 vTextureCo;

void main() {

 vec4 mask = texture2D(uTexture, vTextureCo);
    gl_FragColor = vec4(0.9020, 1.0, mask[2], 0);
}