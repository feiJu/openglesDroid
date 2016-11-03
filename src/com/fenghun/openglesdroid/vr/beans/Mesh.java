package com.fenghun.openglesdroid.vr.beans;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.fenghun.openglesdroid.R;
import com.fenghun.openglesdroid.jni.utils.GLES20Utils;

/**
 * opengl es 2.0 基类 Mesh,所有空间形体最基本的构成元素为Mesh（三角形网格）
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-10-28
 * @function
 */
public class Mesh {

	protected String ATTR_POSITION = "a_Position";
	
	protected String ATTR_COLOR = "a_Color";
	
	protected String ATTR_TEXTURE_COORDINATE = "a_TexCoordinate";
	
	protected String UNIFORM_MVP_MATRIX = "u_MVPMatrix";
	
	protected String UNIFORM_TEXTURE = "u_Texture";
	
	/** Size of the texture coordinate data in elements. */
	private final int TEXTURE_COORDINATE_DATA_SIZE = 2;

	/** Additional constants. */
	private final int POSITION_DATA_SIZE_IN_ELEMENTS = 3;

	// private static final int NORMAL_DATA_SIZE_IN_ELEMENTS = 3;
	private final int COLOR_DATA_SIZE_IN_ELEMENTS = 4;

	// 存储顶点相关句柄
	protected int[] vertexBufferObjs = new int[3]; // VBOs
	// 存储顶点顺序相关句柄
	protected int[] indexBufferObj = new int[1]; // IBO;

	protected int vertexCoordinatesIndex = -1;
	protected int vertexColorsIndex = -1;
	protected int vertexTextureCoordinatesIndex = -1;
	protected int indicesCoordinatesIndex = -1;

	// private float[] verticesCoordinates; // 顶点坐标信息
	// private float[] verticesColors; // 顶点颜色信息
	// private float[] rectTextureCoordinateData; // 材质坐标信息

	private FloatBuffer verCoordinatesBuffer = null;
	private FloatBuffer verColorBuffer = null;
	private FloatBuffer verTextureCoordinatesBuffer = null;
	private ShortBuffer verIndicesBuffer = null;
	private int numOfIndices = 0;

	/**
	 * 定义面的顶点的顺序很重要 在拼接曲面的时候，用来定义面的顶点的顺序非常重要，
	 * 因为顶点的顺序定义了面的朝向（前向或是后向），为了获取绘制的高性能，一般情况不会绘制面的前面和后面，
	 * 只绘制面的“前面”。虽然“前面”“后面”的定义可以应人而易，但一般为所有的“前面”定义统一的顶点顺序(顺时针或是逆时针方向）。
	 */
	// The order we like to connect them.
	// private short[] indicesCoordinates;

	/**
	 * Store the projection matrix. This is used to project the scene onto a 2D
	 * viewport. 透视投影矩阵，用于投影场景到屏幕
	 */
	protected float[] mProjectionMatrix = new float[16];

	/**
	 * Allocate storage for the final combined matrix. This will be passed into
	 * the shader program.
	 */
	private float[] mMVPMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix
	 * transforms world space to eye space; it positions things relative to our
	 * eye.
	 */
	private float[] mViewMatrix = new float[16];

	protected int mProgramHandle;

	private int mMVPMatrixHandle = -1;

	private int mPositionHandle = -1;

	private int mColorHandle = -1;

	/** This will be used to pass in model texture coordinate information. */
	private int mTextureCoordinateHandle = -1;

	/** This will be used to pass in the texture. */
	private int mTextureUniformHandle = -1;

	private int mOneFrameDataHandle = -1;

	/**
	 * 设置摄像头View
	 */
	public void setLookAtM(float eyeX,float eyeY,float eyeZ,float lookX,float lookY,
			float lookZ,float upX,float upY,float upZ) {

		// Position the eye behind the origin.
//		final float eyeX = 0.0f;
//		final float eyeY = 0.0f;
//		final float eyeZ = 1.0f;
//
//		// We are looking toward the distance
//		final float lookX = 0.0f;
//		final float lookY = 0.0f;
//		final float lookZ = -5.0f;
//
//		// Set our up vector. This is where our head would be pointing were we
//		// holding the camera.
//		final float upX = 0.0f;
//		final float upY = 1.0f;
//		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera
		// position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination
		// of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices
		// separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY,
				lookZ, upX, upY, upZ);
	}

	/**
	 * 设置投影矩阵
	 * 
	 * @param width
	 * @param height
	 */
	public void setProjectionMatrix(int width, int height) {

		GLES20.glViewport(0, 0, width, height);
		// Create a new perspective projection matrix. The height will stay the
		// same
		// while the width will vary as per aspect ratio.
		// 创建一个透视投影矩阵，高度保持一致，宽度则按比例计算
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -20.0f;
		final float top = 20.0f;
		final float near = 1.0f;
		final float far = 1000.0f;
		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near,
				far);
	}
	
	/**
	 * 设置投影矩阵
	 * 
	 * @param width
	 * @param height
	 */
	public void setProjectionMatrix(int width, int height,float left,float right,
			float bottom,float top,float near,float far) {

		GLES20.glViewport(0, 0, width, height);
		// Create a new perspective projection matrix. The height will stay the
		// same
		// while the width will vary as per aspect ratio.
		// 创建一个透视投影矩阵，高度保持一致，宽度则按比例计算
		//final float ratio = (float) width / height;
		//final float left = -ratio;
		//final float right = ratio;
		//final float bottom = -1.0f;
		//final float top = 1.0f;
		//final float near = 1.0f;
		//final float far = 100.0f;
		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near,
				far);
	}
	

	/**
	 * 初始化shader
	 */
	protected void initShader(Context context,int verShaderRawID,int frgShaderRawID,String[] attr_str) {

		final String vertexShader = GLES20Utils.readTextFileFromRawResource(
				context,verShaderRawID);
		final String fragmentShader = GLES20Utils.readTextFileFromRawResource(
				context, frgShaderRawID);

		final int vertexShaderHandle = GLES20Utils.loadShader(
				GLES20.GL_VERTEX_SHADER, vertexShader);
		final int fragmentShaderHandle = GLES20Utils.loadShader(
				GLES20.GL_FRAGMENT_SHADER, fragmentShader);

		mProgramHandle = GLES20Utils.createAndLinkProgram(vertexShaderHandle,
				fragmentShaderHandle, attr_str);

		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle,
				UNIFORM_MVP_MATRIX);
		if (mMVPMatrixHandle == -1) {
			throw new RuntimeException(
					"Could not get uniform location for u_MVPMatrix");
		}

		if (verCoordinatesBuffer != null) {
			mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle,
					ATTR_POSITION);
			if (mPositionHandle == -1) {
				throw new RuntimeException(
						"Could not get attrib location for a_Position");
			}
		}

		if (verColorBuffer != null) {
			mColorHandle = GLES20
					.glGetAttribLocation(mProgramHandle, ATTR_COLOR);
			if (mColorHandle == -1) {
				throw new RuntimeException(
						"Could not get attrib location for a_Color");
			}
		}

		if (verTextureCoordinatesBuffer != null) {
			
			// 加载贴图数据
			mOneFrameDataHandle = GLES20Utils.loadTexture(context,
					R.drawable.sea360);
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOneFrameDataHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOneFrameDataHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			
//			
//			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOneFrameDataHandle);
//			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_MIRRORED_REPEAT);
//			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOneFrameDataHandle);
//			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_MIRRORED_REPEAT);
			
			mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle,
					UNIFORM_TEXTURE);
			if (mTextureUniformHandle == -1) {
				throw new RuntimeException(
						"Could not get uniform location for u_Texture");
			}

			mTextureCoordinateHandle = GLES20.glGetAttribLocation(
					mProgramHandle, ATTR_TEXTURE_COORDINATE);
			if (mTextureCoordinateHandle == -1) {
				throw new RuntimeException(
						"Could not get attrib location for a_TexCoordinate");
			}
		}
	}

	protected void render(float[] mModelMatrix) {

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		// Set our per-vertex lighting program.
		GLES20.glUseProgram(mProgramHandle);

		// // Draw the triangle facing straight on.
		// Matrix.setIdentityM(mModelMatrix, 0);
		//
		// if (isLandscape) {
		// translateZ = 0.0f;
		// } else {
		// translateZ = -4.0f;
		// }
		// Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, translateZ);
		// Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
		// This multiplies the view matrix by the model matrix, and stores the
		// result in the MVP matrix
		// (which currently contains model * view).
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		// This multiplies the modelview matrix by the projection matrix, and
		// stores the result in the MVP matrix
		// (which now contains model * view * projection).
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		// Pass in the texture information
		// Set the active texture unit to texture unit 0.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOneFrameDataHandle);
		// Tell the texture uniform sampler to use this texture in the
		// shader by binding to texture unit 0.
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		if (vertexCoordinatesIndex > 0) {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexCoordinatesIndex); // 绑定句柄
			GLES20.glEnableVertexAttribArray(mPositionHandle); // 开启顶点位置属性传入
			GLES20.glVertexAttribPointer(mPositionHandle,
					POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, 0,
					0);
		}

		if (vertexColorsIndex > 0) {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexColorsIndex); // 绑定句柄
			GLES20.glEnableVertexAttribArray(mColorHandle);
			GLES20.glVertexAttribPointer(mColorHandle,
					COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, 0, 0);
		}
		if (vertexTextureCoordinatesIndex > 0) {
			// Pass in the texture information
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
					vertexTextureCoordinatesIndex);
			GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
			GLES20.glVertexAttribPointer(mTextureCoordinateHandle,
					TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);
		}

		// Draw
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
				indicesCoordinatesIndex);// 绑定绘制顺序数据缓存
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, numOfIndices,
				GLES20.GL_UNSIGNED_SHORT, 0);

//		System.out.println("--- mPositionHandle==" + mPositionHandle
//				+ ",mColorAttributeHandle="  + mColorHandle 
//				+ ",mTextureCoordinateHandle=" + mTextureCoordinateHandle);
		if(mPositionHandle > -1)GLES20.glDisableVertexAttribArray(mPositionHandle); // 关闭顶点位置属性传入
		if(mColorHandle > -1) GLES20.glDisableVertexAttribArray(mColorHandle); //关闭顶点位置属性传入
		if(mTextureCoordinateHandle > -1) GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle); // 关闭顶点位置属性传入
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	/**
	 * 
	 * 将客户端内存传入GPU缓存
	 * 
	 * @param indicesCoordinates
	 * @param rectTextureCoordinateData
	 * @param verticesColors
	 * @param verticesCoordinates
	 * 
	 * @param vertexBufferObjs
	 * @param indexBufferObj
	 * @throws Exception
	 */
	protected void transformData2GPU() throws Exception {

		// GPU缓存
		// Second, copy these buffers into OpenGL's memory. After, we don't need
		// to keep the client-side buffers around.
		// 初始化内存句柄
		GLES20.glGenBuffers(vertexBufferObjs.length, vertexBufferObjs, 0);
		GLES20.glGenBuffers(indexBufferObj.length, indexBufferObj, 0);

		boolean genVerBuff = true;
		for (int i = 0; i < vertexBufferObjs.length; i++) {
			if (!(vertexBufferObjs[i] > 0)) {
				genVerBuff = false;
				break;
			}
		}

		if (genVerBuff && indexBufferObj[0] > 0) {

			if (verCoordinatesBuffer != null)
				vertexCoordinatesIndex = GLES20Utils.allocateFloatGPUBuffer(
						verCoordinatesBuffer, vertexBufferObjs, 0);

			if (verColorBuffer != null)
				vertexColorsIndex = GLES20Utils.allocateFloatGPUBuffer(
						verColorBuffer, vertexBufferObjs, 1);

			if (verTextureCoordinatesBuffer != null)
				vertexTextureCoordinatesIndex = GLES20Utils
						.allocateFloatGPUBuffer(verTextureCoordinatesBuffer,
								vertexBufferObjs, 2);

			if (verIndicesBuffer != null)
				indicesCoordinatesIndex = GLES20Utils.allocateShortGPUBuffer(
						verIndicesBuffer, indexBufferObj, 0);

			// GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObjs[0]);
			// // 绑定句柄
			// GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
			// rectCoordinatesBuffer.capacity()
			// * GLES20Utils.BYTES_PER_FLOAT,
			// rectCoordinatesBuffer, GLES20.GL_STATIC_DRAW); // 顶点坐标传入GPU缓存

			// GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObjs[1]);
			// // 绑定句柄
			// GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
			// rectColorBuffer.capacity()
			// * GLES20Utils.BYTES_PER_FLOAT, rectColorBuffer,
			// GLES20.GL_STATIC_DRAW); // 顶点颜色坐标传入GPU缓存

			// GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObjs[1]);
			// // 绑定句柄
			// GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
			// rectTextureCoordinatesBuffer.capacity()
			// * GLES20Utils.BYTES_PER_FLOAT,
			// rectTextureCoordinatesBuffer, GLES20.GL_STATIC_DRAW); //
			// 贴图坐标传入GPU缓存

			// GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
			// indexBufferObj[0]); // 绑定index buffer
			// GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
			// rectIndicesBuffer.capacity() * GLES20Utils.BYTES_PER_SHORT,
			// rectIndicesBuffer, GLES20.GL_STATIC_DRAW); // 顶点绘制顺序传入GPU缓存

			// 解绑vertex buffer
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			// 解绑index buffer
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

			// vertexCoordinatesIndex = vertexBufferObjs[0];
			// vertexColorsIndex = vertexBufferObjs[1];
			// vertexTextureCoordinatesIndex = vertexBufferObjs[1];
			// indicesCoordinatesIndex = indexBufferObj[0];

			// 释放客户端内存
			releaseClientBuffer(verCoordinatesBuffer);
			releaseClientBuffer(verColorBuffer);
			releaseClientBuffer(verTextureCoordinatesBuffer);
			releaseClientBuffer(verIndicesBuffer);

		} else {
			throw new Exception("glGenBuffers");
		}
	}

	private void releaseClientBuffer(Buffer buf) {
		// TODO Auto-generated method stub
		if (buf != null) {
			buf.limit(0);
			buf = null;
		}
	}

	/**
	 * 释放GPU缓存
	 */
	public void release() {
		if (vertexBufferObjs[0] > 0) {
			GLES20.glDeleteBuffers(vertexBufferObjs.length, vertexBufferObjs, 0);
			vertexBufferObjs[0] = 0;
		}

		if (indexBufferObj[0] > 0) {
			GLES20.glDeleteBuffers(indexBufferObj.length, indexBufferObj, 0);
			indexBufferObj[0] = 0;
		}
	}

	/**
	 * 设置顶点坐标
	 * 
	 * @param verticesCoordinates
	 * @throws NullPointerException
	 */
	public void setVerticesCoordinates(float[] verticesCoordinates)
			throws NullPointerException {
		if (verticesCoordinates != null) {
			verCoordinatesBuffer = GLES20Utils
					.allocateFloatBuffer(verticesCoordinates);
		} else {
			throw new NullPointerException("verticesCoordinates is null");
		}
	}

	/**
	 * 设置顶点颜色
	 * 
	 * @param verticesColors
	 */
	public void setVerticesColors(float[] verticesColors) {
		if (verticesColors != null) {
			verColorBuffer = GLES20Utils.allocateFloatBuffer(verticesColors);
		} else {
			throw new NullPointerException("verticesColors is null");
		}
	}

	/**
	 * 
	 * 设置顶点材质坐标
	 * 
	 * @param rectTextureCoordinate
	 */
	public void setVerticesTextureCoordinates(float[] verticesTextureCoordinate) {
		if (verticesTextureCoordinate != null) {
			verTextureCoordinatesBuffer = GLES20Utils
					.allocateFloatBuffer(verticesTextureCoordinate);
		} else {
			throw new NullPointerException("verticesTextureCoordinate is null");
		}
	}

	/**
	 * 设置顶点绘制顺序
	 * 
	 * @param indicesCoordinates
	 */
	public void setVerticesIndices(short[] indicesCoordinates) {
		if (indicesCoordinates != null) {
			verIndicesBuffer = GLES20Utils
					.allocateShortBuffer(indicesCoordinates);
			numOfIndices = indicesCoordinates.length;
		} else {
			throw new NullPointerException("indicesCoordinates is null");
		}
	}
}
