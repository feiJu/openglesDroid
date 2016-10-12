package com.fenghun.openglesdroid.jni.bean20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

/**
 * 正方形
 * @author Fenghun
 *
 */
public class Square {

	private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    // 每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    
    static float squareCoords[] = { -0.5f,  0.5f, 0.0f,   // top left 0
                                    -0.5f, -0.5f, 0.0f,   // bottom left	1
                                     0.5f, -0.5f, 0.0f,   // bottom right	2
                                     0.5f,  0.5f, 0.0f }; // top right	3

//    static float squareCoords[] = { -1.0f,  1.0f, 0.0f,   // top left 0
//        -1.0f, -1.0f, 0.0f,   // bottom left	1
//         1.0f, -1.0f, 0.0f,   // bottom right	2
//         1.0f,  1.0f, 0.0f }; // top right	3
    
    private short indices[] = { 0, 1, 2, 2, 3,0 }; // 顶点的绘制顺序
    
    // 设置颜色，分别为red, green, blue 和alpha (opacity)
 	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (坐标数 * 4)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // 为绘制列表初始化字节缓冲
        ByteBuffer dlb = ByteBuffer.allocateDirect(
        // (对应顺序的坐标数 * 2)short是2字节
        		indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }
    
    /**
     * 绘制函数
     * @param mColorHandle 
     * @param mPositionHandle 
     * @param mProgram 
     */
    public void draw(int mProgram, int mPositionHandle, int mColorHandle){

    	GLES20.glFrontFace(GLES20.GL_CCW);	// 设置逆时针方法为面的“前面”：
		
		GLES20.glEnable(GLES20.GL_CULL_FACE);	// 打开 忽略“后面”设置：);
		
		GLES20.glCullFace(GLES20.GL_BACK);	// 明确指明“忽略“哪个面的代码如下：
    	
		
	    // 启用一个指向三角形的顶点数组的handle
	    GLES20.glEnableVertexAttribArray(mPositionHandle);

	    // 准备三角形的坐标数据
	    /**
		 * index 定点属性索引值 0 到支持的最大顶点属性－1. 
		 * size 顶点属性的矩阵数组组成元素的索引，有效值是 1—4 
		 * type 数据格式，有效值： GL_BYTE GL_UNSIGNED_BYTE GL_SHORT GL_UNSIGNED_SHORT
		 * GL_FLOAT GL_FIXED GL_HALF_FLOAT_OES* 
		 * Normalized 用于指定非浮点数据格式转变成浮点型数据时是否标准化 
		 * stride 顶点属性的成分，指定相邻的数据存储间隔的空间大小。Stride
		 * 指定数据索引 I 和 I+1 的增量。如果为 0，数据连续存储，如果> 0，stride 值是数据和下一个数据增 量值
		 */

	    GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
	                                 GLES20.GL_FLOAT, false,
	                                 vertexStride, vertexBuffer);

	    // 获取指向fragment shader的成员vColor的handle 
	    // mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

	    // 设置三角形的颜色
	    GLES20.glUniform4fv(mColorHandle, 1, color, 0);

	    // 画正方形
	    //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
	    

	    // 禁用指向正方形的顶点数组
	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	    
	    // Disable face culling.
	 	GLES20.glDisable(GLES20.GL_CULL_FACE); // 关闭 忽略“后面”设置：
	 		
	 	// 绘制三角形结束
	 	GLES20.glFinish();
    }
}

