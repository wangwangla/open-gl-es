package com.example.myapplication.learn.transform;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;

/**
 * 三角形
 */
public class TriangleDemo01 extends BaseGameScreen {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vM;" +
                    "void main() {" +
                    "  gl_Position = vM*vPosition;" +
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
            0.5f, -0.5f, 0.0f  // bottom right
    };
    private int mProgram ;
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    //设置颜色，依次为红绿蓝和透明通道
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    private int mPositionHandle;
    private int vm;
    private int mColorHandle;
    float d = 0.01F;
    public TriangleDemo01(){

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
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        vm = GLES20.glGetUniformLocation(mProgram,"vM");
    }

    private float [] proj = new float[16];
    private float model [] = new float[16];
    private float vv[] = new float[16];
    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0,0,width,height);
        Matrix.setLookAtM(vv,0,0,0,3,0,0,0,0,1,0);
//        Matrix.orthoM(proj,0,-1,1,-1,1,3,7);
        Matrix.frustumM(proj,0,-1,1,-1,1,3,8);
        Matrix.setIdentityM(model,0);
        Matrix.rotateM(model,0,10,0,0,2);
        Matrix.translateM(model,0,0.4F,0,-1);
        finall = new float[16];
        Matrix.multiplyMM(finall,0,vv,0,model,0);
        Matrix.multiplyMM(finall,0,proj,0,finall,0);
    }
    float [] finall;

    @Override
    public void dispose() {

    }


    @Override
    public void render() {
        //获取位置句柄   属性句柄
        GLES20.glUniformMatrix4fv(vm,1,false,finall,0);
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
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
