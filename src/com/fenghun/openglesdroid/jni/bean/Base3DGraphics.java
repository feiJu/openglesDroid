package com.fenghun.openglesdroid.jni.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 基本的3d图形类
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-9-27
 * @function
 */

public class Base3DGraphics {
	static final float X = .525731112119133606f;

	static final float Z = .850650808352039932f;

	// 正20面体的12个顶点
	static float vertices_20[] = new float[] {

	-X, 0.0f, Z, // 1

			X, 0.0f, Z, // 2

			-X, 0.0f, -Z, // 3

			X, 0.0f, -Z, // 4

			0.0f, Z, X, // 5

			0.0f, Z, -X, // 6

			0.0f, -Z, X, // 7

			0.0f, -Z, -X,// 8

			Z, X, 0.0f,// 9

			-Z, X, 0.0f, // 10

			Z, -X, 0.0f, // 11

			-Z, -X, 0.0f // 12

	};

	// 顶点的绘制顺序
	static short indices_20[] = new short[] {

	0, 4, 1,

	0, 9, 4,

	9, 5, 4,

	4, 5, 8,

	4, 8, 1,

	8, 10, 1,

	8, 3, 10,

	5, 3, 8,

	5, 2, 3,

	2, 7, 3,

	7, 10, 3,

	7, 6, 10,

	7, 11, 6,

	11, 0, 6,

	0, 1, 6,

	6, 1, 10,

	9, 0, 11,

	9, 11, 2,

	9, 2, 5,

	7, 2, 11 };

	// 为了能够更好的显示3D效果，我们为每个顶点随机定义一些颜色如下：
	float[] colors20 = {

	0f, 0f, 0f, 1f, // 1

			0f, 0f, 1f, 1f,// 2

			0f, 1f, 0f, 1f,// 3

			0f, 1f, 1f, 1f,// 4

			1f, 0f, 0f, 1f,// 5

			1f, 0f, 1f, 1f,// 6

			1f, 1f, 0f, 1f,// 7

			1f, 1f, 1f, 1f,// 8

			1f, 0f, 0f, 1f,// 9

			0f, 1f, 0f, 1f,// 10

			0f, 0f, 1f, 1f,// 11

			1f, 0f, 1f, 1f // 12
	};

	private FloatBuffer vertex20Buffer;

	private FloatBuffer color20Buffer;

	private ShortBuffer index20Buffer;
	
	private float angle=0f;

	public Base3DGraphics() {
		// TODO Auto-generated constructor stub

		// 顶点缓存
		ByteBuffer vbb20 = ByteBuffer.allocateDirect(vertices_20.length * 4);
		vbb20.order(ByteOrder.nativeOrder());
		vertex20Buffer = vbb20.asFloatBuffer();
		vertex20Buffer.put(vertices_20);
		vertex20Buffer.position(0);

		// 颜色缓存
		ByteBuffer cbb20 = ByteBuffer.allocateDirect(colors20.length * 4);
		cbb20.order(ByteOrder.nativeOrder());
		color20Buffer = cbb20.asFloatBuffer();
		color20Buffer.put(colors20);
		color20Buffer.position(0);

		// 顶点绘制顺序缓存
		ByteBuffer ibb20 = ByteBuffer.allocateDirect(indices_20.length * 2);
		ibb20.order(ByteOrder.nativeOrder());
		index20Buffer = ibb20.asShortBuffer();
		index20Buffer.put(indices_20);
		index20Buffer.position(0);
	}

	/**
	 * 绘制正20面体
	 * 
	 * @param gl
	 */
	public void drawPositive20surface(GL10 gl) {

		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);

		gl.glLoadIdentity(); // 重置当前模型的观察矩阵

		gl.glTranslatef(0, 0, -4); 	// 向z轴负方向平移4个单位

		gl.glRotatef(0f, 1, 0, 0);	// 绕x轴旋转0
		gl.glRotatef(-angle, 0, 1, 0);	// 绕y轴旋转
		gl.glRotatef(-angle, 0, 0, 1);	// 绕z轴旋转
		
		
		gl.glFrontFace(GL10.GL_CCW);	// 设置逆时针方法为面的“前面”

		gl.glEnable(GL10.GL_CULL_FACE); // 开启 面忽略设置

		gl.glCullFace(GL10.GL_BACK);	// 指明忽略后面

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);	// 开启顶点数组传入功能

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex20Buffer);// 传入顶点数组

		gl.glEnableClientState(GL10.GL_COLOR_ARRAY); // 开启颜色数组传入功能

		gl.glColorPointer(4, GL10.GL_FLOAT, 0, color20Buffer);	// 传入颜色顶点数组

		gl.glDrawElements(GL10.GL_TRIANGLES, indices_20.length,

		GL10.GL_UNSIGNED_SHORT, index20Buffer);	// 以indices_20的顺序绘制顶点，

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);	// 关闭顶点数组传入功能
		
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);	// 关闭颜色数组传入功能

		gl.glDisable(GL10.GL_CULL_FACE);	// 关闭面忽略功能

		// 改变旋转角度
		angle++;
		angle = angle%360;
	}
}
