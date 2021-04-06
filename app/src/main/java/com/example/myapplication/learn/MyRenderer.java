package com.example.myapplication.learn;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.myapplication.learn.shape.TriangleType;
import com.example.myapplication.learn.shape.base.BaseGameScreen;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {

    private BaseGameScreen square1 = null;

    private FloatBuffer vertices = null;

    // Step size in x and y directions
    // (number of pixels to move each time)
    private float x = 0;
    private float y = 0;
    private float xstep = 1.0f;
    private float ystep = 1.0f;

    // Keep track of windows changing width and height
    private float windowWidth;
    private float windowHeight;

    public MyRenderer(Context ctx) {
        square1 = new TriangleType();

    }

    private void makeStencilPattern(GL10 gl) {
        float dRadius = 0.1f;

        ByteBuffer bb = ByteBuffer.allocateDirect(4000 * 4 * 2);
        bb.order(ByteOrder.nativeOrder());
        vertices = bb.asFloatBuffer();
        vertices.position(0);

        for(float dAngle = 0; dAngle < 400.0; dAngle += 0.1)
        {
            vertices.put((float) (dRadius * Math.cos(dAngle)));
            vertices.put((float) (dRadius * Math.sin(dAngle)));
            dRadius *= 1.002;
        }

        vertices.position(0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);
        // Replace the current matrix with the identity matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glStencilFunc(GL10.GL_NEVER, 0x0, 0x0);
        gl.glStencilOp(GL10.GL_INCR, GL10.GL_INCR, GL10.GL_INCR);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0,
                vertices);
        gl.glColor4f(0, 1, 1, 1);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4000 * 2);

        gl.glStencilFunc(GL10.GL_NOTEQUAL, 0x1, 0x1);
        gl.glStencilOp(GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_KEEP);
        gl.glColor4f(1, 0, 0, 1);

        // Reverse direction when you reach left or right edge
        if(x > windowWidth-20 || x < -windowWidth)
            xstep = -xstep;

        // Reverse direction when you reach top or bottom edge
        if(y > windowHeight || y < -windowHeight + 20)
            ystep = -ystep;


        // Check bounds. This is in case the window is made
        // smaller while the rectangle is bouncing and the
        // rectangle suddenly finds itself outside the new
        // clipping volume
        if(x > windowWidth-20)
            x = windowWidth-20-1;

        if(y > windowHeight)
            y = windowHeight-1;

        // Actually move the square
        x += xstep;
        y += ystep;

        gl.glTranslatef(x, y, 0);

        square1.render();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        float aspectRatio;

        // Prevent a divide by zero
        if(h == 0)
            h = 1;

        // Set Viewport to window dimensions
        gl.glViewport(0, 0, w, h);

        // Reset coordinate system
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        // Establish clipping volume (left, right, bottom, top, near, far)
        aspectRatio = (float)w / (float)h;
        if (w <= h)
        {
            windowWidth = 100;
            windowHeight = 100 / aspectRatio;
            gl.glOrthof(-100.0f, 100.0f, -windowHeight, windowHeight, 1.0f, -1.0f);
        }
        else
        {
            windowWidth = 100 * aspectRatio;
            windowHeight = 100;
            gl.glOrthof(-windowWidth, windowWidth, -100.0f, 100.0f, 1.0f, -1.0f);
        }

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        square1.surfaceChange(w,h);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub
        gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);

        gl.glEnable(GL10.GL_STENCIL_TEST);
        gl.glClearStencil(0);

        makeStencilPattern(gl);
        square1.create();
    }

}