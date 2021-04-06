package me.felixzhang.example.my_particles2.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static me.felixzhang.example.my_particles2.Constants.BYTE_PER_FLOAT;

/**
 * Created by felix on 15/5/19.
 */
public class VertexArray {

    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData) {
        this.floatBuffer = ByteBuffer.allocateDirect(vertexData.length * BYTE_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        floatBuffer.put(vertexData);
    }


    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }

    /**
     * 在原有数组的基础上更新指定范围的元素，如果全部复制的话速度太慢
     *
     * @param vertexData
     * @param start
     * @param count
     */
    public void updateBuffer(float[] vertexData, int start, int count) {
        floatBuffer.position(start);
        floatBuffer.put(vertexData, start, count);
        floatBuffer.position(0);
    }
}
