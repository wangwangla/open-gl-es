package com.example.myapplication.shape;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 不写, 再写没什么意义
 */
public class Square extends Shape{
    private final String vertexShaderCode = "" +
            "attribute vec4 cPosition;" +
            "void main(){" +
            "   gl_position = Position" +
            "}";

    private final String fragmentShaderCode = "" +
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main(){" +
            "   gl_FragColor = vColor" +
            "}";

    private int mProgram;

    static final int COORDS_PER_VERTEX = 3;

    static float square[] = {
        -0.5F,0.5F,0.0F,
        -0.5F,-0.5F,0.0F,
        0.5F,-0.5F,0.0F,
        0.5F,0.5F,0.0F,
    };

    FloatBuffer vertextBuffer;

    static short index[] = {
        0,1,2,0,2,3
    };

    @Override
    public void create() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);

        //准备数据
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(square.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertextBuffer = byteBuffer.asFloatBuffer();
        vertextBuffer.put(square);
        vertextBuffer.position(0);
    }

    @Override
    public void render() {
        GLES20.glUseProgram(mProgram);
        int mHandlePosition = GLES20.glGetAttribLocation(mProgram,"");
        GLES20.glEnableVertexAttribArray(mHandlePosition);
        GLES20.glVertexAttribPointer(mHandlePosition,COORDS_PER_VERTEX,GLES20.GL_FLOAT,
                false,12,vertextBuffer);
    }
}
