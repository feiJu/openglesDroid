package com.fenghun.openglesdroid.jni.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

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

	// Our UV texture buffer.
	private FloatBuffer mTextureBuffer; // 材质坐标buffer

	// Our texture id.
	private int mTextureId = -1; // 材质id

	// The bitmap we want to load as a texture.
	private Bitmap mBitmap; //

	// Indicates if we need to load the texture.
	private boolean mShouldLoadTexture = false;

	/**
	 * 
	 * Set the bitmap to load into a texture.
	 * 
	 * @param bitmap
	 */

	public void loadBitmap(Bitmap bitmap) {

		this.mBitmap = bitmap;

		mShouldLoadTexture = true;

	}

	/**
	 * 
	 * Loads the texture.
	 * 
	 * @param gl
	 */

	private void loadGLTexture(GL10 gl) {

		// Generate one texture pointer...

		int[] textures = new int[1];	// 材质ID数组

		gl.glGenTextures(1, textures, 0);	// 获取一个材质ID
		//textures中存放了创建的Texture ID，使用同样的Texture Id ，也可以来删除一个Texture：
		// Delete a texture.
		// gl.glDeleteTextures(1, textures, 0)
		mTextureId = textures[0];

		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);	// 绑定指定id的材质

		// Create Nearest Filtered Texture
		// 渲染Texture方式，GL10.GL_LINEAR和GL10.GL_NEAREST
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR); // 材质比渲染区域小时的渲染方式

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR); // 材质比渲染区域大时的渲染方式

		// 如果材质
		// Different possible texture parameters, 
		// e.g. 
		// GL10.GL_CLAMP_TO_EDGE 只靠边线绘制一次。
		// GL_REPEAT 重复绘制
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT); // 横向

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);	// 纵向

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);

	}

	public void draw(GL10 gl) {

		//gl.glLoadIdentity();// 重置当前的模型观察矩阵
		
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
		/**
		 * size: 每个顶点坐标维数，可以为2，3，4。
		 * type: 顶点的数据类型，可以为GL_BYTE, GL_SHORT, GL_FIXED,或 GL_FLOAT，缺省为浮点类型GL_FLOAT。
		 * stride: 每个相邻顶点之间在数组中的间隔（字节数），缺省为0，表示顶点存储之间无间隔。
		 * pointer: 存储顶点的数组。
		 * 
		 */
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

		if (mShouldLoadTexture) {
			loadGLTexture(gl);
			mShouldLoadTexture = false;
		}

		if (mTextureId != -1 && mTextureBuffer != null) {

			gl.glEnable(GL10.GL_TEXTURE_2D);

			// Enable the texture state
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

			// Point to our buffers
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);

			gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
		}

		gl.glTranslatef(x, y, z);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);

		// Point out the where the color buffer is.
		gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
				GL10.GL_UNSIGNED_SHORT, indicesBuffer);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		// Disable the colors buffer.
		if (colorBuffer != null) {
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
		
		// Disable face culling.
		// gl.glDisable(GL10.GL_CULL_FACE);

	}

	/**
	 * 允许子类重新定义顶点坐标
	 * 
	 * @param vertices
	 */
	protected void setVertices(float[] vertices) {
		// a float is bytes, therefore
		// we multiply the number if
		// vertices with .
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verticesBuffer = vbb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);
	}

	/**
	 * 允许子类重新定义顶点的顺序
	 * 
	 * @param indices
	 */
	protected void setIndices(short[] indices) {

		// short is bytes, therefore we multiply
		// the number if
		// vertices with .
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indicesBuffer = ibb.asShortBuffer();
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
		numOfIndices = indices.length;
	}

	/**
	 * 允许子类重新定义颜色
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	protected void setColor(float red, float green, float blue, float alpha) {
		// Setting the flat color.
		rgba[0] = red;
		rgba[1] = green;
		rgba[2] = blue;
		rgba[3] = alpha;
	}

	/**
	 * 允许子类重新定义颜色。
	 * 
	 * @param colors
	 */
	protected void setColors(float[] colors) {

		// float has bytes.

		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	/**
	 * 
	 * Set the texture coordinates.
	 * 
	 * 设置材质坐标
	 * 
	 * @param textureCoords
	 */

	protected void setTextureCoordinates(float[] textureCoords) {

		// float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer byteBuf = ByteBuffer
				.allocateDirect(textureCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		mTextureBuffer = byteBuf.asFloatBuffer();
		mTextureBuffer.put(textureCoords);
		mTextureBuffer.position(0);
	}
}
