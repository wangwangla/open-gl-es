package com.example.myapplication.learn.camra;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLES30;

import com.example.myapplication.learn.shape.base.Shape;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class CamraDemo extends Shape {
    private final FloatBuffer vertexBuffer, mTexVertexBuffer;
    private final ShortBuffer mVertexIndexBuffer;
    private int mProgram;
    private int textureId;
    private float[] POSITION_VERTEX = new float[]{
            0f, 0f, 0f,     //顶点坐标V0
            1f, 1f, 0f,     //顶点坐标V1
            -1f, 1f, 0f,    //顶点坐标V2
            -1f, -1f, 0f,   //顶点坐标V3
            1f, -1f, 0f     //顶点坐标V4
    };
    /**
     * 纹理坐标
     * (s,t)
     */
    private static final float[] TEX_VERTEX = {
            0.5f, 0.5f, //纹理坐标V0
            1f, 1f,     //纹理坐标V1
            0f, 1f,     //纹理坐标V2
            0f, 0.0f,   //纹理坐标V3
            1f, 0.0f    //纹理坐标V4
    };

    /**
     * 索引
     */
    private static final short[] VERTEX_INDEX = {
            0, 1, 2,  //V0,V1,V2 三个顶点组成一个三角形
            0, 2, 3,  //V0,V2,V3 三个顶点组成一个三角形
            0, 3, 4,  //V0,V3,V4 三个顶点组成一个三角形
            0, 4, 1   //V0,V4,V1 三个顶点组成一个三角形
    };
    /**
     * 相机ID
     */
    private int mCameraId;
    /**
     * 相机实例
     */
    private Camera mCamera;
    /**
     * Surface
     */
    private SurfaceTexture mSurfaceTexture;

    /**
     * 矩阵索引
     */
    private int uTextureSamplerLocation;

    public CamraDemo(){
       this.mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
       mCamera = Camera.open(mCameraId);
        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(POSITION_VERTEX.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        //传入指定的坐标数据
        vertexBuffer.put(POSITION_VERTEX);
        vertexBuffer.position(0);
        mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(TEX_VERTEX);
        mTexVertexBuffer.position(0);
        mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer().put(VERTEX_INDEX);
        mVertexIndexBuffer.position(0);
    }

    @Override
    public void create() {
        String vv = "#version 300 es\n" +
                "layout (location = 0) in vec4 vPosition;\n" +
                "layout (location = 1) in vec4 aTextureCoord;\n" +
                "//纹理矩阵\n" +
                "out vec2 yuvTexCoords;\n" +
                "void main() {\n" +
                "     gl_Position  = vPosition;\n" +
                "     gl_PointSize = 10.0;\n" +
                "     //只保留x和y分量\n" +
                "     yuvTexCoords = aTextureCoord;\n" +
                "}";
        String vvv = "#version 300 es\n" +
                "//OpenGL ES3.0外部纹理扩展\n" +
                "#extension GL_OES_EGL_image_external_essl3 : require\n" +
                "precision mediump float;\n" +
                "uniform samplerExternalOES yuvTexSampler;\n" +
                "in vec2 yuvTexCoords;\n" +
                "out vec4 vFragColor;\n" +
                "void main() {\n" +
                "     vFragColor = texture(yuvTexSampler,yuvTexCoords);\n" +
                "}";

        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //编译
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vv);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,vvv);
        //链接程序片段
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
        //程序加入到环境里面
        GLES20.glUseProgram(mProgram);

        //获取Shader中定义的变量在program中的位置
        uTextureSamplerLocation = GLES30.glGetUniformLocation(mProgram, "yuvTexSampler");

        //加载纹理
        textureId = loadTexture();
        //加载SurfaceTexture
        loadSurfaceTexture(textureId);
    }

    @Override
    public void render() {
        //使用程序片段
        GLES30.glUseProgram(mProgram);
        //更新纹理图像
        mSurfaceTexture.updateTexImage();
        //激活纹理单元0
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        //绑定外部纹理到纹理单元0
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        //将此纹理单元床位片段着色器的uTextureSampler外部纹理采样器
        GLES30.glUniform1i(uTextureSamplerLocation, 0);
        //将纹理矩阵传给片段着色器
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);

        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, mTexVertexBuffer);

        // 绘制
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_INDEX.length, GLES20.GL_UNSIGNED_SHORT, mVertexIndexBuffer);

    }

    @Override
    public void surfaceChange(int width, int height) {

    }

    @Override
    public void dispose() {

    }


    /**
     * 加载外部纹理
     * @return
     */
    public int loadTexture() {
        int[] tex = new int[1];
        //创建一个纹理
        GLES30.glGenTextures(1, tex, 0);
        //绑定到外部纹理上
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0]);
        //设置纹理过滤参数
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        //解除纹理绑定
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return tex[0];
    }

    public boolean loadSurfaceTexture(int textureId) {
        //根据纹理ID创建SurfaceTexture
        mSurfaceTexture = new SurfaceTexture(textureId);
        //设置SurfaceTexture作为相机预览输出
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        //开启相机预览
        mCamera.startPreview();
        return true;
    }
}
