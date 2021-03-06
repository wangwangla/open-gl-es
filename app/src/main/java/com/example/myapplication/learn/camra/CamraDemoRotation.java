package com.example.myapplication.learn.camra;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class CamraDemoRotation {
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
    private int uTextureMatrixLocation;
    private int uTextureSamplerLocation;

    public CamraDemoRotation(){
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

    private void setCameraDisplayOrientation(int cameraId, Camera camera) {
//        Activity targetActivity = (Activity) mGLSurfaceView.getContext();
//        android.hardware.Camera.CameraInfo info =
//                new android.hardware.Camera.CameraInfo();
//        android.hardware.Camera.getCameraInfo(cameraId, info);
//        int rotation = getDefaultDisplay()
//                .getRotation();
//        int degrees = 0;
//        switch (rotation) {
//            case Surface.ROTATION_0: degrees = 0; break;
//            case Surface.ROTATION_90: degrees = 90; break;
//            case Surface.ROTATION_180: degrees = 180; break;
//            case Surface.ROTATION_270: degrees = 270; break;
//        }
//
//        int result;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;  // compensate the mirror
//        } else {  // back-facing
//            result = (info.orientation - degrees + 360) % 360;
//        }
//        camera.setDisplayOrientation(result);
    }

    public void onSurfaceCreated() {
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

        uTextureMatrixLocation = GLES30.glGetUniformLocation(mProgram, "uTextureMatrix");
        //获取Shader中定义的变量在program中的位置
        uTextureSamplerLocation = GLES30.glGetUniformLocation(mProgram, "yuvTexSampler");

        //加载纹理
        textureId = loadTexture();
        //加载SurfaceTexture
        loadSurfaceTexture(textureId);
    }

    public int loadShader(int type, String shaderCode){
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void onDrawFrame(GL10 gl) {
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
