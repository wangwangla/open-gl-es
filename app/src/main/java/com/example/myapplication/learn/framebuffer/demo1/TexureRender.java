package com.example.myapplication.learn.framebuffer.demo1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TexureRender implements GLSurfaceView.Renderer {
    private BitmapFboTexture bitmapFboTexture;
    private BitmapRenderTexture bitmapRenderTexture;

    public TexureRender(Context context) {
        bitmapFboTexture = new BitmapFboTexture(context);
        bitmapRenderTexture = new BitmapRenderTexture();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        bitmapFboTexture.onSurfaceCreated();
        bitmapRenderTexture.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //宽高
        GLES20.glViewport(0, 0, width, height);

        bitmapFboTexture.onSurfaceChanged(width, height);
        bitmapRenderTexture.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清空颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //设置背景颜色
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //FBO处理
        bitmapFboTexture.draw();
        //通过FBO处理之后，拿到纹理id，然后渲染
        bitmapRenderTexture.draw(bitmapFboTexture.getFboTextureId());
    }
}
