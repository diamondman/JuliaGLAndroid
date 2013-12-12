precision highp float;
uniform vec2 c;
varying vec2 ccoord;

void main()
{
  vec2 a = vec2((ccoord.x*ccoord.x - ccoord.y*ccoord.y + c.x), 2.0*ccoord.x*ccoord.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  a = vec2((a.x*a.x - a.y*a.y + c.x), 2.0*a.x*a.y + c.y);
  float length = sqrt(a.x*a.x+a.y*a.y);
  bool valid = length<(5.0/2.0);
  gl_FragColor = vec4((a.x+2.0)/4.0, (a.y+2.0)/4.0, 1*int(valid), 1);
}