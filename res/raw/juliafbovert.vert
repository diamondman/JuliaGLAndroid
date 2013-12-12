attribute vec2 vPosition;
uniform mat4 uMVPMatrix;
varying vec2 ccoord;

void main()
{
  gl_Position = uMVPMatrix*vec4(vPosition, 0.0, 1.0);
  ccoord = vec2(vPosition.xy);
}