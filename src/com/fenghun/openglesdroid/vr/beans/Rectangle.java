package com.fenghun.openglesdroid.vr.beans;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.fenghun.openglesdroid.jni.bean20.ErrorHandler;
import com.fenghun.openglesdroid.jni.bean20.HeightMap;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler.ErrorType;
import com.fenghun.openglesdroid.jni.utils.GLES20Utils;

import android.opengl.GLES20;
import android.util.Log;

/**
 * 矩形
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-10-20
 * @function
 */
public class Rectangle {

	private static String TAG = "Rectangle";

	/** Size of the texture coordinate data in elements. */
	private final int TEXTURE_COORDINATE_DATA_SIZE = 2;

	/** Additional constants. */
	private  final int POSITION_DATA_SIZE_IN_ELEMENTS = 3;
	// private static final int NORMAL_DATA_SIZE_IN_ELEMENTS = 3;
	//private  final int COLOR_DATA_SIZE_IN_ELEMENTS = 4;

	// 存储顶点相关句柄
	private final int[] vertexBufferObjs = new int[2];
	// 存储顶点顺序相关句柄
	private final int[] indexBufferObj = new int[1];

	private int vertexCoordinatesIndex = 0;
	//private int vertexColorsIndex = 0;
	private int vertexTextureCoordinatesIndex= 0;
	private int indicesCoordinatesIndex = 0;
	
	// 顶点信息，opengl 的坐标系与屏幕的坐标系不同，
	// opengl 的原点在画布的中心，向左为x正，向上为y正，向外为z正。（右手坐标系）
	// X, Y, Z,
	private float verticesCoordinates[] = { -1.7777778f, 1.0f, 0.0f, // 0, Top
																		// Left
			-1.7777778f, -1.0f, 0.0f, // 1, Bottom Left
			1.7777778f, -1.0f, 0.0f, // 2, Bottom Right
			1.7777778f, 1.0f, 0.0f // 3, Top Right
	};

	// R, G, B, A
//	private float verticesColors[] = { 1.0f, 0.0f, 0.0f, 1.0f, // 0
//			0.0f, 0.0f, 1.0f, 1.0f, // 1
//			0.0f, 1.0f, 0.0f, 1.0f, // 2
//			1.0f, 1.0f, 0.0f, 1.0f // 3
//	};

	/**
	 * 定义面的顶点的顺序很重要 在拼接曲面的时候，用来定义面的顶点的顺序非常重要，
	 * 因为顶点的顺序定义了面的朝向（前向或是后向），为了获取绘制的高性能，一般情况不会绘制面的前面和后面，
	 * 只绘制面的“前面”。虽然“前面”“后面”的定义可以应人而易，但一般为所有的“前面”定义统一的顶点顺序(顺时针或是逆时针方向）。
	 */
	// The order we like to connect them.
	private short[] indicesCoordinates = { 0, 1, 2, 2, 3, 0 }; // 顶点坐标绘制的顺序，

	final float[] rectTextureCoordinateData = { 0.0f, 0.0f, // 0
			0.0f, 1.0f, // 1
			1.0f, 1.0f, // 2
			1.0f, 0.0f // 3
	};

	public Rectangle(float screenWidth, float screenHeight,
			ErrorHandler errorHandler) {
		// TODO Auto-generated constructor stub
		try {
			float ratio = screenWidth / screenHeight > 1 ? screenWidth
					/ screenHeight : screenHeight / screenWidth;
			verticesCoordinates = new float[] { 
					-ratio, 1.0f, 0.0f, // 0, Top Left
					-ratio, -1.0f, 0.0f, // 1, Bottom Left
					ratio, -1.0f, 0.0f, // 2, Bottom Right
					ratio, 1.0f, 0.0f // 3, Top Right
			};

			// 客户端缓存
			// 位置坐标缓存
			final FloatBuffer rectCoordinatesBuffer = GLES20Utils
					.allocateFloatBuffer(verticesCoordinates);
//			final FloatBuffer rectColorBuffer = GLES20Utils
//					.allocateFloatBuffer(verticesColors);
			final FloatBuffer rectTextureCoordinatesBuffer = GLES20Utils
					.allocateFloatBuffer(rectTextureCoordinateData);
			final ShortBuffer rectIndicesBuffer = GLES20Utils
					.allocateShortBuffer(indicesCoordinates);

			// GPU缓存
			// Second, copy these buffers into OpenGL's memory. After, we don't
			// need
			// to keep the client-side buffers around.
			// 初始化内存句柄
			GLES20.glGenBuffers(vertexBufferObjs.length, vertexBufferObjs, 0);
			GLES20.glGenBuffers(1, indexBufferObj, 0);

			if (vertexBufferObjs[0] > 0 && vertexBufferObjs[1] > 0 && indexBufferObj[0] > 0) {
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObjs[0]); // 绑定句柄
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						rectCoordinatesBuffer.capacity()
								* GLES20Utils.BYTES_PER_FLOAT,
						rectCoordinatesBuffer, GLES20.GL_STATIC_DRAW); // 顶点坐标传入GPU缓存

//				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObjs[1]); // 绑定句柄
//				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
//						rectColorBuffer.capacity()
//								* GLES20Utils.BYTES_PER_FLOAT, rectColorBuffer,
//						GLES20.GL_STATIC_DRAW); // 顶点颜色坐标传入GPU缓存

				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObjs[1]); // 绑定句柄
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						rectTextureCoordinatesBuffer.capacity()
								* GLES20Utils.BYTES_PER_FLOAT, rectTextureCoordinatesBuffer,
						GLES20.GL_STATIC_DRAW); // 贴图坐标传入GPU缓存
				
				GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,indexBufferObj[0]); // 绑定index buffer
				GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
						rectIndicesBuffer.capacity()
								* GLES20Utils.BYTES_PER_SHORT,
						rectIndicesBuffer, GLES20.GL_STATIC_DRAW); // 顶点绘制顺序传入GPU缓存
				// 解绑vertex buffer
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0); 
				// 解绑index buffer											 
				GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
				
				vertexCoordinatesIndex = vertexBufferObjs[0];
				//vertexColorsIndex = vertexBufferObjs[1];
				vertexTextureCoordinatesIndex = vertexBufferObjs[1];
				indicesCoordinatesIndex = indexBufferObj[0];
				
			} else {
				errorHandler.handleError(ErrorType.BUFFER_CREATION_ERROR,
						"glGenBuffers");
			}
		} catch (Throwable t) {
			Log.w(TAG, t);
			errorHandler.handleError(ErrorType.BUFFER_CREATION_ERROR,
					t.getLocalizedMessage());
		}
	}

	public void render(int mPositionAttributeHandle, /*int mColorAttributeHandle,*/int mTextureCoordinateHandle) {
		if (vertexBufferObjs[0] > 0 && indexBufferObj[0] > 0) {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexCoordinatesIndex); // 绑定句柄
			GLES20.glEnableVertexAttribArray(mPositionAttributeHandle); // 开启顶点位置属性传入
			GLES20.glVertexAttribPointer(mPositionAttributeHandle,
					POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, 0,
					0);
			
//			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexColorsIndex); // 绑定句柄
//			GLES20.glEnableVertexAttribArray(mColorAttributeHandle);
//			GLES20.glVertexAttribPointer(mColorAttributeHandle,
//					COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, 0, 0);
			

			// Pass in the texture information
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexTextureCoordinatesIndex);
			GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
			GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
					0, 0);
			
			// Draw
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
					indicesCoordinatesIndex);// 绑定绘制顺序数据缓存
			GLES20.glDrawElements(GLES20.GL_TRIANGLES,
					indicesCoordinates.length, GLES20.GL_UNSIGNED_SHORT, 0);

			GLES20.glDisableVertexAttribArray(mPositionAttributeHandle); // 关闭顶点位置属性传入
//			GLES20.glDisableVertexAttribArray(mColorAttributeHandle); // 关闭顶点位置属性传入
			GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle); // 关闭顶点位置属性传入
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
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
}
