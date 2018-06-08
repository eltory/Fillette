uniform vec2 uAspectRatio;
uniform vec2 uAspectRatioPreview;
attribute vec2 aPosition;

varying vec2 vTextureCoord;

void main() {
    gl_Position = vec4(aPosition, 0.0, 1.0);
    gl_Position.xy *= uAspectRatio / uAspectRatioPreview;
    vTextureCoord = (aPosition + 1.0) * 0.5;
}