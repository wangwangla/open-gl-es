package com.example.myapplication.learn.base;

import android.opengl.GLES20;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Yuan;
import com.example.myapplication.core.Game;
import com.example.myapplication.learn.mobancesi.MobanCeshi;
import com.example.myapplication.learn.mobancesi.MobanTest;
import com.example.myapplication.learn.shape.base.Shape;


public class MyGame extends Game {
    private MobanCeshi shape;

    public MyGame(MainActivity mainActivity){
        shape = new MobanCeshi(mainActivity);
    }

    @Override
    public void create() {
        shape.create();
    }


    @Override
    public void render() {
        GLES20.glClearColor(0.5F,0.5F,0.5F,1);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT |GLES20.GL_STENCIL_BUFFER_BIT);

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
//        shape.resume();
    }

    @Override
    public void pause() {
        super.pause();
//        shape.pause();
    }
}

