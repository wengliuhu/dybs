#version 100
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES sTexture;
varying highp vec2 vTextureCoord;

void main() {
      vec4 textureColor = texture2D(sTexture, vTextureCoord);
     gl_FragColor = vec4((1.0 - textureColor.rgb), textureColor.w);
}