package com.fenghun.openglesdroid.jni.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

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

	private Star sun = new Star(); // 太阳
	
	private Star earth = new Star();	// 地球
	
	private Star moon = new Star();	// 月球

	private int angle = 0;
	
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
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	/**
	 * 绘制迷你太阳系
	 * 
	 * 太阳居中，逆时针自转。
	 * 地球绕太阳顺时针公转，本身不自转。旋转和平移操作的顺序影响实现的效果
	 * 月亮绕地球顺时针公转，自身逆时针自转。
	 * 
	 * @param gl
	 */
	public void drawMiniSolarSystem(GL10 gl) {
		gl.glLoadIdentity();	// 重置观察矩阵

		/**
		 * param1:eyex,eyey,eyez 指定观测点的空间坐标。（camera的位置）
    	 * param2:centerX,centerY,centerZ,指定被观测物体的参考点的坐标。
    	 * param3:upx,upy,upz 指定观测点方向为“上”的向量。
    	 * 
    	 * 注：这些坐标都采用世界坐标系。
		 */
		// 设置modelview变换矩阵，将摄像机摆放在z轴上正方向15个单位处，
		// 物体摆放在坐标原点处，
		// param3暂时没有理解是什么意思
		GLU.gluLookAt(gl, 0.0f, 0.0f, 15.0f,

		0.0f, 0.0f, 0.0f,

		0.0f, 1.0f, 0.0f);	

		// Star A
		// Save the current matrix.
		gl.glPushMatrix(); // 在栈中保存当前矩阵

		// Rotate Star A counter-clockwise.
		gl.glRotatef(angle, 0, 0, 1);	// 绕Z轴旋转
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);	// 红色，不透明
		// Draw Star A.
		sun.draw(gl);
		// Restore the last matrix.
		gl.glPopMatrix();	// 在从栈中恢复所存矩阵

		// Star B
		// Save the current matrix
		gl.glPushMatrix();

		
		/**
		 * 1.
		 * gl.glRotatef
		 * gl.glTranslatef
		 * 这样的代码顺序是先设置了旋转的中心点，然后平移，在矩阵变换时
		 * 先计算的平移的矩阵乘法，后计算的旋转的矩阵乘法，与代码顺序相反。
		 * 
		 * 2. 
		 * gl.glTranslatef
		 * gl.glRotatef
		 * 这样的代码顺序是先平移，然后以平移后的点为旋转中心，
		 * 在矩阵变换时，先计算的旋转矩阵乘法，在计算平移矩阵乘法，与代码执行顺序相反。
		 */
		// Rotate Star B before moving it,
		// making it rotate around A.
		gl.glRotatef(-angle, 0, 0, 1);	// 绕Z轴旋转，当前的状态是以（0,0,0）为中心
		// Move Star B.
		gl.glTranslatef(3, 0, 0);	// 在X轴正方向上平移3个单位

		// Scale it to 50% of Star A
		gl.glScalef(.5f, .5f, .5f);	// 缩小为A的一半
		gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);	// 蓝色不透明
		// Draw Star B.
		earth.draw(gl);

		// Star C
		// Save the current matrix
		gl.glPushMatrix();

		// Make the rotation around B
		gl.glRotatef(-angle, 0, 0, 1);	// 绕Z轴旋转，当前的旋转中心点在B旋转平移后的位置
		gl.glTranslatef(2, 0, 0);

		// Scale it to 50% of Star B
		gl.glScalef(.5f, .5f, .5f);

		// Rotate around it's own center.
		gl.glRotatef(angle * 10, 0, 0, 1);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		// Draw Star C.
		moon.draw(gl);

		// Restore to the matrix as it was before C.
		gl.glPopMatrix();

		// Restore to the matrix as it was before B.
		gl.glPopMatrix();

		// Increse the angle.
		angle++;
	}
}
