package com.example.myapplication.learn.framebuffer.demo3;

import android.content.Context;
import android.opengl.GLES20;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Demo01x extends BaseGameScreen {
    private Context context;

    private float[] vertext = {
            -1.0f,1.0f,
            -1.0f,-1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };

    private float[] coood = {
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };
    private int glHPosition;
    private int glHCoordinate;

    private FloatBuffer vertextBuffer;
    private FloatBuffer coodBuffer;


    public Demo01x(Context context){
        super(context.getResources());
        this.context = context;
        vertexShaderCode = uRes("shader/vertex.sh");
        fragmentShaderCode = uRes("shader/fragment.frag");
    }

    @Override
    public void create() {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertext.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertextBuffer = bb.asFloatBuffer();
        vertextBuffer.put(vertext);
        vertextBuffer.position(0);

        ByteBuffer coodbb = ByteBuffer.allocateDirect(coood.length * 4);
        coodbb.order(ByteOrder.nativeOrder());
        coodBuffer = coodbb.asFloatBuffer();
        coodBuffer.put(coood);
        coodBuffer.position(0);



        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);

        glHPosition = GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHCoordinate = GLES20.glGetAttribLocation(mProgram,"vCoord");
    }


    public void render(int textureId) {
        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,vertextBuffer);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,coodBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES20.glDisableVertexAttribArray(glHPosition);
        GLES20.glDisableVertexAttribArray(glHCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);

    }

    @Override
    public void render() {

    }

    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void dispose() {

    }
}
