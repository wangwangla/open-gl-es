package com.example.myapplication.controller;

import android.opengl.GLES20;

import com.example.myapplication.core.Game;
import com.example.myapplication.shape.Triangle;
import com.example.myapplication.shape.TriangleMatrix;

public class MyGame extends Game {
    //显示三角形
//    private Triangle triangle;
    //显示等腰三角形
    private TriangleMatrix triangleMatrix ;

    public MyGame (){
//        triangle = new Triangle();
        triangleMatrix = new TriangleMatrix();
    }
    @Override
    public void create() {
//        triangle.create();
        triangleMatrix.create();
    }


    @Override
    public void render() {
        GLES20.glClearColor(0,0,0,0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        triangleMatrix.render();
    }


    @Override
    public void dispose() {

    }

    @Override
    public void surfaceChanage(int width, int height) {
        triangleMatrix.surfaceChange(width,height);
    }
}
