precision highp float;
uniform mat4 uMVPMatrix;
varying vec2 comcoord;

void main()
{
  gl_FragColor = vec4(0.5, (comcoord.x+1.0)/2.0, 1.0-(comcoord.y+1.0)/2.0, 1);
}