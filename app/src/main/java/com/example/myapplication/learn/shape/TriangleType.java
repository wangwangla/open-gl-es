package com.example.myapplication.learn.shape;

import android.opengl.GLES20;

import com.example.myapplication.learn.shape.base.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TriangleType extends Shape {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    static final int COORDS_PER_VERTEX = 3;
    private FloatBuffer vertexBuffer;
    static float triangleCoords[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f,  // bottom right
            1,-0.5F,0F
//            ,
//            0.5f,  0.5f, 0.0f, // top
//            0.5f, -0.5f, 0.0f,  // bottom right


    };
    private int mProgram ;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    //设置颜色，依次为红绿蓝和透明通道
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    private int mPositionHandle;
    private int mColorHandle;
    float d = 0;
    public TriangleType(){

    }

    public void create(){
        ByteBuffer bb = ByteBuffer.allocateDirect(
                triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
        //程序加入到环境里面
        GLES20.glUseProgram(mProgram);
//        检查是否有效
        GLES20.glValidateProgram(mProgram);
        //得到活跃的unifor
        int arr[] = new int[1];
        int arr1[] = new int[1];
        int arr2[] = new int[1];
        int arr3[] = new int[1];
        byte arr4[] = new byte[10];
        GLES20.glGetProgramiv(mProgram,GLES20.GL_ACTIVE_UNIFORMS,arr,0);
        GLES20.glGetActiveUniform(
                mProgram,
                1,
                1,
                arr1,
                1,
                arr2,
                1,
                arr3,
                1,
                arr4,
                0
                );
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    }

    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void dispose() {

    }


    @Override
    public void render() {
        //获取位置句柄   属性句柄

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(
                mPositionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer);
        //获取片元着色器的vColor成员的句柄

        color[1] = color[1]-d;
        if (color[1]<=0||color[1]>=1){
            d=-d;
        }
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glLineWidth(3);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
