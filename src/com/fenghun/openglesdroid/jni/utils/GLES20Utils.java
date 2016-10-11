package com.fenghun.openglesdroid.jni.utils;

import android.opengl.GLES20;
import android.util.Log;

public class GLES20Utils {
	
	private static String TAG = "GLES20Utils";
	
	/**
	 * 
	 * 装载着色器
	 * 
	 * 
	 * @param type 着色器类型：1. GLES20.GL_VERTEX_SHADER 顶点着色器 2. GLES20.GL_FRAGMENT_SHADER 片段着色器
	 * @param shaderCode 着色器源码
	 * @return
	 */
	public static int loadShader(int type, String shaderCode){

		// 创建着色器对象
	    int shader = GLES20.glCreateShader(type);

	    // 将源码添加到shader并编译之
	    GLES20.glShaderSource(shader, shaderCode);	// 装在着色器源码，
	    GLES20.glCompileShader(shader);	// 装载着色器对象,编译源码

	    // Check the compile status,检查编译状态
	    int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e(TAG, "Could not compile shader " + type + ":");
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
	    return shader;
	}
	

    public static void checkGlError(String tag,String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(tag, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
