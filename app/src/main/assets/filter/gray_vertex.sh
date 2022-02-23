#version 100
uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;
attribute vec4 aVertexCo;
attribute vec2 aTextureCo;
varying highp vec2 vTextureCo;
void main() {
 gl_Position = uMVPMatrix * aVertexCo;
 vTextureCo = (uTexMatrix * aTextureCo).xy;
}