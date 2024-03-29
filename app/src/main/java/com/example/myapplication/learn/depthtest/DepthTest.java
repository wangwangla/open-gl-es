package com.example.myapplication.learn.depthtest;

import android.content.Context;
import android.opengl.GLES20;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

/**
 * 深度测试：一般绘制立方体的时候需要使用
 *
 *
 */
public class DepthTest extends BaseGameScreen {
    private Depth imageTexture;
    private Depth imageTexture1;
    public DepthTest(Context context){
        imageTexture = new Depth(context);
        imageTexture1 = new Depth(context);
    }

    @Override
    public void create() {
        imageTexture.create();
        imageTexture1.create();
    }

    @Override
    public void render() {
//        GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        GLES20.glEnable(GLES20.GL_STENCIL_TEST);
//
//        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 0xFF); //所有片段都要写入模板缓冲
//        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);//若模板测试和深度测试都通过了，将片段对应的模板值替换为1
//        GLES20.glStencilMask(0xFF);
//
//绘制物体
//        imageTexture.transform(0,0,0);
        imageTexture.render();
//        imageTexture.render();

//        GLES20.glStencilFunc(GLES20.GL_NOTEQUAL, 1, 0xFF);//当片段的模板值不为 1 时，片段通过测试进行渲染
//
////禁用模板写入和深度测试
//        GLES20.glStencilMask(0x00);
//        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

//绘制物体轮廓;
//放大 1.05 倍

//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        GLES20.glDepthFunc(GLES20.GL_LESS);
//        imageTexture.rotation(20,1,0,0);

        imageTexture1.render();
//        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

//开启模板写入和深度测试
//        GLES20.glStencilMask(0xFF);
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        GLES20.glDisable(GLES20.GL_STENCIL_TEST);
    }

    @Override
    public void surfaceChange(int width, int height) {
        imageTexture.surfaceChange(width,height);
        imageTexture1.surfaceChange(width,height);
        imageTexture1.rotation(60,1,0,0);
        imageTexture1.transform(0.3F,0.3F,0);
    }

    @Override
    public void dispose() {
        imageTexture.dispose();
    }
}
