#version 100
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTexture;
varying highp vec2 vTextureCo;

void main() {
gl_FragColor = texture2D( uTexture, vTextureCo);
}