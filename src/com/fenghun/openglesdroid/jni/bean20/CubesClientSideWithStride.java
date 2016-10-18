package com.fenghun.openglesdroid.jni.bean20;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class CubesClientSideWithStride extends Cubes{
	private FloatBuffer mCubeBuffer;		

	public CubesClientSideWithStride(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {	
		mCubeBuffer = getInterleavedBuffer(cubePositions, cubeNormals, cubeTextureCoordinates, generatedCubeFactor);						
	}

	@Override
	public void render(int mPositionHandle,int mNormalHandle,int mTextureCoordinateHandle,int mActualCubeFactor) {				
		final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;
		
		// Pass in the position information
		// 传入位置信息
		mCubeBuffer.position(0);
		GLES20.glEnableVertexAttribArray(mPositionHandle);			
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, mCubeBuffer);

		// Pass in the normal information
		// 传入法线信息
		mCubeBuffer.position(POSITION_DATA_SIZE);
		GLES20.glEnableVertexAttribArray(mNormalHandle);			
		GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, mCubeBuffer);
		
		// Pass in the texture information
		// 传入材质信息
		mCubeBuffer.position(POSITION_DATA_SIZE + NORMAL_DATA_SIZE);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);		
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
				stride, mCubeBuffer);			

		// Draw the cubes.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36);
	}

	@Override
	public void release() {
		mCubeBuffer.limit(0);
		mCubeBuffer = null;
	}
}
