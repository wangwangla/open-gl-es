# open-gl-es

主要参考午王的博客，有的不是很理解，就粘贴进去，然后自己在写一遍。还有其他的一些博客，比如里面的HSV转化RGB等。

## 图形绘制

- 三角形
- 矩阵变换
- 等腰三角形
- 变色

## 纹理展示

- 纹理显示
- 矩阵变换
- 高斯模糊
- 放大镜
- 冷色
- 暖色
- 4宫格
- 设置颜色
- 灰色
- 绘制圆
- HSV显示（HSV-->RGB  RGB-->HSV）
- YUV 

## 属性

- 移动
- 旋转
- 缩放

## 其他

- frameBuffer
- egl
- 相机



机器翻译open gl es 2.0  + 2009

https://github.com/wangwangla/OpenGlEs



libgdx绘制圆

```java
public class SeneTest extends Group {
    private Image image1;
    private Image image2;
    private SpineActor spineActor;
    private Cir cir;
    public SeneTest(){
        cir = new Cir(300,300,200);  //测试区域
        addActor(cir);

        image1 = new Image(new Texture("11.png"));   //图片测试
        image2 = new Image(new Texture("122.png"));
        addActor(image1);

//        addActor(image2);

        spineActor = new SpineActor("spine/countrypic/Australia/Australia");
        addActor(spineActor);
        spineActor.setPosition(20,100, Align.center);   //动画测试
        spineActor.setAnimation("1",true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);


        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF);

        cir.draw(batch,parentAlpha);

        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 0x1, 0xFF);//等于1 通过测试 ,就是上次绘制的图 的范围 才通过测试。
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);//没有通过测试的，保留原来的，也就是保留上一次的值。

        spineActor.draw(batch,parentAlpha);

        batch.flush();   //测试在片段着色器之后，所以需要先flush之后在关闭模板测试


        Gdx.gl.glDisable(Gdx.gl.GL_STENCIL_TEST);
    }
}
···



