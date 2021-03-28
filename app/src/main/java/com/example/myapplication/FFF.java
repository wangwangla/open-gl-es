package com.example.myapplication;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Description:
 */
public class FFF extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView= new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(new TexureRender(this));
        setContentView(mGLView);
    }

}
