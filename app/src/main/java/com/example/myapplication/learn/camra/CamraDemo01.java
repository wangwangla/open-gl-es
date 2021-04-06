package com.example.myapplication.learn.camra;

import android.opengl.GLES20;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CamraDemo01 extends BaseGameScreen {

    private String fshader = "precision mediump float;\n" +
            "varying vec2 textureCoordinate;\n" +
            "uniform sampler2D vTexture;\n" +
            "void main() {\n" +
            "    vec4 color=texture2D( vTexture, textureCoordinate);\n" +
            "    float rgb=color.g;\n" +
            "    vec4 c=vec4(rgb,rgb,rgb,color.a);\n" +
            "    gl_FragColor = c;\n" +
            "}";
    private String vShader = "attribute vec4 vPosition;\n" +
            "attribute vec2 vCoord;\n" +
            "uniform mat4 vMatrix;\n" +
            "\n" +
            "varying vec2 textureCoordinate;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_Position = vMatrix*vPosition;\n" +
            "    textureCoordinate = vCoord;\n" +
            "}";

    @Override
    public void create() {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(color.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        colorBuffer = byteBuffer.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
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
