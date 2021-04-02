package com.example.myapplication.learn.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.example.myapplication.learn.shape.base.Shape;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Move1 extends Shape {
    private int mProgram;
    private int glHPosition;
    private int glHTexture;
    private int glHCoordinate;
    private int vMatrix;
    private Bitmap mBitmap;
    private FloatBuffer bPos;
    private FloatBuffer bCoord;
    //相机位置



    private final float[] sPos={
            -1.0f,1.0f,
            -1.0f,-1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };
    private float[] mModelMatrix = new float[16];
    private final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };
    private String vertexShaderCode =
            "attribute vec4 vPosition;\n" +      //位置
                    "attribute vec2 vCoordinate;\n" +    // 纹理
                    "varying vec2 aCoordinate;\n" +
                    "uniform mat4 vMatrix;" +      //  传递纹理   片段着色器
                    "void main(){\n" +
                    "    gl_Position=vPosition * vMatrix;\n" +
                    "    aCoordinate=vCoordinate;\n" +
                    "}";
    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform sampler2D vTexture;\n" +
                    "varying vec2 aCoordinate;\n" +
                    "void main(){\n" +
                    "    vec4 nColor=texture2D(vTexture,aCoordinate);\n"+
                    "    gl_FragColor=nColor;" +
                    "}";


    private Context context;
    public Move1(Context context){
        this.context = context;
        ByteBuffer bb=ByteBuffer.allocateDirect(sPos.length*4);
        bb.order(ByteOrder.nativeOrder());
        bPos=bb.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);
        ByteBuffer cc=ByteBuffer.allocateDirect(sCoord.length*4);
        cc.order(ByteOrder.nativeOrder());
        bCoord=cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }

//    private int vChangeColor;

    public void preProgram(){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    private int[] createTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture;
        }
        return null;
    }

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    @Override
    public void render() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);

//        GLES20.glUniform4fv(vChangeColor,1,new float[]{1,1,1,1},0);

        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexture, 0);
        int []texture = createTexture();

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 4.0f, 0.0f, -7.0f);
//        Matrix.rotateM(mModelMatrix, 0, 4, 0f, 0.0f, 0.0f);
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mMVPMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES20.glDisableVertexAttribArray(glHPosition);
        GLES20.glDisableVertexAttribArray(glHCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            GLES20.glDeleteTextures(1, texture, 0);
        }
    }

    @Override
    public void create() {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        preProgram();
        glHPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHCoordinate=GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        glHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
        vMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        try {
            mBitmap= BitmapFactory.decodeStream(context.getAssets().open("texture/fengj.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0,0,width,height);
        //---------------
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void dispose() {

    }



//    // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
//    // (which currently contains model * view).
//        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
//
//    // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
//    // (which now contains model * view * projection).
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
//
//    // Pass in the combined matrix.
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

}
