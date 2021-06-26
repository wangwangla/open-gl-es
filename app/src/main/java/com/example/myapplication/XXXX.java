package com.example.myapplication;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class XXXX extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        GLSurfaceView view = new GLSurfaceView(this);
        setContentView(view);
        view.setRenderer(new GLSurfaceView.Renderer() {
//            private TriangleDemo01 triangle01 = new TriangleDemo01();
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
//                triangle01.surfaceChange(width,height);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
//                triangle01.render();
            }
        });
    }
}
