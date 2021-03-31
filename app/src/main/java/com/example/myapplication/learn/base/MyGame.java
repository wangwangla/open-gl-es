package com.example.myapplication.learn.base;

import android.opengl.GLES20;
import android.view.View;

import com.example.myapplication.MainActivity;
import com.example.myapplication.base.core.Game;
import com.example.myapplication.base.shape.Texture;
import com.example.myapplication.learn.framebuffer.FrameBufferDemo;
import com.example.myapplication.learn.shape.Triangle;
import com.example.myapplication.learn.shape.TriangleArray;
import com.example.myapplication.learn.shape.TriangleType;
import com.example.myapplication.learn.shape.base.Shape;
import com.example.myapplication.base.shape.ChangerColorTriangleMatrix;
import com.example.myapplication.learn.texture.ImageTexture;
import com.example.myapplication.learn.texture.ImageTextureMat;
import com.example.myapplication.learn.transform.Move;

import javax.microedition.khronos.opengles.GL10;


public class MyGame extends Game {
    private Shape shape;
//    private CamraDemo demo;

    public MyGame(MainActivity mainActivity, View view){
        shape = new Move(mainActivity);
//        shape = new ImageTextureClod(mainActivity);
//        demo = new CamraDemo();
    }

    @Override
    public void create() {
        shape.create();
//        demo.onSurfaceCreated();
    }


    @Override
    public void render(GL10 gl) {
        GLES20.glClearColor(0.5F,0.5F,0.5F,1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        shape.render();
//        demo.onDrawFrame(gl);
    }


    @Override
    public void dispose() {

    }

    @Override
    public void surfaceChanage(int width, int height) {
        shape.surfaceChange(width,height);
    }
}
