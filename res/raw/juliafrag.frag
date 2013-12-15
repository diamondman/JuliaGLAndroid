precision highp float;
varying vec2 comcoord;
uniform sampler2D complex_map;

void main()
{
  gl_FragColor = texture2D(complex_map, comcoord);
}