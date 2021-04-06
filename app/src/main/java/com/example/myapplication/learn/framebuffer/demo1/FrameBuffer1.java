package com.example.myapplication.learn.framebuffer.demo1;

import android.content.Context;
import android.opengl.GLES20;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

public class FrameBuffer1 extends BaseGameScreen {
    private BitmapFboTexture bitmapFboTexture;
    private BitmapRenderTexture bitmapRenderTexture;

    public FrameBuffer1(Context context) {
        bitmapFboTexture = new BitmapFboTexture(context);
        bitmapRenderTexture = new BitmapRenderTexture();
    }

    @Override
    public void create() {
        bitmapFboTexture.onSurfaceCreated();
        bitmapRenderTexture.onSurfaceCreated();
    }

    @Override
    public void render() {
        //清空颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //设置背景颜色
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //FBO处理
        bitmapFboTexture.draw();
        //通过FBO处理之后，拿到纹理id，然后渲染
        bitmapRenderTexture.draw(bitmapFboTexture.getFboTextureId());
    }

    @Override
    public void surfaceChange(int width, int height) {
        //宽高
//        width = width / 2;
//        height = height/2;
        GLES20.glViewport(0, 0, width, height);

        bitmapFboTexture.onSurfaceChanged(width, height);
        bitmapRenderTexture.onSurfaceChanged(width, height);
    }

    @Override
    public void dispose() {

    }
}