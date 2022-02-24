#version 100
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTexture;
varying highp vec2 vTextureCo;

highp vec3 getColor(highp vec3 rgb) {
  float r = rgb[0];
  float g = rgb[1];
  float realR = r > g ? r : 0.0;
  float realG = g > r ? g : 0.0;
  return vec3(realR, realG, 0.0);
}

void main() {

 vec4 mask = texture2D(uTexture, vTextureCo);
      gl_FragColor = vec4(getColor(mask.rgb), mask[3]);
}