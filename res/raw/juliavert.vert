attribute vec2 vPosition;
uniform mat4 uMVPMatrix;
uniform float utime;
varying vec2 comcoord;
void main()
{
  gl_Position = uMVPMatrix*vec4(vPosition, 0.0, 1.0);
  comcoord = vec2(vPosition.xy);
}