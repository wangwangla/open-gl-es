package com.example.myapplication.core;

public interface ApplicationListener {
    public void create ();
    public void resize (int width, int height);
    public void render ();
    public void pause ();
    public void resume ();
    public void dispose ();

    void surfaceChanage(int width, int height);
}
