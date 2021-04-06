package me.felixzhang.example.my_particles2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;


public class ParticleActivity extends Activity {

    private GLSurfaceView mGLSurfaceView;
    private ParticlesRenderer mRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);

        mGLSurfaceView.setEGLContextClientVersion(2);
        mRenderer = new ParticlesRenderer(this);
        mGLSurfaceView.setRenderer(mRenderer);

        setContentView(mGLSurfaceView);
    }


}
