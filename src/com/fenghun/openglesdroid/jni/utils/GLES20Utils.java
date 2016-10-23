package com.fenghun.openglesdroid.jni.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class GLES20Utils {

	private static String TAG = "GLES20Utils";

	/** How many bytes per float. */
	public static final int BYTES_PER_FLOAT = 4;
	/** How many bytes per int. */
	public static final int BYTES_PER_SHORT = 2;

	/**
	 * read in the shader from a text file in the raw resources folder
	 * 
	 * @param context
	 * @param resourceId
	 * @return
	 */
	public static String readTextFileFromRawResource(final Context context,
			final int resourceId) {
		final InputStream inputStream = context.getResources().openRawResource(
				resourceId);
		final InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream);
		final BufferedReader bufferedReader = new BufferedReader(
				inputStreamReader);

		String nextLine;
		final StringBuilder body = new StringBuilder();

		try {
			while ((nextLine = bufferedReader.readLine()) != null) {
				body.append(nextLine);
				body.append('\n');
			}
		} catch (IOException e) {
			return null;
		}

		return body.toString();
	}

	/**
	 * 加载贴图材质
	 * 
	 * @param context
	 * @param resourceId
	 * @return
	 */
	public static int loadTexture(final Context context, final int resourceId) {

		// create a new handle for us. This handle serves as a unique
		// identifier,
		// and we use it whenever we want to refer to the same texture in
		// OpenGL.
		final int[] textureHandle = new int[1];
		GLES20.glGenTextures(1, textureHandle, 0);

		if (textureHandle[0] != 0) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false; // No pre-scaling

			// Read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), resourceId, options);

			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

			// Set filtering
			// This tells OpenGL what type of filtering to apply when drawing
			// the texture smaller than the original size in pixels.
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			// This tells OpenGL what type of filtering to apply when magnifying
			// the texture beyond its original size in pixels.
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();
		}

		if (textureHandle[0] == 0) {
			throw new RuntimeException("Error loading texture.");
		}

		return textureHandle[0];
	}

	/**
	 * Helper function to compile and link a program.
	 * 
	 * @param vertexShaderHandle
	 *            An OpenGL handle to an already-compiled vertex shader.
	 * @param fragmentShaderHandle
	 *            An OpenGL handle to an already-compiled fragment shader.
	 * @param attributes
	 *            Attributes that need to be bound to the program.
	 * @return An OpenGL handle to the program.
	 */
	public static int createAndLinkProgram(final int vertexShaderHandle,
			final int fragmentShaderHandle, final String[] attributes) {
		int programHandle = GLES20.glCreateProgram();

		if (programHandle != 0) {
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);

			// Bind attributes
			if (attributes != null) {
				final int size = attributes.length;
				for (int i = 0; i < size; i++) {
					GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				}
			}

			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS,
					linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) {
				Log.e(TAG,
						"Error compiling program: "
								+ GLES20.glGetProgramInfoLog(programHandle));
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}

		if (programHandle == 0) {
			throw new RuntimeException("Error creating program.");
		}

		return programHandle;
	}

	/**
	 * 
	 * 装载着色器
	 * 
	 * 
	 * @param type
	 *            着色器类型：1. GLES20.GL_VERTEX_SHADER 顶点着色器 2.
	 *            GLES20.GL_FRAGMENT_SHADER 片段着色器
	 * @param shaderCode
	 *            着色器源码
	 * @return
	 */
	public static int loadShader(int type, String shaderCode) {

		/**
		 * 创建着色器对象
		 * 
		 * Type 创建着色器的类型 GL_VERTEX_SHADER 或者 GL_FRAGMENT_SHADER
		 * 返回值是个新着色器的句柄，不使用了，用 glDeleteShader 删除它。
		 */
		int shader = GLES20.glCreateShader(type);

		// 将源码添加到shader并编译之
		/**
		 * 提供着色器源码。
		 * 
		 * 
		 */
		GLES20.glShaderSource(shader, shaderCode); // 装载着色器源码，

		/**
		 * 不是所有的 OpenGL ES 2.0 工具提供编译着色器的功能，（有的要求离线编译）这在后面着色器二进制码中介绍
		 */
		GLES20.glCompileShader(shader); // 装载着色器对象,编译源码

		// Check the compile status,检查编译状态
		int[] compiled = new int[1];
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
			Log.e(TAG, "Could not compile shader " + type + ":");
			Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
			/**
			 * 主要如果着色器正连接着一个项目对象，glDeleteShader 不会立刻删除着色器，
			 * 而是设置删除标记，一旦着色器不再连接项目对象，才删除着色器使用的内存。
			 */
			GLES20.glDeleteShader(shader);
			shader = 0;
		}
		return shader;
	}

	public static void checkGlError(String tag, String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(tag, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}

	/**
	 * 
	 * 初始化立方体数据
	 * 
	 * @param point1
	 * @param point2
	 * @param point3
	 * @param point4
	 * @param point5
	 * @param point6
	 * @param point7
	 * @param point8
	 * @param elementsPerPoint
	 * @return
	 */
	public static float[] generateCubeData(float[] point1, float[] point2,
			float[] point3, float[] point4, float[] point5, float[] point6,
			float[] point7, float[] point8, int elementsPerPoint) {
		// Given a cube with the points defined as follows:
		// front left top, front right top, front left bottom, front right
		// bottom,
		// back left top, back right top, back left bottom, back right bottom,
		// return an array of 6 sides, 2 triangles per side, 3 vertices per
		// triangle, and 4 floats per vertex.
		final int FRONT = 0;
		final int RIGHT = 1;
		final int BACK = 2;
		final int LEFT = 3;
		final int TOP = 4;
		final int BOTTOM = 5;

		final int size = elementsPerPoint * 6 * 6;
		final float[] cubeData = new float[size];

		for (int face = 0; face < 6; face++) {
			// Relative to the side, p1 = top left, p2 = top right, p3 = bottom
			// left, p4 = bottom right
			final float[] p1, p2, p3, p4;

			// Select the points for this face
			if (face == FRONT) {
				p1 = point1;
				p2 = point2;
				p3 = point3;
				p4 = point4;
			} else if (face == RIGHT) {
				p1 = point2;
				p2 = point6;
				p3 = point4;
				p4 = point8;
			} else if (face == BACK) {
				p1 = point6;
				p2 = point5;
				p3 = point8;
				p4 = point7;
			} else if (face == LEFT) {
				p1 = point5;
				p2 = point1;
				p3 = point7;
				p4 = point3;
			} else if (face == TOP) {
				p1 = point5;
				p2 = point6;
				p3 = point1;
				p4 = point2;
			} else // if (side == BOTTOM)
			{
				p1 = point8;
				p2 = point7;
				p3 = point4;
				p4 = point3;
			}

			// In OpenGL counter-clockwise winding is default. This means that
			// when we look at a triangle,
			// if the points are counter-clockwise we are looking at the
			// "front". If not we are looking at
			// the back. OpenGL has an optimization where all back-facing
			// triangles are culled, since they
			// usually represent the backside of an object and aren't visible
			// anyways.

			// Build the triangles
			// 1---3,6
			// | / |
			// 2,4--5
			int offset = face * elementsPerPoint * 6;

			for (int i = 0; i < elementsPerPoint; i++) {
				cubeData[offset++] = p1[i];
			}
			for (int i = 0; i < elementsPerPoint; i++) {
				cubeData[offset++] = p3[i];
			}
			for (int i = 0; i < elementsPerPoint; i++) {
				cubeData[offset++] = p2[i];
			}
			for (int i = 0; i < elementsPerPoint; i++) {
				cubeData[offset++] = p3[i];
			}
			for (int i = 0; i < elementsPerPoint; i++) {
				cubeData[offset++] = p4[i];
			}
			for (int i = 0; i < elementsPerPoint; i++) {
				cubeData[offset++] = p2[i];
			}
		}

		return cubeData;
	}

	/**
	 * 
	 * 分配浮点数内存
	 * 
	 * @param floatData
	 * @return
	 */
	public static FloatBuffer allocateFloatBuffer(float[] floatData) {
		// TODO Auto-generated method stub
		final FloatBuffer floatBuffer = ByteBuffer
				.allocateDirect(floatData.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		floatBuffer.put(floatData);
		floatBuffer.position(0);
		return floatBuffer;
	}

	/**
	 * 
	 * 分配短整型内存
	 * 
	 * @param indicesCoordinates
	 * @return
	 */
	public static ShortBuffer allocateShortBuffer(short[] shortData) {
		// TODO Auto-generated method stub
		final ShortBuffer shortBuffer = ByteBuffer
				.allocateDirect(shortData.length * BYTES_PER_SHORT)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		shortBuffer.put(shortData);
		shortBuffer.position(0);
		return shortBuffer;
	}
}
