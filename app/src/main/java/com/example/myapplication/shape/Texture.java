package com.example.myapplication.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.view.View;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Texture extends Shape{
    private int mProgram;
    private int glHPosition;
    private int glHTexture;
    private int glHCoordinate;
    private int glHMatrix;
    private int hIsHalf;
    private int glHUxy;
    private Bitmap mBitmap;

    private FloatBuffer bPos;
    private FloatBuffer bCoord;
    private boolean isHalf;

    private float uXY;
    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mMVPMatrix=new float[16];

    private final float[] sPos={
            -1.0f,1.0f,
            -1.0f,-1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };

    private final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };
    private String vertexShaderCode =
            "attribute vec4 vPosition;\n" +
            "attribute vec2 vCoordinate;\n" +
            "uniform mat4 vMatrix;\n" +
            "\n" +
            "varying vec2 aCoordinate;\n" +
            "varying vec4 aPos;\n" +
            "varying vec4 gPosition;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_Position=vMatrix*vPosition;\n" +
            "    aPos=vPosition;\n" +
            "    aCoordinate=vCoordinate;\n" +
            "    gPosition=vMatrix*vPosition;\n" +
            "}";
    private String fragmentShaderCode = "precision mediump float;\n" +
            "\n" +
            "uniform sampler2D vTexture;\n" +
            "uniform int vChangeType;\n" +
            "uniform vec3 vChangeColor;\n" +
            "uniform int vIsHalf;\n" +
            "uniform float uXY;\n" +
            "\n" +
            "varying vec4 gPosition;\n" +
            "\n" +
            "varying vec2 aCoordinate;\n" +
            "varying vec4 aPos;\n" +
            "\n" +
            "void modifyColor(vec4 color){\n" +
            "    color.r=max(min(color.r,1.0),0.0);\n" +
            "    color.g=max(min(color.g,1.0),0.0);\n" +
            "    color.b=max(min(color.b,1.0),0.0);\n" +
            "    color.a=max(min(color.a,1.0),0.0);\n" +
            "}\n" +
            "\n" +
            "void main(){\n" +
            "    vec4 nColor=texture2D(vTexture,aCoordinate);\n" +
            "    if(aPos.x>0.0||vIsHalf==0){\n" +
            "        if(vChangeType==1){\n" +
            "            float c=nColor.r*vChangeColor.r+nColor.g*vChangeColor.g+nColor.b*vChangeColor.b;\n" +
            "            gl_FragColor=vec4(c,c,c,nColor.a);\n" +
            "        }else if(vChangeType==2){\n" +
            "            vec4 deltaColor=nColor+vec4(vChangeColor,0.0);\n" +
            "            modifyColor(deltaColor);\n" +
            "            gl_FragColor=deltaColor;\n" +
            "        }else if(vChangeType==3){\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x-vChangeColor.r,aCoordinate.y-vChangeColor.r));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x-vChangeColor.r,aCoordinate.y+vChangeColor.r));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x+vChangeColor.r,aCoordinate.y-vChangeColor.r));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x+vChangeColor.r,aCoordinate.y+vChangeColor.r));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x-vChangeColor.g,aCoordinate.y-vChangeColor.g));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x-vChangeColor.g,aCoordinate.y+vChangeColor.g));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x+vChangeColor.g,aCoordinate.y-vChangeColor.g));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x+vChangeColor.g,aCoordinate.y+vChangeColor.g));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x-vChangeColor.b,aCoordinate.y-vChangeColor.b));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x-vChangeColor.b,aCoordinate.y+vChangeColor.b));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x+vChangeColor.b,aCoordinate.y-vChangeColor.b));\n" +
            "            nColor+=texture2D(vTexture,vec2(aCoordinate.x+vChangeColor.b,aCoordinate.y+vChangeColor.b));\n" +
            "            nColor/=13.0;\n" +
            "            gl_FragColor=nColor;\n" +
            "        }else if(vChangeType==4){\n" +
            "            float dis=distance(vec2(gPosition.x,gPosition.y/uXY),vec2(vChangeColor.r,vChangeColor.g));\n" +
            "            if(dis<vChangeColor.b){\n" +
            "                nColor=texture2D(vTexture,vec2(aCoordinate.x/2.0+0.25,aCoordinate.y/2.0+0.25));\n" +
            "            }\n" +
            "            gl_FragColor=nColor;\n" +
            "        }else{\n" +
            "            gl_FragColor=nColor;\n" +
            "        }\n" +
            "    }else{\n" +
            "        gl_FragColor=nColor;\n" +
            "    }\n" +
            "}";


    private Context context;
    public Texture(Context context){
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

    public void setHalf(boolean half){
        this.isHalf=half;
    }

    public void setImageBuffer(int[] buffer,int width,int height){
        mBitmap= Bitmap.createBitmap(buffer,width,height, Bitmap.Config.RGB_565);
    }

    public void setBitmap(Bitmap bitmap){
        this.mBitmap=bitmap;
    }

    public void onSurfaceCreated() {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
        glHPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHCoordinate=GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        glHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
        glHMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix");
        hIsHalf=GLES20.glGetUniformLocation(mProgram,"vIsHalf");
        glHUxy=GLES20.glGetUniformLocation(mProgram,"uXY");
//        hChangeType=GLES20.glGetUniformLocation(mProgram,"vChangeType");
//        hChangeColor=GLES20.glGetUniformLocation(mProgram,"vChangeColor");
        try {
            mBitmap= BitmapFactory.decodeStream(context.getAssets().open("texture/fengj.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0,0,width,height);

        int w=mBitmap.getWidth();
        int h=mBitmap.getHeight();
        float sWH=w/(float)h;
        float sWidthHeight=width/(float)height;
        uXY=sWidthHeight;
        if(width>height){
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight*sWH,sWidthHeight*sWH, -1,1, 3, 5);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight/sWH,sWidthHeight/sWH, -1,1, 3, 5);
            }
        }else{
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1/sWidthHeight*sWH, 1/sWidthHeight*sWH,3, 5);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH/sWidthHeight, sWH/sWidthHeight,3, 5);
            }
        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
//        GLES20.glUniform1i(hChangeType,filter.getType());
//        GLES20.glUniform3fv(hChangeColor,1,filter.data(),0);
        GLES20.glUniform1i(hIsHalf,isHalf?1:0);
        GLES20.glUniform1f(glHUxy,uXY);
        GLES20.glUniformMatrix4fv(glHMatrix,1,false,mMVPMatrix,0);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexture, 0);
        createTexture();
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
    }

    private int createTexture(){
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
            return texture[0];
        }
        return 0;
    }

    @Override
    public void render() {

    }

    @Override
    public void create() {

    }
}
