attribute vec2 vPosition;
uniform mat4 uMVPMatrix;
uniform float utime;
varying vec2 comcoord;
void main()
{
  gl_Position = uMVPMatrix*vec4(vPosition.x+(cos(6.0*utime+vPosition.y*100.0)*sin(utime+vPosition.x*100.0))/2.0, 
  	                            vPosition.y+(cos(utime+vPosition.x*100.0)*sin(5.0*utime+vPosition.y*100.0))/2.0, 0.0, 1.0);
  comcoord = vec2(vPosition.xy);
}