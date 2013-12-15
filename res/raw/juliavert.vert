attribute vec2 vPosition;
attribute vec2 texcoord;
varying vec2 comcoord;

void main()
{
  gl_Position = vec4(vPosition, 0.0, 1.0);
  comcoord = texcoord;
}