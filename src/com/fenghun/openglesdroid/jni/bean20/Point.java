package com.fenghun.openglesdroid.jni.bean20;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Point {

	// Define a simple shader program for our point.
    final String pointVertexShader =
    	"uniform mat4 u_MVPMatrix;      \n"		
      +	"attribute vec4 a_Position;     \n"		
      + "void main()                    \n"
      + "{                              \n"
      + "   gl_Position = u_MVPMatrix   \n"
      + "               * a_Position;   \n"
      + "   gl_PointSize = 5.0;         \n"
      + "}                              \n";
    
    final String pointFragmentShader = 
    	"precision mediump float;       \n"					          
      + "void main()                    \n"
      + "{                              \n"
      + "   gl_FragColor = vec4(1.0,    \n" 
      + "   1.0, 1.0, 1.0);             \n"
      + "}                              \n";
	

    /**
	 * Draws a point representing the position of the light.
	 */
	public void drawLight(float[] mMVPMatrix,float[] mLightModelMatrix,float[] mProjectionMatrix, int mPointProgramHandle,
			float[] mLightPosInModelSpace, float[] mViewMatrix) {
	
		final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");
        
		// Pass in the position.
		GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

		// Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle);  
		
		// Pass in the transformation matrix.
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		
		// Draw the point.
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	}
    
	public String getPointVertexShader() {
		return pointVertexShader;
	}

	public String getPointFragmentShader() {
		return pointFragmentShader;
	}
}
