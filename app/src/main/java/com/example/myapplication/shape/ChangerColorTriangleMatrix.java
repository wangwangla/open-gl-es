package com.example.myapplication.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//颜色变化
public class ChangerColorTriangleMatrix extends Shape{
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;" +
                    "varying vec4 vColor;" +
                    "attribute vec4 aColor;" +
                    "void main() {" +
                    "  gl_Position = vMatrix*vPosition;" +
                    "vColor = aColor;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    static final int COORDS_PER_VERTEX = 3;
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    static float triangleCoords[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };
    private int mProgram ;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    //设置颜色，依次为红绿蓝和透明通道
//    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f ,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };
    private int mPositionHandle;
    private int mColorHandle;
    private int mMatrixHandler;


    public void create(){
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
    }



    //相机位置
    private float[] mViewMatrix=new float[16];
    //透视
    private float[] mProjectMatrix=new float[16];
    //变换矩阵
    private float[] mMVPMatrix=new float[16];


    public void surfaceChange(int width,int height){
        float ratio=(float)width/height;
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,7);
        Matrix.setLookAtM(mViewMatrix, 0,
                0, 0, 7.0f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }


    @Override
    public void render() {
        //程序加入到环境里面
        GLES20.glUseProgram(mProgram);
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //设置
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //获取位置句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        //获取片元着色器的vColor成员的句柄
//        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        mColorHandle = GLES20.glGetAttribLocation(mProgram,"aColor");
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,
                0,colorBuffer);
        //设置绘制三角形的颜色
//        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
