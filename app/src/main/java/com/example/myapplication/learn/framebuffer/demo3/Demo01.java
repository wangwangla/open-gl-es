package com.example.myapplication.learn.framebuffer.demo3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.myapplication.learn.shape.base.Shape;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Demo01 extends Shape {
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
    private Bitmap mBitmap;
    private int glHPosition;
    private int glHCoordinate;
    private int glHTexturee;

    private FloatBuffer vertextBuffer;
    private FloatBuffer coodBuffer;
    private Demo01x demo01x ;
    int fbotexture;
    private int frameId;

    private int imageTextureId;

    public Demo01(Context context){
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
        glHTexturee = GLES20.glGetUniformLocation(mProgram,"vTexture");

        try {
            mBitmap= BitmapFactory.decodeStream(context.getAssets().open("texture/fengj.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        createImageTexture();
        createFBO();
        demo01x = new Demo01x(context);
        demo01x.create();
    }

    @Override
    public void render() {
        drawFrameBuffer();
        demo01x.render(fbotexture);
    }

    public void drawFrameBuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameId);
        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexturee, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTextureId);
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,vertextBuffer);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,coodBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES20.glDisableVertexAttribArray(glHPosition);
        GLES20.glDisableVertexAttribArray(glHCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
    }

    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        demo01x.surfaceChange(width,height);
    }

    @Override
    public void dispose() {

    }


//    创建实质
    private int createImageTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //绑定
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
            imageTextureId = texture[0];
            return texture[0];
        }
        return 0;
    }
//

    public void createFBO(){
        int fb[] = new int[1];
        GLES20.glGenFramebuffers(1,fb,0);
        frameId = fb[0];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,frameId);
        fbotexture = createTexture();
        GLES20.glFramebufferTexture2D(
                GLES20.GL_FRAMEBUFFER,
                GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                fbotexture,
                0);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,
                0,
                GLES20.GL_RGBA,
                360,
                634,
                0,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE,
                null);
        //7. 解绑纹理和FBO
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    //创建一个表皮
    public int createTexture(){
        int [] textureIds = new int[1];
        GLES20.glGenTextures(1,textureIds,0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureIds[0]);
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        return textureIds[0];
    }
}
