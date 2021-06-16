package com.example.myapplication.learn.base;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AndroidGraphics implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{
    ApplicationListener applicationListener;
    private GLSurfaceView view ;

    public AndroidGraphics(MainActivity mainActivity){
        mainActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        view = mainActivity.findViewById(R.id.surface);
//        view = new MySurfaceView(mainActivity);
        view.setEGLContextClientVersion(2);
        view.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        view.setRenderer(this);
        view.setRenderMode(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
//        view.setRenderer(new MyRenderer(mainActivity));
//        view.setEGLConfigChooser(8, 8, 8, 8, 16, 16);

//        view.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        view.setZOrderOnTop(true);
//        view.setRenderer(new ParticleRenderer(mainActivity));
//        ViewGroup.LayoutParams lp = view.getLayoutParams();
//        Point point_width = new Point();
//        mainActivity.getWindowManager().getDefaultDisplay().getSize(point_width);
//        lp.width = 1280;
//        lp.height = 720;
//        view.setLayoutParams(lp);
        applicationListener = new MyGame(mainActivity);
    }

    public void change(int type){
        applicationListener.change(type);


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        applicationListener.create();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
        applicationListener.surfaceChanage(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        applicationListener.render();
    }

    public void onPause() {
        ((GLSurfaceView)(view)).onPause();
        applicationListener.pause();
    }

    public void onResume() {
        if (view!=null)
        ((GLSurfaceView)(view)).onResume();
        applicationListener.resume();
    }

    public View getView() {
        return view;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        view.setRenderer(this);
    }
}
