package com.example.myapplication.learn.base;

import android.opengl.GLES20;
import com.example.myapplication.MainActivity;
import com.example.myapplication.base.shape.ChangerColorTriangleMatrix;
import com.example.myapplication.core.Game;
import com.example.myapplication.learn.camra.CameView;
import com.example.myapplication.learn.meiyan.ImageTextureMeiyanStep1;
import com.example.myapplication.learn.meiyan.ImageTextureMeiyanStep2;
import com.example.myapplication.learn.meiyan.ImageTextureMeiyanStep3;
import com.example.myapplication.learn.shape.base.Shape;
import com.example.myapplication.learn.texture.ImageTextureMat;
import com.example.myapplication.learn.texture.ImageTextureMatYUV;
import com.example.myapplication.learn.texture.ImageTextureMatYUV1;
import com.example.myapplication.learn.texture.ImageTextureMatrix;


public class MyGame extends Game {
    private Shape shape;

    public MyGame(MainActivity mainActivity){
        shape = new ImageTextureMatYUV(mainActivity);
//        shape = new CameView();
    }

    @Override
    public void create() {
        shape.create();
    }


    @Override
    public void render() {
        GLES20.glClearColor(0.5F,0.5F,0.5F,1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        shape.render();
    }


    @Override
    public void dispose() {

    }

    @Override
    public void surfaceChanage(int width, int height) {
        shape.surfaceChange(width,height);
    }

    @Override
    public void resume() {
        super.resume();
        shape.resume();
    }

    @Override
    public void pause() {
        super.pause();
        shape.pause();
    }
}

