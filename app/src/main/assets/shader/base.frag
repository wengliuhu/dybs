precision mediump float;

uniform samplerExternalOES sTexture;
varying highp vec2 vTextureCoord;
void main() {
     gl_FragColor = texture2D(sTexture, vTextureCoord);
}