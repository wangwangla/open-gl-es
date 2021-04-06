package com.example.myapplication.learn.particer;

import android.opengl.GLES20;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

public class ParticleRenderer extends BaseGameScreen {

    private String vvv = "\n" +
            "uniform mat4 u_Matrix;\n" +
            "uniform float u_Time;                   //当前系统的时间\n" +
            "\n" +
            "attribute vec3 a_Position;\n" +
            "attribute vec3 a_Color;\n" +
            "attribute vec3 a_DirectionVector;\n" +
            "attribute float a_ParticleStartTime;        //例子创建的时间\n" +
            "\n" +
            "\n" +
            "varying float v_ElapseTime;\n" +
            "varying vec3 v_Color;\n" +
            "\n" +
            "void main() {\n" +
            "    v_Color=a_Color;\n" +
            "    v_ElapseTime=u_Time-a_ParticleStartTime;\n" +
            "    vec3 currentPosition=a_Position+(a_DirectionVector*v_ElapseTime);\n" +
            "\n" +
            "    gl_Position=u_Matrix*vec4(currentPosition,1.0);\n" +
            "    gl_PointSize=10.0;\n" +
            "\n" +
            "}";
    private String fff = "precision mediump float;\n" +
            "\n" +
            "varying vec3 v_Color;\n" +
            "varying float v_ElapseTime;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "gl_FragColor=vec4(v_Color/v_ElapseTime,1.0);\n" +
            "\n" +
            "}";

    @Override
    public void create() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vvv);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fff);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
        int lin[] = new int[1];
        GLES20.glGetProgramiv(mProgram,GLES20.GL_LINK_STATUS,lin,0);
        if (lin[0] == 0){
            String s = GLES20.glGetProgramInfoLog(mProgram);
            System.out.println(s);
        }
        GLES20.glDeleteShader(1);
    }

    @Override
    public void render() {

    }

    @Override
    public void surfaceChange(int width, int height) {

    }

    @Override
    public void dispose() {

    }

}