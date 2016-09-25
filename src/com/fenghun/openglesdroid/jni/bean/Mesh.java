package com.fenghun.openglesdroid.jni.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * 
 * 基类 Mesh,所有空间形体最基本的构成元素为Mesh（三角形网格）
 * 
 * @author fenghun@gmail.com
 * @date --
 * @function
 */
public class Mesh {

	// Our vertex buffer.顶点buffer
	private FloatBuffer verticesBuffer = null;

	// Our index buffer.绘制顺序buffer
	private ShortBuffer indicesBuffer = null;

	// The number of indices.
	private int numOfIndices = -1;

	// Flat Color
	private float[] rgba = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	// Smooth Colors
	private FloatBuffer colorBuffer = null;

	// Translate params.定义了平移变换的参数
	public float x = 0;
	public float y = 0;
	public float z = 0;

	// Rotate params.定义旋转变换的参数
	public float rx = 0;
	public float ry = 0;
	public float rz = 0;

	public void draw(GL10 gl) {

		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);

		// Enable face culling.
		//gl.glEnable(GL10.GL_CULL_FACE);

		// What faces to remove with the face culling.
		//gl.glCullFace(GL10.GL_BACK);

		// Enabled the vertices buffer for writing and
		// to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		// Specifies the location and data format
		// of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);

		// Set flat color
		gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);

		// Smooth color
		if (colorBuffer != null) {
			// Enable the color array buffer to be
			// used during rendering.
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		}

		gl.glTranslatef(x, y, z);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);

		
		Log.d("----------- numOfIndices = ", numOfIndices+",indicesBuffer.capacity()"+indicesBuffer.capacity());
		
		// Point out the where the color buffer is.
		gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
				GL10.GL_UNSIGNED_SHORT, indicesBuffer);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		// Disable face culling.
		//gl.glDisable(GL10.GL_CULL_FACE);

	}

	/**
	 * 允许子类重新定义顶点坐标
	 * @param vertices
	 */
	protected void setVertices(float[] vertices) {
		// a float is bytes, therefore
		// we multiply the number if
		// vertices with .
		ByteBuffer vbb= ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verticesBuffer = vbb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);
	}

	/**
	 * 允许子类重新定义顶点的顺序
	 * @param indices
	 */
	protected void setIndices(short[] indices) {

		// short is bytes, therefore we multiply
		// the number if
		// vertices with .
		ByteBuffer ibb= ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indicesBuffer = ibb.asShortBuffer();
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
		numOfIndices = indices.length;
	}

	/**
	 * 允许子类重新定义颜色
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	protected void setColor(float red, float green,float blue, float alpha) {
		// Setting the flat color.
		rgba[0] = red;
		rgba[1] = green;
		rgba[2] = blue;
		rgba[3] = alpha;
	}

	/**
	 * 允许子类重新定义颜色。
	 * @param colors
	 */
	protected void setColors(float[] colors) {

		// float has bytes.

		ByteBuffer cbb= ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}
}
