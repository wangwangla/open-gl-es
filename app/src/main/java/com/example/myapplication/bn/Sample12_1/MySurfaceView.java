package com.example.myapplication.bn.Sample12_1;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器    
    
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    //GLSurfaceView的宽高比
    float ratio;
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //设置使用OPENGL ES3.0
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染   
    }
	
	//触摸事件回调方法
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;//计算触控笔Y位移
            float dx = x - mPreviousX;//计算触控笔X位移
            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;//设置沿y轴旋转角度
            mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度
            requestRender();//重绘画面
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }

	private class SceneRenderer implements Renderer
    {  
		float yAngle;//绕Y轴旋转的角度
    	float xAngle; //绕X轴旋转的角度
    	//从指定的obj文件中加载对象
		LoadedObjectVertexNormal lovo;
    	
        public void onDrawFrame(GL10 gl) 
        {
            //设置屏幕背景色RGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);   
        	//清除深度缓冲与颜色缓冲
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //设置摄像机观察矩阵
            MatrixState.setCamera(0,0,0,0f,0f,-1f,0f,1.0f,0.0f);
            
            MatrixState.pushMatrix();//保护现场
            MatrixState.translate(0, -2f, -25f);   //平移变换
            //绕Y轴、X轴旋转
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            //若加载的物体部位空则绘制物体
            if(lovo!=null)
            {
            	lovo.drawSelf();
            }   
            
            //绘制副视角场景=============================================begin=========================             
            //启用剪裁测试
        	GLES30.glEnable(GL10.GL_SCISSOR_TEST);    
        	//设置区域
        	GLES30.glScissor(0,1080-900,830,900);//0,480-200,230,200);
        	//设置屏幕背景色RGBA
            GLES30.glClearColor(0.7f,0.7f,0.7f,1.0f);  
            //清除颜色缓存与深度缓存
            GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);            
            //调用此方法计算产生透视投影矩阵
//            MatrixState.setProjectFrustum(-0.62f*ratio, 4.18f*ratio, -2.55f, 0.45f, 2, 100);
            //设置摄像机观察矩阵
//            MatrixState.setCamera(0, 50f, -30f,0, -2f, -25f,0f,0.0f,-1.0f);
			if (lovo != null) {
				lovo.drawSelf();
			} 
            //禁用剪裁测试
        	GLES30.glDisable(GL10.GL_SCISSOR_TEST);  
        	//绘制副视角场景=============================================end=========================
            MatrixState.popMatrix();
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置 
        	GLES30.glViewport(0, 0, width, height); 
        	//计算GLSurfaceView的宽高比
            ratio = (float) width / height;
            //调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0,0,0,0f,0f,-1f,0f,1.0f,0.0f);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //打开深度检测
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //打开背面剪裁   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //初始化变换矩阵
            MatrixState.setInitStack();
            //初始化光源位置
            MatrixState.setLightLocation(40, 10, 20);
            //加载要绘制的物体
            lovo=LoadUtil.loadFromFileVertexOnly("sample12_1/ch.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
        }
    }
}
