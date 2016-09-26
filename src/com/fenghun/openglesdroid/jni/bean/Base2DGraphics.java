package com.fenghun.openglesdroid.jni.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 基本的二维图像
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-9-27
 * @function
 */
public class Base2DGraphics {

	// 三个点坐标值，在xy平面上
	float[] vertexArrayPoints = new float[] {

	-0.8f, -0.4f * 1.732f, 0.0f,

	0.8f, -0.4f * 1.732f, 0.0f,

	0.0f, 0.4f * 1.732f, 0.0f, };

	FloatBuffer vertex_points_buffer; // 缓存

	// 定义四个点，在xy平面，用于绘制线段
	float vertexArrayLineSegments[] = {

	-0.8f, -0.4f * 1.732f, 0.0f,

	-0.4f, 0.4f * 1.732f, 0.0f,

	0.0f, -0.4f * 1.732f, 0.0f,

	0.4f, 0.4f * 1.732f, 0.0f,

	};

	int index = 0;

	FloatBuffer vertex_line_segments_buffer; // 缓存

	// 定义6个顶点使用三种不同模式来绘制三角形TRIANGLES
	float vertexArrayTriangles[] = { -0.8f, -0.4f * 1.732f, 0.0f, 0.0f,
			-0.4f * 1.732f, 0.0f, -0.4f, 0.4f * 1.732f, 0.0f,

			0.0f, -0.0f * 1.732f, 0.0f, 0.8f, -0.0f * 1.732f, 0.0f, 0.4f,
			0.4f * 1.732f, 0.0f, };
	FloatBuffer vertex_triangles_buffer;

	public Base2DGraphics() {
		// TODO Auto-generated constructor stub

		// 点坐标 缓存
		ByteBuffer vbbPoints = ByteBuffer
				.allocateDirect(vertexArrayPoints.length * 4);
		vbbPoints.order(ByteOrder.nativeOrder());
		vertex_points_buffer = vbbPoints.asFloatBuffer();
		vertex_points_buffer.put(vertexArrayPoints);
		vertex_points_buffer.position(0);

		// 线段坐标缓存
		ByteBuffer vbbLineSegments = ByteBuffer
				.allocateDirect(vertexArrayLineSegments.length * 4);
		vbbLineSegments.order(ByteOrder.nativeOrder());
		vertex_line_segments_buffer = vbbLineSegments.asFloatBuffer();
		vertex_line_segments_buffer.put(vertexArrayLineSegments);
		vertex_line_segments_buffer.position(0);

		// 三角形缓存
		ByteBuffer vbbTriangles = ByteBuffer
				.allocateDirect(vertexArrayTriangles.length * 4);
		vbbTriangles.order(ByteOrder.nativeOrder());
		vertex_triangles_buffer = vbbTriangles.asFloatBuffer();
		vertex_triangles_buffer.put(vertexArrayTriangles);
		vertex_triangles_buffer.position(0);

	}

	/**
	 * 绘制点
	 * 
	 * @param gl
	 */
	public void drawPoints(GL10 gl) {

		gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);// 蓝色
		gl.glPointSize(8f); // 用来设置绘制点的大小。
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -4); // 向z轴负方向平移4个单位
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// 开启点坐标传入

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex_points_buffer); // 传入点坐标
		gl.glDrawArrays(GL10.GL_POINTS, 0, 3); // 绘制点

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // 关闭点坐标传入
	}

	/**
	 * 绘制线段
	 * 
	 * @param gl
	 */
	public void drawLineSegments(GL10 gl) {
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -4);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex_line_segments_buffer);
		index++;
		index %= 3;

		if (index == 0) {
			gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
			gl.glDrawArrays(GL10.GL_LINES, 0, 4);
		} else if (index == 1) {
			gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
			gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4);
		} else {
			gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
			gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// switch (index) {
		// case 0:
		// case 1:
		// case 2:
		// gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		// gl.glDrawArrays(GL10.GL_LINES, 0, 4);
		// break;
		// case 3:
		// case 4:
		// case 5:
		// gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
		// gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 4);
		// break;
		// case 6:
		// case 7:
		// case 8:
		// case 9:
		// gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
		// gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
		// break;
		// }
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

	}

	/**
	 * 绘制三角形
	 * 
	 * @param gl
	 */
	public void drawTrangles(GL10 gl) {
		gl.glLoadIdentity();

		gl.glTranslatef(0, 0, -4);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex_triangles_buffer);

		index++;

		index %= 3;

		if (index == 0) {
			gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		} else if (index == 1) {
			gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 6);
		} else {
			gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// switch (index) {
		//
		// case 0:
		//
		// case 1:
		//
		// case 2:
		//
		// gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		//
		// gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
		//
		// break;
		//
		// case 3:
		//
		// case 4:
		//
		// case 5:
		//
		// gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
		//
		// gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 6);
		//
		// break;
		//
		// case 6:
		//
		// case 7:
		//
		// case 8:
		//
		// case 9:
		//
		// gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
		//
		// gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
		//
		// break;
		//
		// }

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
