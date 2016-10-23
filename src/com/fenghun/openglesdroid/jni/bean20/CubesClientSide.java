package com.fenghun.openglesdroid.jni.bean20;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class CubesClientSide extends Cubes{

	
	private FloatBuffer mCubePositions;
	private FloatBuffer mCubeNormals;
	private FloatBuffer mCubeTextureCoordinates;

	public CubesClientSide(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {	
		FloatBuffer[] buffers = getBuffers(cubePositions, cubeNormals, cubeTextureCoordinates, generatedCubeFactor);
		
		mCubePositions = buffers[0];
		mCubeNormals = buffers[1];
		mCubeTextureCoordinates = buffers[2];
	}

	@Override
	public void render(int mPositionHandle,int mNormalHandle,int mTextureCoordinateHandle,int mActualCubeFactor) {				        
		// Pass in the position information
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, mCubePositions);

		// Pass in the normal information
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, mCubeNormals);
		
		// Pass in the texture information
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
				0, mCubeTextureCoordinates);

		// Draw the cubes.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36);
	}

	@Override
	public void release() {
		mCubePositions.limit(0);
		mCubePositions = null;
		mCubeNormals.limit(0);
		mCubeNormals = null;
		mCubeTextureCoordinates.limit(0);
		mCubeTextureCoordinates = null;
	}
}
