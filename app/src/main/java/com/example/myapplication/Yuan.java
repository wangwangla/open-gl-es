package com.example.myapplication;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.myapplication.learn.shape.base.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class Yuan extends Shape{

    private final FloatBuffer vertexBuffer;
    private int uMatrixLocation;
    private final float[] mMatrix = new float[16];

    /**
     * 顶点着色器
     */
    private String vertextShader =
            "#version 300 es\n" +
                    "layout (location = 0) in vec4 vPosition;\n" +
                    "uniform mat4 u_Matrix;\n" +
                    "void main() {\n" +
                    "     gl_Position  = u_Matrix * vPosition;\n" +
                    "     gl_PointSize = 10.0;\n" +
                    "}\n";

    /**
     * 片段着色器
     */
    private String fragmentShader =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "out vec4 fragColor;\n" +
                    "void main() {\n" +
                    "     fragColor = vec4(1.0,1.0,1.0,1.0);\n" +
                    "}\n";


    public Yuan() {
        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(createPositions().length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        //传入指定的坐标数据
        vertexBuffer.put(createPositions());
        vertexBuffer.position(0);
    }

    @Override
    public void create() {
        onSurfaceCreated();
    }

    @Override
    public void render() {
        onDrawFrame();
    }

    @Override
    public void surfaceChange(int width, int height) {
        onSurfaceChanged(width,height);
    }

    @Override
    public void dispose() {

    }


    public void onSurfaceCreated() {

        final int vertexShaderId = loadShader(GLES20.GL_VERTEX_SHADER, vertextShader);
        final int fragmentShaderId = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        //在OpenGLES环境中使用程序片段
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShaderId);
        GLES20.glAttachShader(mProgram,fragmentShaderId);
        GLES20.glLinkProgram(mProgram);
        GLES20.glUseProgram(mProgram);
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
    }

    public void onSurfaceChanged(int width, int height) {
//        GLES20.glViewport(0, 0, width, height);
        float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
        if (width > height) {
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 0f, 10f);
        } else {
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 0f, 10f);
        }
    }



    public void onDrawFrame() {
        GLES20.glUseProgram(mProgram);
        //准备坐标数据
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
//        //启用顶点的句柄
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 362);

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(0);
    }


    private float[] createPositions() {
        // 绘制的半径
        float radius = 0.8f;
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);             //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        float angDegSpan = 360f / 360;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (radius * Math.sin(i * Math.PI / 180f)));
            data.add((float) (radius * Math.cos(i * Math.PI / 180f)));
            data.add(0.0f);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }
}