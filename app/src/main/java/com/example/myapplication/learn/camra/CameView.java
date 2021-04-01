package com.example.myapplication.learn.camra;

import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.example.myapplication.learn.Drawwe;
import com.example.myapplication.learn.shape.base.Shape;

import javax.microedition.khronos.opengles.GL10;

public class CameView extends Shape {

    @Override
    public void create() {
        //创建一个纹理
        int textureId = createTextureId();
        //将纹理 和  surface相关联
        texture = new SurfaceTexture(textureId);
        //将纹理 传入  渲染中
        drawwe = new Drawwe(textureId);
        //打开相机   相机将预览画面放入到surface中
        CameraInterface.getInstance().doOpenCamera();
    }

    @Override
    public void render() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //从图像流中将纹理图像更新为最近的帧
        texture.updateTexImage();
        drawwe.draw();
    }

    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        //如果还未预览，就开始预览
        if(!CameraInterface.getInstance().isPreviewing()){
            CameraInterface.getInstance().doStartPreview(texture);
        }
    }

    @Override
    public void dispose() {

    }
    private int createTextureId() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

    private SurfaceTexture texture;
    private Drawwe drawwe;

    @Override
    public void resume() {
        System.out.println("resume........");
        CameraInterface.getInstance().doStopCamera();
    }

    @Override
    public void pause() {
        System.out.println("pause。。。。。。。。。。。。。。");
        CameraInterface.getInstance().doStartPreview(texture);
    }
}
