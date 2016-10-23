package com.fenghun.openglesdroid.jni.bean20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.jni.utils.GLES20Utils;

import android.opengl.GLES20;
import android.util.Log;

/**
 * 
 * @author Fenghun
 * 
 */
public class Triangle {

	private static String TAG = "Triangle";

	
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

	// 数组中每个顶点的坐标数
	private static final int COORDS_PER_VERTEX = 3;

	private final int vertexStride = COORDS_PER_VERTEX * 4;

	static float triangleCoords[] = { // 按逆时针方向顺序:
			0.0f, 0.622008459f, 0.0f, // top
			-0.5f, -0.311004243f, 0.0f, // bottom left
			0.5f, -0.311004243f, 0.0f, // bottom right
			
			0.1f, 0.622008459f, 0.0f, // top
			0.6f, 0.0f, 0.0f, // bottom left
			1.0f, 0.622008459f, 0.0f // bottom left
	};
	
	
//	static float triangleCoords[] = { // 按逆时针方向顺序:
//		-0.5f, 0.5f, 0.0f, // top
//		-0.5f, -0.5f, 0.0f, // bottom left
//		0.5f, 0.5f, 0.0f, // bottom right
//		
//	};
	

	// 设置颜色，分别为red, green, blue 和alpha (opacity)
	float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

	public Triangle() {
		// 为存放形状的坐标，初始化顶点字节缓冲
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (坐标数 * 4)float占四字节
				triangleCoords.length * 4);
		// 设用设备的本点字节序
		bb.order(ByteOrder.nativeOrder());

		// 从ByteBuffer创建一个浮点缓冲
		vertexBuffer = bb.asFloatBuffer();
		// 把坐标们加入FloatBuffer中
		vertexBuffer.put(triangleCoords);
		// 设置buffer，从第一个坐标开始读
		vertexBuffer.position(0);
		
		
		setVertexShader(GLES20Utils.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode)); // 加载顶点着色器
		setFragmentShader(GLES20Utils.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode)); // 加载片段着色器

	}

	public void draw(int mProgram, int mPositionHandle, int mColorHandle) {

		GLES20.glFrontFace(GLES20.GL_CCW);	// 设置逆时针方法为面的“前面”：
		
		GLES20.glEnable(GLES20.GL_CULL_FACE);	// 打开 忽略“后面”设置：);
		
		GLES20.glCullFace(GLES20.GL_BACK);	// 明确指明“忽略“哪个面的代码如下：
		
		
		// 启用一个指向三角形的顶点数组的handle
		GLES20.glEnableVertexAttribArray(mPositionHandle); //

		// 传入三角形的坐标数据
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
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		// 获取指向fragment shader的成员vColor的handle
		// mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// 设置三角形的颜色,Uniform常量
		GLES20.glUniform4fv(mColorHandle, 1, color, 0); //
		// 下面的命令也是相同的，只是输入参数一个是矢量，另一个不是
		// GLES20.glUniform4f(location, x, y, z, w);

		// 画三角形
		 GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
		//GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 6);
		//GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
		//GLES20.glDrawArrays(GLES20.GL_LINES, 0, 6);// GL_LINE_STRIP
		
		
		// 禁用指向三角形的顶点数组
		GLES20.glDisableVertexAttribArray(mPositionHandle);

		// Disable face culling.
		GLES20.glDisable(GLES20.GL_CULL_FACE); // 关闭 忽略“后面”设置：
		
		// 绘制三角形结束
		GLES20.glFinish();
		// 重置当前的模型观察矩阵
		// GLES20.glLoadIdentity();
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
