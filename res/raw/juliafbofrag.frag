precision highp float;
uniform vec2 c;
varying vec2 ccoord;

void main()
{
  vec2 a = vec2((ccoord.x*ccoord.x - ccoord.y*ccoord.y + c.x), 2.0*ccoord.x*ccoord.y + c.y);
  float length = sqrt(a.x*a.x+a.y*a.y);
  bool valid = length>2.0;
  gl_FragColor = vec4(1.0*float(valid), 0, 0, 1);
}