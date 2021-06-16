package com.example.myapplication;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.learn.base.AndroidGraphics;

public class MainActivity extends Activity implements View.OnClickListener{
    protected AndroidGraphics graphics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View viewById = findViewById(R.id.triangle);
        View viewById1 = findViewById(R.id.triangle_array);
        View viewById2 = findViewById(R.id.triangle_matrix);
        View viewById3 = findViewById(R.id.triangle_type);
        View viewById4 = findViewById(R.id.egl);
        viewById.setOnClickListener(this);
        viewById1.setOnClickListener(this);
        viewById2.setOnClickListener(this);
        viewById3.setOnClickListener(this);
        viewById4.setOnClickListener(this);

        graphics = new AndroidGraphics(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.egl:
                break;
            case R.id.triangle:
                graphics.change(1);
                break;
            case R.id.triangle_array:
                graphics.change(2);
                break;
            case R.id.triangle_matrix:
                graphics.change(3);
                break;
            case R.id.triangle_type:
                graphics.change(4);
                break;



        }
    }
}
