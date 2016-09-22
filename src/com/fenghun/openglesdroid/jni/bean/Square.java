package com.fenghun.openglesdroid.jni.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-9-23
 * @function 正方形
 */
public class Square {

	// 顶点信息，opengl 的坐标系与屏幕的坐标系不同，
	// opengl 的原点在画布的中心，向左为x正，向上为y正，向外围z正。（右手坐标系）
	private float vertices[] = { -1.0f, 1.0f, 0.0f, // 0, Top Left
			-1.0f, -1.0f, 0.0f, // 1, Bottom Left
			1.0f, -1.0f, 0.0f, // 2, Bottom Right
			1.0f, 1.0f, 0.0f // 3, Top Right
	};

	/**
	 * 定义面的顶点的顺序很重要 在拼接曲面的时候，用来定义面的顶点的顺序非常重要，
	 * 因为顶点的顺序定义了面的朝向（前向或是后向），为了获取绘制的高性能，一般情况不会绘制面的前面和后面，
	 * 只绘制面的“前面”。虽然“前面”“后面”的定义可以应人而易，但一般为所有的“前面”定义统一的顶点顺序(顺时针或是逆时针方向）。
	 */
	// The order we like to connect them.
	private short[] indices = { 0, 1, 2, 0, 2, 3 };	// 顶点绘制的顺序，

	// Our vertex buffer.
	private FloatBuffer vertexBuffer; // 浮点数数组通常放在一个Buffer（java.nio)中来提高性能。

	// Our index buffer.
	private ShortBuffer indexBuffer;

	public Square() {

		// a float is 4 bytes, therefore we
		// multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// short is 2 bytes, therefore we multiply
		// the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	/**
	 * 
	 * This function draws our square on screen.
	 * 
	 * @param gl
	 */
	public void draw(GL10 gl) {
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);	// 设置逆时针方法为面的“前面”：
		
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE);	// 打开 忽略“后面”设置：
		
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);	// 明确指明“忽略“哪个面的代码如下：
		
		
		//OpenGL ES提供一个成为”管道Pipeline”的机制，这个管道定义了一些“开关”来控制OpenGL ES支持的某些功能，
		// 缺省情况这些功能是关闭的，如果需要使用OpenGL ES的这些功能，需要明确告知OpenGL “管道”打开所需功能。
		// 因此对于我们的这个示例，需要告诉OpenGL库打开 Vertex buffer以便传入顶点坐标Buffer。
		// 要注意的使用完某个功能之后，要关闭这个功能以免影响后续操作：
		// Enabled the vertices buffer for writing and to be used during rendering.
		// 
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);	// 开启顶点传入开关

		// Specifies the location and data format of
		// an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);	// 传入顶点
		
		
		// glDrawArrays(int mode, int first, int count)   使用VetexBuffer 来绘制，顶点的顺序由vertexBuffer中的顺序指定。
	    // glDrawElements(int mode, int count, int type, Buffer indices)  ，可以重新定义顶点的顺序，顶点的顺序由indices Buffer 指定。
		// 绘制的几何图形的基本类型mode如下:
		// GL_POINTS 绘制独立的点。 GL_LINE_STRIP 绘制一系列线段。GL_LINE_LOOP 类同上，但是首尾相连，构成一个封闭曲线。
		// GL_LINES 顶点两两连接，为多条线段构成。GL_TRIANGLES 每隔三个顶点构成一个三角形，为多个三角形组成。
		// GL_TRIANGLE_STRIP 每相邻三个顶点组成一个三角形，为一系列相接三角形构成。
		// GL_TRIANGLE_FAN 以一个点为三角形公共顶点，组成一系列相邻的三角形。
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // 关闭顶点传入开关

		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE); // 关闭 忽略“后面”设置：
	}
}
