package com.example.myapplication.learn.base;

import android.opengl.GLES20;
import com.example.myapplication.MainActivity;
import com.example.myapplication.core.Game;
import com.example.myapplication.learn.shape.base.Shape;
import com.example.myapplication.learn.texture.ImageTexture;
import com.example.myapplication.learn.texture.ImageTexture02;
import com.example.myapplication.shape.ChangerColorTriangleMatrix;


public class MyGame extends Game {
    private Shape shape;

    public MyGame(MainActivity mainActivity){
//        shape = new ChangerColorTriangleMatrix();

        shape = new ImageTexture02(mainActivity);
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
}
