precision mediump float;

varying vec3 v_Color;
varying float v_ElapseTime;

void main() {

gl_FragColor=vec4(v_Color/v_ElapseTime,1.0);

}