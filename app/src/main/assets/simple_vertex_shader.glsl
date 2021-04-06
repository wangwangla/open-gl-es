
uniform mat4 u_Matrix;
uniform float u_Time;                   //当前系统的时间

attribute vec3 a_Position;
attribute vec3 a_Color;
attribute vec3 a_DirectionVector;
attribute float a_ParticleStartTime;        //例子创建的时间


varying float v_ElapseTime;
varying vec3 v_Color;

void main() {
    v_Color=a_Color;
    v_ElapseTime=u_Time-a_ParticleStartTime;
    vec3 currentPosition=a_Position+(a_DirectionVector*v_ElapseTime);

    gl_Position=u_Matrix*vec4(currentPosition,1.0);
    gl_PointSize=10.0;

}