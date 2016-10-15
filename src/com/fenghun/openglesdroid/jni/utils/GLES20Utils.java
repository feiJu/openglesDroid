package com.fenghun.openglesdroid.jni.utils;

import android.opengl.GLES20;
import android.util.Log;

public class GLES20Utils {

	private static String TAG = "GLES20Utils";

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
}
