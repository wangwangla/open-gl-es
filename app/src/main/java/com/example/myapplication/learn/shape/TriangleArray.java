package com.example.myapplication.learn.shape;

import android.opengl.GLES20;

import com.example.myapplication.learn.shape.base.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 将颜色和顶点放在同一个数组区域的
 */
public class TriangleArray extends Shape {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec4 vColor;" +
                    "varying vec4  color;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    " color = vColor;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 color;" +
                    "void main() {" +
                    "  gl_FragColor = color;" +
                    "}";

    static final int COORDS_PER_VERTEX = 3;
    private FloatBuffer vertexBuffer;
    static float triangleCoords[] = {
            0.5f,  0.5f, 0.0f,1.0F,1.0F,1.0F, // top
            -0.5f, -0.5f, 0.0f,1.0F,1.0F,1.0F, // bottom left
            0.5f, -0.5f, 0.0f,1.0F,1.0F,1.0F  // bottom right
    };
    private int mProgram ;
    //顶点个数
    private final int vertexCount = triangleCoords.length / (COORDS_PER_VERTEX+3);
    //顶点之间的偏移量
    private final int vertexStride =( COORDS_PER_VERTEX+3) * 4; // 每个顶点四个字节
    //设置颜色，依次为红绿蓝和透明通道
    private int mPositionHandle;
    private int mColorHandle;
    float d = 0.01F;
    public TriangleArray(){

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
//        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glBindAttribLocation(mProgram,mPositionHandle,"vPosition");
//        上面两句的效果是一样的
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");

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
        vertexBuffer.position(0);
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
        GLES20.glEnableVertexAttribArray(mColorHandle);
        //准备三角形的坐标数据
//        GLES20.glVer
        vertexBuffer.position(3);
        GLES20.glVertexAttribPointer(
                mColorHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer);
        //获取片元着色器的vColor成员的句柄



        //设置绘制三角形的颜色
//        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);

    }

}
