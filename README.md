# open-gl-es

## 绘制

### 绘制三角形

绘制的三角形，并不是等腰的三角形，即使写的坐标是，可以使用矩阵变换，或者使用相机。

相机位置：在3D种的位置
观察方向：
相机的UP方向：

```java
Matrix.setLookAtM (float[] rm,      //接收相机变换矩阵
                int rmOffset,       //变换矩阵的起始位置（偏移量）
                float eyeX,float eyeY, float eyeZ,   //相机位置
                float centerX,float centerY,float centerZ,  //观测点位置
                float upX,float upY,float upZ)  //up向量在xyz上的分量
```

改变尺寸的时候进行设置矩阵，对坐标进行矩阵变换。



、冷色调效果、暖色调效果、模糊效果、放大镜效果























