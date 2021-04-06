package com.example.myapplication.learn.mobancesi;

import android.content.Context;
import android.opengl.GLES20;

import com.example.myapplication.Yuan;
import com.example.myapplication.learn.shape.base.BaseGameScreen;

/**
 * 模板测试
 */
public class MobanCeshi  extends BaseGameScreen {
    private Moban imageTexture;
    private Moban2 imageTexture1;
    private Yuan triangle;
    public MobanCeshi(Context context){
        imageTexture = new Moban(context);
        imageTexture1 = new Moban2(context);
        triangle = new Yuan();
    }

    @Override
    public void create() {
        imageTexture.create();
        imageTexture1.create();
        triangle.create();
    }

    /**
     * /*
     *  *
     * (glStencilFunc)常量
     * GL_NEVER 		从不通过模板测试
     * GL_ALWAYS 		总是通过模板测试
     * GL_LESS 		只有参考值<(模板缓存区的值&mask)时才通过
     * GL_LEQUAL 		只有参考值<=(模板缓存区的值&mask)时才通过
     * GL_EQUAL 		只有参考值=(模板缓存区的值&mask)时才通过
     * GL_GEQUAL 		只有参考值>=(模板缓存区的值&mask)时才通过
     * GL_GREATER 		只有参考值>(模板缓存区的值&mask)时才通过
     * GL_NOTEQUAL 	只有参考值!=(模板缓存区的值&mask)时才通过
     * (glStencilOp)常量
     * GL_KEEP 	保持当前的模板缓存区值
     * GL_ZERO 	把当前的模板缓存区值设为0
     * GL_REPLACE 	用glStencilFunc函数所指定的参考值替换模板参数值
     * GL_INCR 	增加当前的模板缓存区值，但限制在允许的范围内
     * GL_DECR 	减少当前的模板缓存区值，但限制在允许的范围内
     * GL_INVERT 	将当前的模板缓存区值进行逐位的翻转
     *  */

    @Override
    public void render() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT |GLES20.GL_STENCIL_BUFFER_BIT);
//        imageTexture.render();
        GLES20.glEnable(GLES20.GL_STENCIL_TEST);
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 0xEF);
        triangle.render();
        GLES20.glStencilFunc(GLES20.GL_NOTEQUAL, 0x1, 0xFF);//等于1 通过测试 ,就是上次绘制的图 的范围 才通过测试。
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);//没有通过测试的，保留原来的，也就是保留上一次的值。
        imageTexture1.render();
//
        GLES20.glDisable(GLES20.GL_STENCIL_TEST);
    }

    @Override
    public void surfaceChange(int width, int height) {
        imageTexture.surfaceChange(width,height);
        imageTexture1.surfaceChange(width,height);
//        imageTexture1.transform(0.3F,0.3F,0);
        triangle.surfaceChange(width, height);
    }

    @Override
    public void dispose() {
        imageTexture.dispose();
    }
}
