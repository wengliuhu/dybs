precision highp float;
precision highp int;
attribute vec4 aPosition;
attribute vec4 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;

varying vec2 vTextureCoord;

void main(){
 gl_Position = uMVPMatrix * aPosition;
 vTextureCoord = (uTexMatrix * aTextureCoord).xy;
}