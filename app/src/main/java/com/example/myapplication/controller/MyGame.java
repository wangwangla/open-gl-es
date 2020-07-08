package com.example.myapplication.controller;

import android.opengl.GLES20;

import com.example.myapplication.core.Game;
import com.example.myapplication.shape.Triangle;

public class MyGame extends Game {
    private Triangle triangle;
    public MyGame (){
        triangle = new Triangle();
    }
    @Override
    public void create() {
        triangle.create();
    }

    @Override
    public void render() {
        GLES20.glClearColor(0,0,0,0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        triangle.render();
    }


    @Override
    public void dispose() {

    }
}
