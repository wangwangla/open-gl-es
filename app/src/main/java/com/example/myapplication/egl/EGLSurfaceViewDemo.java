package com.example.myapplication.egl;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/**
 * EGL
 */
public class EGLSurfaceViewDemo extends SurfaceView implements SurfaceHolder.Callback {
    private GL10 mGL10;
    public EGLSurfaceViewDemo(Context context) {
        super(context);
        //注册回调接口
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //开启线程
        startGLThreads();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 关闭
        stopGLThreads();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    private void onDrawFrame(GL10 gl) {
        gl.glClearColor(1, 0, 0, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }


    private EGL_GLThread mEGL_GLThread;
    public void startGLThreads() {
        mEGL_GLThread = new EGL_GLThread();
        mEGL_GLThread.drawFlag(true);        //线程标志位为true
        mEGL_GLThread.start();
    }

    public void stopGLThreads() {
        mEGL_GLThread.drawFlag(false);
        mEGL_GLThread = null;
    }

    //刷帧线程
    private class EGL_GLThread extends Thread {
        private boolean keyFlag = false;
        @Override
        public void run() {
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            EGLDisplay eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            egl10.eglInitialize(eglDisplay, new int[2]);
            EGLConfig[] configs = new EGLConfig[1];
            egl10.eglChooseConfig(eglDisplay,
                    new int[]{EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE},
                    configs,
                    1,
                    new int[1]);
            EGLConfig eglEGLConfig = configs[0];
            EGLSurface eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglEGLConfig,
                    EGLSurfaceViewDemo.this.getHolder(), null);
            EGLContext eglContext = egl10.eglCreateContext(eglDisplay, eglEGLConfig,
                    EGL10.EGL_NO_CONTEXT, null);
            egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
            mGL10 = (GL10) eglContext.getGL();
            //绘制是一直执行的
            while (isDrawing()) {
                onDrawFrame(mGL10);
                egl10.eglSwapBuffers(eglDisplay, eglSurface);
            }
            stop(egl10, eglDisplay, eglSurface, eglContext);
        }

        private void stop(EGL10 egl10, EGLDisplay eglDisplay, EGLSurface eglSurface, EGLContext eglContext) {
            egl10.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_CONTEXT);
            egl10.eglDestroyContext(eglDisplay, eglContext);
            egl10.eglDestroySurface(eglDisplay, eglSurface);
            mGL10 = null;
        }

        public void drawFlag(boolean keyFlag) {
            this.keyFlag = keyFlag;
        }

        public boolean isDrawing() {
            return keyFlag;
        }
    }
}

