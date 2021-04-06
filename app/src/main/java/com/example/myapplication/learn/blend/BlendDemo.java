package com.example.myapplication.learn.blend;

import android.content.Context;
import android.opengl.GLES20;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

public class BlendDemo extends BaseGameScreen {
    private Blend01 imageTexture;
    private Blend02 imageTexture1;
//    private Yuan triangle;
    public BlendDemo(Context context){
        imageTexture = new Blend01(context);
        imageTexture1 = new Blend02(context);
//        triangle = new Yuan();
    }

    @Override
    public void create() {
        imageTexture.create();
        imageTexture1.create();
//        triangle.create();
    }

    @Override
    public void render() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT |GLES20.GL_STENCIL_BUFFER_BIT);
//        triangle.surfaceChange(widthXX, heightXX);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        imageTexture.render();
        imageTexture1.render();
        GLES20.glEnable(GLES20.GL_BLEND);


//        GLES20.glEnable(GLES20.GL_BLEND);
//        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//        triangle.surfaceChange(widthXX/2, heightXX/2);
//        triangle.render();
//        GLES20.glDisable(GLES20.GL_BLEND);
    }

    int widthXX;
    int heightXX;
    @Override
    public void surfaceChange(int width, int height) {
        this.widthXX = width;
        this.heightXX = height;
        imageTexture.surfaceChange(width,height);
        imageTexture1.surfaceChange(width,height);
//        imageTexture1.transform(0.3F,0.3F,0);
//        triangle.surfaceChange(width/2,height/2);
//        triangle.surfaceChange(width/2, height/2);
    }

    @Override
    public void dispose() {
        imageTexture.dispose();
    }
}
