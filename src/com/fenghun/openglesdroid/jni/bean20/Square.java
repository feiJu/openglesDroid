package com.fenghun.openglesdroid.jni.bean20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.jni.utils.GLES20Utils;

import android.opengl.GLES20;

/**
 * 正方形
 * @author Fenghun
 *
 */
public class Square {

	
	/**
	 * OpenGL ES 2.0 中，在有效的顶点着色器和片段着色器被装载前，什么渲染都做不了。
	 * 我们介绍管线时介绍了顶点着色器和片段着色器，做任何渲染前，必须有顶点和片段着色器。
	 */
	// 顶点着色器，
	private final String vertexShaderCode = "attribute vec4 vPosition;" // 输入属性，4个成员矢量的vPosition
			+ "void main() {" // 主函数声明着色器宣布着色器开始执行
			+ "  gl_Position = vPosition;" + "}";

	// 片段着色器
	private final String fragmentShaderCode = "precision mediump float;" // 声明着色器默认的浮点变量精度
			+ "uniform vec4 vColor;"
			+ "void main() {"
			+ "  gl_FragColor = vColor;" // gl_FragColor是片段着色器最终的输出值
			+ "}";
	
	private int vertexShader;
	
	private int fragmentShader;
	
	private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    
    private FloatBuffer vertexBuffer2;
    private ShortBuffer drawListBuffer2;

    // 每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    
    static float squareCoords[] = { -0.5f,  0.5f, 0.0f,   // top left 0
                                    -0.5f, -0.5f, 0.0f,   // bottom left	1
                                     0.5f, -0.5f, 0.0f,   // bottom right	2
                                     0.5f,  0.5f, 0.0f }; // top right	3

    static float squareCoords2[] = { -0.25f,  0.25f, 0.0f,   // top left 0
        -0.25f, -0.25f, 0.0f,   // bottom left	1
        0.25f, -0.25f, 0.0f,   // bottom right	2
        0.25f,  0.25f, 0.0f }; // top right	3
    
    private short indices[] = { 0, 1, 2, 2, 3,0 }; // 顶点的绘制顺序
    
    private short indices2[] = { 0, 1, 2, 2, 3,0 }; // 顶点的绘制顺序
    
    
    // 设置颜色，分别为red, green, blue 和alpha (opacity)
 	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
 	
 	float color2[] = { 1.0f, 0.0f, 0.0f, 1.0f };

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
        
        
     // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb2 = ByteBuffer.allocateDirect(
        // (坐标数 * 4)
                squareCoords2.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        vertexBuffer2 = bb2.asFloatBuffer();
        vertexBuffer2.put(squareCoords2);
        vertexBuffer2.position(0);

        // 为绘制列表初始化字节缓冲
        ByteBuffer dlb2 = ByteBuffer.allocateDirect(
        // (对应顺序的坐标数 * 2)short是2字节
        		indices2.length * 2);
        dlb2.order(ByteOrder.nativeOrder());
        drawListBuffer2 = dlb2.asShortBuffer();
        drawListBuffer2.put(indices2);
        drawListBuffer2.position(0);
        
        
        setVertexShader(GLES20Utils.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode)); // 加载顶点着色器
		setFragmentShader(GLES20Utils.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode)); // 加载片段着色器
        
    }
    
    /**
     * 绘制函数
     * @param mColorHandle 
     * @param mPositionHandle 
     * @param mProgram 
     */
    public void draw(int mProgram){
    	
    	GLES20.glFrontFace(GLES20.GL_CCW);	// 设置逆时针方法为面的“前面”：
		
		GLES20.glEnable(GLES20.GL_CULL_FACE);	// 打开 忽略“后面”设置：);
		
		GLES20.glCullFace(GLES20.GL_BACK);	// 明确指明“忽略“哪个面的代码如下：
    	
		
	    int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	    if (mColorHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for vColor");
		}
	  
		// 获取指向vertex shader的成员vPosition的 handle,（这里直接获取的返回值为glBindAttribLocation中的第二个参数,
		//并完成glBindAttribLocation操作） 
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		if (mPositionHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for vPosition");
		}
		
		
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

	    // 画正方形，这里绘制出来的正方形不规则，待解决。。。
	    // 有关不规则的问题：是opengl的绘制坐标系与屏幕的坐标系不成比例造成的正方形变形。
	    //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
	    
	    // 深度测试
	    GLES20.glDepthFunc(GLES20.GL_LEQUAL);
	    
	    GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer2);
	    
	    // 设置三角形的颜色
	    GLES20.glUniform4fv(mColorHandle, 1, color2, 0);
	    GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices2.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer2);

	    // 禁用指向正方形的顶点数组
	    GLES20.glDisableVertexAttribArray(mPositionHandle);
	    
	    // Disable face culling.
	 	GLES20.glDisable(GLES20.GL_CULL_FACE); // 关闭 忽略“后面”设置：
	 		
	 	// 绘制三角形结束
	 	GLES20.glFinish();
    }

	public String getVertexShaderCode() {
		return vertexShaderCode;
	}

	public String getFragmentShaderCode() {
		return fragmentShaderCode;
	}

	public int getVertexShader() {
		return vertexShader;
	}

	public void setVertexShader(int vertexShader) {
		this.vertexShader = vertexShader;
	}

	public int getFragmentShader() {
		return fragmentShader;
	}

	public void setFragmentShader(int fragmentShader) {
		this.fragmentShader = fragmentShader;
	}
}

