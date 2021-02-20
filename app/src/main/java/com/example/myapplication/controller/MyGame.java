package com.example.myapplication.controller;

import android.opengl.GLES20;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.core.Game;
import com.example.myapplication.image.ChangeImage;
import com.example.myapplication.image.ClodImage;
import com.example.myapplication.image.GrayImage;
import com.example.myapplication.image.ImageTexture;
import com.example.myapplication.shape.ChangerColorTriangleMatrix;
import com.example.myapplication.shape.Image;
import com.example.myapplication.shape.Texture;
import com.example.myapplication.shape.Triangle;
import com.example.myapplication.shape.TriangleMatrix;

public class MyGame extends Game {
    //显示三角形
//    private Triangle triangle;
    //显示等腰三角形
//    private TriangleMatrix triangleMatrix ;
//  变化颜色
//    private ChangerColorTriangleMatrix matrix ;
//    private Texture texture;
//    private ClodImage image;
//    private Image image;
//    private GrayImage image;
//    private ChangeImage image;

    private ImageTexture texture;
    public MyGame(MainActivity mainActivity){
        texture = new ImageTexture(mainActivity);
//        triangle = new Triangle();
//        triangleMatrix = new TriangleMatrix();
//        matrix = new ChangerColorTriangleMatrix();
//        image = new ClodImage(mainActivity);
//        texture = new Texture(mainActivity);
//         image = new ChangeImage(mainActivity);
//        image = new Image(mainActivity);
    }
    @Override
    public void create() {
//        triangle.create();
//        triangleMatrix.create();
//        matrix.create();
//        image.create();
//        texture.onSurfaceCreated();
//        image.create();
        texture.create();
    }


    @Override
    public void render() {
        GLES20.glClearColor(0.5F,0.5F,0.5F,1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        triangle.render();
//        image.render();
        texture.render();
//        triangleMatrix.render();
//        matrix.render();
//        texture.onDrawFrame();
//        image.render();
    }


    @Override
    public void dispose() {

    }

    @Override
    public void surfaceChanage(int width, int height) {
//        triangleMatrix.surfaceChange(width,height);
//        matrix.surfaceChange(width,height);
//        texture.onSurfaceChanged(width,height);
//        image.onSurfaceCreated();
//        image.onSurfaceCreated();

    }
}
