package com.example.myapplication.base.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.myapplication.learn.shape.base.Shape;

//颜色变化
public class ChangerColorTriangleMatrix extends Shape {
    //相机位置
    private float[] mViewMatrix=new float[16];
    //透视
    private float[] mProjectMatrix=new float[16];
    //变换矩阵
    private float[] mMVPMatrix=new float[16];
    private int mPositionHandle;
    private int mColorHandle;
    private int mMatrixHandler;
    static final int COORDS_PER_VERTEX = 3;
    //顶点个数
    private final int vertexCount ;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    //设置颜色，依次为红绿蓝和透明通道
//    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public ChangerColorTriangleMatrix(){
        vertexShaderCode =
                "attribute vec4 vPosition;" +
                        "uniform mat4 vMatrix;" +
                        "varying vec4 vColor;" +
                        "attribute vec4 aColor;" +
                        "void main() {" +
                        "  gl_Position = vMatrix*vPosition;" +
                        "vColor = aColor;" +
                        "}";

        fragmentShaderCode =
                "precision mediump float;" +
                        "varying vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";
        float colorTemp[] = {
                0.0f, 1.0f, 0.0f, 1.0f ,
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f
        };
         float triangleCoordsTemp[] = {
                1f,  1f, 0.0f, // top
                -1f, -1f, 0.0f, // bottom left
                1f, -1f, 0.0f  // bottom right
         };
         color = colorTemp;triangleCoords = triangleCoordsTemp;
        vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    }

    public void create(){
        super.create();
    }

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
    public void dispose() {

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
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
