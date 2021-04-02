package com.example.myapplication.learn.texture;

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

/**
 *  我认为需要时yuv的图片才可以正确的显示,
 */
public class ImageTextureMatYUV extends Shape {
    private int mProgram;
    private int glHPosition;
    private int glHTexture1;
    private int glHTexture2;
    private int glHCoordinate;
    private int vMatrix;
    private Bitmap mBitmap;
    private FloatBuffer bPos;
    private FloatBuffer bCoord;
    //相机位置
    private float[] mViewMatrix=new float[16];
    //透视
    private float[] mProjectMatrix=new float[16];
    //变换矩阵
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
//                    "uniform sampler2D vTexture;\n" +


                    "uniform sampler2D SamplerY;            \n"+
                    "uniform sampler2D SamplerUV;            \n"+
                    "const float PI = 3.14159265;           \n"+
                    "const mat3 convertMat = mat3( 1.0, 1.0, 1.0, 0.0, -0.39465, 2.03211, 1.13983, -0.58060, 0.0 );\n"+


                    "varying vec2 aCoordinate;\n" +
                    "void main(){\n" +
//                    "    vec4 nColor=texture2D(vTexture,aCoordinate);\n"+
//                    "    gl_FragColor=nColor;" +


                    "vec3 yuv;                                  \n"+
                    "yuv.x = texture2D(SamplerY, aCoordinate).r;         \n"+
                    "yuv.z = texture2D(SamplerUV, aCoordinate).r - 0.5;   \n"+
                    "yuv.y = texture2D(SamplerUV, aCoordinate).a - 0.5;   \n"+
                    "vec3 color = convertMat * yuv;             \n"+
                    "vec4 mainColor = vec4(color, 1.0);         \n"+
                    "gl_FragColor =mainColor;                                       \n"+
                    "}";


    private Context context;
    public ImageTextureMatYUV(Context context){

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

    private int[] createTexture(int[] texture,int format){
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
//            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);

            GLUtils.texImage2D(
                    GLES20.GL_TEXTURE_2D,
                0,
                    format,
                mBitmap, -1, 0);
            return texture;
        }
        return null;
    }

    private int[] texture1 = new int[1];
    private int[] texture2 = new int[1];

    @Override
    public void render() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);

//        GLES20.glUniform4fv(vChangeColor,1,new float[]{1,1,1,1},0);
        int []t_1 = createTexture(texture1,GLES20.GL_LUMINANCE_ALPHA);
        int []t_2 = createTexture(texture2,GLES20.GL_LUMINANCE);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexture1, 0);
        GLES20.glUniform1i(glHTexture2, 1);


        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mMVPMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES20.glDisableVertexAttribArray(glHPosition);
        GLES20.glDisableVertexAttribArray(glHCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            GLES20.glDeleteTextures(1, t_1, 0);
            GLES20.glDeleteTextures(1, t_2, 0);
        }
    }

    @Override
    public void create() {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        preProgram();
        glHPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHCoordinate=GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        glHTexture1=GLES20.glGetUniformLocation(mProgram,"SamplerY");
        glHTexture2=GLES20.glGetUniformLocation(mProgram,"SamplerUV");

//        "uniform sampler2D SamplerY;            \n"+
//                "uniform sampler2D SamplerUV;

        vMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        try {
            mBitmap= BitmapFactory.decodeStream(context.getAssets().open("texture/11.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0,0,width,height);
        int w=mBitmap.getWidth();
        int h=mBitmap.getHeight();
        float sWH=w/(float)h;
        float sWidthHeight=width/(float)height;
        if(width<height){
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1/sWidthHeight*sWH, 1/sWidthHeight*sWH,3, 7);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH/sWidthHeight, sWH/sWidthHeight,3, 7);
            }
        }else{
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight*sWH,sWidthHeight*sWH, -1,1, 3, 7);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight/sWH,sWidthHeight/sWH, -1,1, 3, 7);
            }
        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void dispose() {

    }
}
