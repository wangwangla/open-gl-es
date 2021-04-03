package com.example.myapplication.learn.mobancesi;

import android.content.Context;

import com.example.myapplication.base.shape.Image;
import com.example.myapplication.learn.shape.base.Shape;
import com.example.myapplication.learn.yuv.ImageTexture;

public class MobanTest extends Shape {
    private ImageTexture imageTexture;

    public MobanTest(Context context){
        imageTexture = new ImageTexture(context);
    }

    @Override
    public void create() {
        super.create();
        imageTexture.create();
    }

    @Override
    public void render() {
        imageTexture.render();
    }

    @Override
    public void surfaceChange(int width, int height) {
        imageTexture.surfaceChange(width,height);
    }

    @Override
    public void dispose() {
        imageTexture.dispose();
    }
}
