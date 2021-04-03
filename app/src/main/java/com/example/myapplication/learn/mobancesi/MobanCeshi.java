package com.example.myapplication.learn.mobancesi;

import android.content.Context;
import android.opengl.GLES20;

import com.example.myapplication.Yuan;
import com.example.myapplication.learn.shape.Triangle;
import com.example.myapplication.learn.shape.base.Shape;

import java.util.TreeMap;

/**
 * 模板测试
 */
public class MobanCeshi  extends Shape {
    private Moban imageTexture;
    private Moban2 imageTexture1;
    private Yuan triangle;
    public MobanCeshi(Context context){
        imageTexture = new Moban(context);
        imageTexture1 = new Moban2(context);
        triangle = new Yuan();
    }

    @Override
    public void create() {
        imageTexture.create();
        imageTexture1.create();
        triangle.create();
    }

    @Override
    public void render() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT |GLES20.GL_STENCIL_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_STENCIL_TEST);
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 0xEF);
        triangle.render();
        GLES20.glStencilFunc(GLES20.GL_EQUAL, 0x1, 0xFF);//等于1 通过测试 ,就是上次绘制的图 的范围 才通过测试。
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);//没有通过测试的，保留原来的，也就是保留上一次的值。
//        imageTexture1.render();
        imageTexture.render();
        GLES20.glDisable(GLES20.GL_STENCIL_TEST);
    }

    @Override
    public void surfaceChange(int width, int height) {
        imageTexture.surfaceChange(width,height);
//        imageTexture1.surfaceChange(width,height);
//        imageTexture1.transform(0.3F,0.3F,0);
        triangle.surfaceChange(width, height);
    }

    @Override
    public void dispose() {
        imageTexture.dispose();
    }
}
