package me.felixzhang.example.my_particles2.objects;

import android.graphics.Color;

import me.felixzhang.example.my_particles2.data.VertexArray;
import me.felixzhang.example.my_particles2.programs.ParticlesShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static me.felixzhang.example.my_particles2.Constants.BYTE_PER_FLOAT;
import static me.felixzhang.example.my_particles2.util.Geometry.Point;
import static me.felixzhang.example.my_particles2.util.Geometry.Vector;

/**
 * Created by felix on 15/5/19.
 */
public class ParticlsSystem {
    private static int POSITION_COMPONENT_COUNT = 3;
    private static int COLOR_COMPONENT_COUNT = 3;
    private static int VECTOR_COMPONENT_COUNT = 3;
    private static int PARTICLE_START_TIME_COMPONENT_COUNT = 1;

    private static int TOTAL_COMPONENT_COUNT =
            POSITION_COMPONENT_COUNT +
                    COLOR_COMPONENT_COUNT +
                    VECTOR_COMPONENT_COUNT +
                    POSITION_COMPONENT_COUNT;
    private static int STRIDE = TOTAL_COMPONENT_COUNT * BYTE_PER_FLOAT;


    private VertexArray vertexArray;
    private final float[] particles;
    private int maxParticlesCount;
    private int currentParticlesCount; //当前粒子的总数

    private int nextParticleOffset = 0;  //下一个例子放在数组的位置


    /**
     * @param maxParticlesCount 粒子系统中能容纳的最多的粒子数
     */
    public ParticlsSystem(int maxParticlesCount) {
        this.maxParticlesCount = maxParticlesCount;
        particles = new float[maxParticlesCount * TOTAL_COMPONENT_COUNT];
        this.vertexArray = new VertexArray(particles);
    }


    /**
     * 向系统中添加粒子，每次添加一个
     *
     * @param positionPoint 新加粒子的位置
     */
    public void addParticles(Point positionPoint, int color, Vector direction, float particleStrtTime) {

        final int particleOffset = nextParticleOffset * TOTAL_COMPONENT_COUNT;  //记住新粒子从数组的哪个编号开始
        int currentOffset = particleOffset;                       //记住新粒子的每个属性从哪里开始

        nextParticleOffset++;
        if (currentParticlesCount < maxParticlesCount) {
            currentParticlesCount++;
        }
        //当超出数组范围时，将下一个粒子放在数组的开头位置，达到回收的目的
        if (nextParticleOffset == maxParticlesCount) {
            nextParticleOffset = 0;
        }


        //把新粒子的数据写到数组中
        particles[currentOffset++] = positionPoint.x;
        particles[currentOffset++] = positionPoint.y;
        particles[currentOffset++] = positionPoint.z;

        particles[currentOffset++] = Color.red(color) / 255f;       //OpenGL需要[0,1)的颜色值
        particles[currentOffset++] = Color.green(color) / 255f;
        particles[currentOffset++] = Color.blue(color) / 255f;


        particles[currentOffset++] = direction.x;
        particles[currentOffset++] = direction.y;
        particles[currentOffset++] = direction.z;


        particles[currentOffset++] = particleStrtTime;

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }


    /**
     * 将着色器中的属性与数据联系起来，这样OpenGL才知道去哪里找数据
     */
    public void bindData(ParticlesShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaPositionLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);

        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaColorLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaDirectionVectorLocation(),
                VECTOR_COMPONENT_COUNT,
                STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;


        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getaParticleStartTimeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT,
                STRIDE);
    }


    public void draw() {
        glDrawArrays(GL_POINTS, 0, currentParticlesCount);
    }


}
