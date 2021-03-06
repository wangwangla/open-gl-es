package com.example.myapplication.learn.base;

import javax.microedition.khronos.opengles.GL10;

public interface ApplicationListener {
    public void create ();
    public void resize (int width, int height);
    public void render(GL10 gl);
    public void pause ();
    public void resume ();
    public void dispose ();

    void surfaceChanage(int width, int height);
}
