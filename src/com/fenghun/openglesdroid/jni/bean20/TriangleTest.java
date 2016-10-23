package com.fenghun.openglesdroid.jni.bean20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * 
 * 
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-10-14
 * @function
 */
public class TriangleTest {


	final String vertexShader =
		    "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
		 
		  + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
		  + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
		 
		  + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader. 
		  											// 顶点着色器的输出变量，供片段着色器使用，必须保证与片段着色器定义相同
		 
		  + "void main()                    \n"     // The entry point for our vertex shader.
		  + "{                              \n"
		  + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
		                                            // It will be interpolated across the triangle.
		  + "   gl_Position = u_MVPMatrix   \n"     // gl_Position is a special variable used to store the final position.
		  + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
		  + "}                              \n";    // normalized screen coordinates.矢量和矩阵相乘获取最终的归一化屏幕坐标系中的点
	
	final String fragmentShader =
		    "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
		                                            // precision in the fragment shader.
		  + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
		                                            // triangle per fragment.接收顶点着色器的输入变量，与顶点着色器相对应
		  + "void main()                    \n"     // The entry point for our fragment shader.
		  + "{                              \n"
		  + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline. 直接将颜色输入给管道
		  + "}                              \n";
	
	/**
	 * Store our model data in a float buffer. 用float buffer 存储模型数据
	 * 
	 */
	private final FloatBuffer mTriangle1Vertices;
	private final FloatBuffer mTriangle2Vertices;
	private final FloatBuffer mTriangle3Vertices;

	/**
	 * Allocate storage for the final combined matrix. This will be passed into
	 * the shader program.
	 */
	private float[] mMVPMatrix = new float[16];

	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;

	/** How many elements per vertex. */
	private final int mStrideBytes = 7 * mBytesPerFloat;

	/** Offset of the position data. */
	private final int mPositionOffset = 0;

	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;

	/** Offset of the color data. */
	private final int mColorOffset = 3;

	/** Size of the color data in elements. */
	private final int mColorDataSize = 4;

	public TriangleTest() {
		// This triangle is red, green, and blue.
		final float[] triangle1VerticesData = {
				// X, Y, Z,
				// R, G, B, A
				-0.5f, -0.25f, 0.0f, // 坐标值
				1.0f, 0.0f, 0.0f, 1.0f, // 颜色值

				0.5f, -0.25f, 0.0f, // 坐标值
				0.0f, 0.0f, 1.0f, 1.0f, // 颜色值

				0.0f, 0.559016994f, 0.0f, // 坐标值
				0.0f, 1.0f, 0.0f, 1.0f // 颜色值

		};
		
		final float[] triangle2VerticesData = {
				// X, Y, Z,
				// R, G, B, A
				-0.5f, -0.25f, 0.0f, // 坐标值
				1.0f, 1.0f, 0.0f, 1.0f, // 颜色值

				0.5f, -0.25f, 0.0f, // 坐标值
				0.0f, 1.0f, 1.0f, 1.0f, // 颜色值

				0.0f, 0.559016994f, 0.0f, // 坐标值
				1.0f, 0.0f, 1.0f, 1.0f // 颜色值

		};
		
		final float[] triangle3VerticesData = {
				// X, Y, Z,
				// R, G, B, A
				-0.5f, -0.25f, 0.0f, // 坐标值
				1.0f, 1.0f, 1.0f, 1.0f, // 颜色值

				0.5f, -0.25f, 0.0f, // 坐标值
				0.5f, 0.5f, 0.5f, 1.0f, // 颜色值

				0.0f, 0.559016994f, 0.0f, // 坐标值
				0.0f, 0.0f, 0.0f, 1.0f // 颜色值

		};
		

		// Initialize the buffers. 初始化缓存
		mTriangle1Vertices = ByteBuffer
				.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTriangle1Vertices.put(triangle1VerticesData).position(0);

		 mTriangle2Vertices = ByteBuffer
		 .allocateDirect(triangle2VerticesData.length * mBytesPerFloat)
		 .order(ByteOrder.nativeOrder()).asFloatBuffer();
		 mTriangle2Vertices.put(triangle2VerticesData).position(0);
		 
		 mTriangle3Vertices = ByteBuffer
				 .allocateDirect(triangle3VerticesData.length * mBytesPerFloat)
				 .order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTriangle3Vertices.put(triangle3VerticesData).position(0);

	}

	/**
	 * Draws a triangle from the given vertex data.
	 * 
	 * @param aTriangleBuffer
	 *            The buffer containing the vertex data.
	 */
	public void drawTriangle(final FloatBuffer aTriangleBuffer,
			int mPositionHandle, int mColorHandle, float[] mViewMatrix,
			float[] mModelMatrix, float[] mProjectionMatrix,
			int mMVPMatrixHandle) {
		// Pass in the position information
		aTriangleBuffer.position(mPositionOffset);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		aTriangleBuffer.position(mColorOffset);
		GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);

		GLES20.glEnableVertexAttribArray(mColorHandle);

		// This multiplies the view matrix by the model matrix, and stores the
		// result in the MVP matrix
		// (which currently contains model * view).
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

		// This multiplies the modelview matrix by the projection matrix, and
		// stores the result in the MVP matrix
		// (which now contains model * view * projection).
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
	}

	public FloatBuffer getmTriangle1Vertices() {
		return mTriangle1Vertices;
	}

	public FloatBuffer getmTriangle2Vertices() {
		return mTriangle2Vertices;
	}

	public FloatBuffer getmTriangle3Vertices() {
		return mTriangle3Vertices;
	}

	public String getVertexShader() {
		return vertexShader;
	}

	public String getFragmentShader() {
		return fragmentShader;
	}
	
}
