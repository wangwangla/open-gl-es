package com.example.myapplication;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.myapplication.learn.base.AndroidGraphics;

public class MainActivity extends Activity {
    protected AndroidGraphics graphics;
    private GLSurfaceView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        graphics = new AndroidGraphics(this);
//        setContentView(graphics.getView());
        setContentView(R.layout.activity_main);
        graphics = new AndroidGraphics(this);

//        GLSurfaceView view = findViewById(R.id.surface);
//        view = new MySurfaceView(this);
//        view.setEGLContextClientVersion(2);
//        view.setRenderer(new ParticlesRenderer(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (graphics !=null){
            graphics.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (graphics !=null){
            graphics.onResume();
        }
    }
}
