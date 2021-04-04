package com.example.myapplication.learn.blend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Blend02 extends BaseGameScreen {
    private int mProgram;
    private int glHPosition;
    private int glHTexture;
    private int glHCoordinate;
    private int vMatrix;
    private Bitmap mBitmap;
    private FloatBuffer bPos;
    private FloatBuffer bCoord;
    //相机位置
    private float[] mViewMatrix=new float[16];
    //透视
    private float[] mProjectMatrix=new float[16];
    //变换矩阵
    private float[] mMVPMatrix=new float[16];



    private final float[] sPos={
            -1.0f,1.0f,
            -1.0f,-1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };
    private float[] mModelMatrix = new float[16];
    private final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };
    private String vertexShaderCode =
            "attribute vec4 vPosition;\n" +      //位置
                    "attribute vec2 vCoordinate;\n" +    // 纹理
                    "varying vec2 aCoordinate;\n" +
                    "uniform mat4 vMatrix;" +      //  传递纹理   片段着色器
                    "void main(){\n" +
                    "    gl_Position=vPosition * vMatrix;\n" +
                    "    aCoordinate=vCoordinate;\n" +
                    "}";
    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform sampler2D vTexture;\n" +
                    "varying vec2 aCoordinate;\n" +
                    "void main(){\n" +
                    "    vec4 nColor=texture2D(vTexture,aCoordinate);\n"+
                    "    gl_FragColor=nColor;" +
                    "}";


    private Context context;
    public Blend02(Context context){
        this.context = context;
        ByteBuffer bb=ByteBuffer.allocateDirect(sPos.length*4);
        bb.order(ByteOrder.nativeOrder());
        bPos=bb.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);
        ByteBuffer cc=ByteBuffer.allocateDirect(sCoord.length*4);
        cc.order(ByteOrder.nativeOrder());
        bCoord=cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }

//    private int vChangeColor;

    public void preProgram(){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    private int[] createTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture;
        }
        return null;
    }

    @Override
    public void render() {

        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexture, 0);
        int []texture = createTexture();
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mMVPMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES20.glDisableVertexAttribArray(glHPosition);
        GLES20.glDisableVertexAttribArray(glHCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            GLES20.glDeleteTextures(1, texture, 0);
        }


    }

    @Override
    public void create() {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        preProgram();
        glHPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHCoordinate=GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        glHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
        vMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        try {
            mBitmap= BitmapFactory.decodeStream(context.getAssets().open("texture/texture02.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0,0,width,height);
        float sWidthHeight=width/(float)height;
//        Matrix.perspectiveM(mProjectMatrix,0,45,sWidthHeight,3,8);
//        Matrix.orthoM(mProjectMatrix,0,,sWidthHeight,3,8);
        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1/sWidthHeight, 1/sWidthHeight,3, 7);
        //设置相机位置

        //正交无论怎样移动大小是不变的
        Matrix.setLookAtM(mViewMatrix,0,
                0,0,7,0,0,0,
                0,1,0);

        Matrix.setIdentityM(mModelMatrix, 0);

//        rotation(2,0,0,1);
        transform(0,0,0);
//        transform(1,0,0);

//        Matrix.translateM(mModelMatrix,0,0,0,-5);
//        Matrix.rotateM(mModelMatrix, 0, 80, 0.0f, 0f, 1.0f);
//        Matrix.scaleM(mModelMatrix,0,0.5F,0.5F,0.5F);
    }

    public void transform(float x,float y,float z){
        mModelMatrix[3] = x;
        mModelMatrix[7] = y;
        mModelMatrix[11] = z;
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mModelMatrix,0);
        Matrix.multiplyMM(mMVPMatrix,0,mViewMatrix,0,mMVPMatrix,0);
    }

    public void rotation(float angle,float x,float y,float z){
//
//        if (x != 0) {
//            mModelMatrix[5] *= (float) Math.cos(angle);
//            mModelMatrix[6] *= -(float) Math.sin(angle);
//            mModelMatrix[9] *= (float) Math.sin(angle);
//            mModelMatrix[10] *= (float) Math.cos(angle);
//        }
//        if (y!=0){
//            mModelMatrix[0] *= (float) Math.cos(angle);
//            mModelMatrix[2] *= (float) Math.sin(angle);
//            mModelMatrix[8] *= -(float) Math.sin(angle);
//            mModelMatrix[10] *= (float) Math.cos(angle);
//        }
//        if (z!=0){
//            mModelMatrix[0] *= (float) Math.cos(angle);
//            mModelMatrix[1] *= -(float) Math.sin(angle);
//            mModelMatrix[4] *= (float) Math.sin(angle);
//            mModelMatrix[5] *= (float) Math.cos(angle);
//        }

        Matrix.rotateM(mModelMatrix,0,angle,x,y,z);
    }

    public void scale(float x,float y){
        mModelMatrix[0] *= (float) Math.cos(x);
        mModelMatrix[5] *= (float) Math.cos(y);
    }

    @Override
    public void dispose() {

    }
}
