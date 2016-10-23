package com.fenghun.openglesdroid.jni.bean20;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class CubesWithVbo extends Cubes{

	
	final int mCubePositionsBufferIdx;
	final int mCubeNormalsBufferIdx;
	final int mCubeTexCoordsBufferIdx;

	public CubesWithVbo(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
		FloatBuffer[] floatBuffers = getBuffers(cubePositions, cubeNormals, cubeTextureCoordinates, generatedCubeFactor);
		
		FloatBuffer cubePositionsBuffer = floatBuffers[0];
		FloatBuffer cubeNormalsBuffer = floatBuffers[1];
		FloatBuffer cubeTextureCoordinatesBuffer = floatBuffers[2];			
		
		// Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.					
		final int buffers[] = new int[3];
		GLES20.glGenBuffers(3, buffers, 0);		// 初始化内存句柄				

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]); // 绑定
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubePositionsBuffer.capacity() * BYTES_PER_FLOAT, cubePositionsBuffer, GLES20.GL_STATIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);// 绑定
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubeNormalsBuffer.capacity() * BYTES_PER_FLOAT, cubeNormalsBuffer, GLES20.GL_STATIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[2]);// 绑定
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubeTextureCoordinatesBuffer.capacity() * BYTES_PER_FLOAT, cubeTextureCoordinatesBuffer,
				GLES20.GL_STATIC_DRAW);

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);	// 解除绑定

		mCubePositionsBufferIdx = buffers[0];
		mCubeNormalsBufferIdx = buffers[1];
		mCubeTexCoordsBufferIdx = buffers[2];
		
		cubePositionsBuffer.limit(0);
		cubePositionsBuffer = null;
		cubeNormalsBuffer.limit(0);
		cubeNormalsBuffer = null;
		cubeTextureCoordinatesBuffer.limit(0);
		cubeTextureCoordinatesBuffer = null;
	}

	@Override
	public void render(int mPositionHandle,int mNormalHandle,int mTextureCoordinateHandle,int mActualCubeFactor) {	      
		// Pass in the position information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubePositionsBufferIdx);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);

		// Pass in the normal information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeNormalsBufferIdx);
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);
		
		// Pass in the texture information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeTexCoordsBufferIdx);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
				0, 0);

		// Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);		// 解除绑定

		// Draw the cubes.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36);
	}

	@Override
	public void release() {
		// Delete buffers from OpenGL's memory
		final int[] buffersToDelete = new int[] { mCubePositionsBufferIdx, mCubeNormalsBufferIdx,
				mCubeTexCoordsBufferIdx };
		GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
	}
	
}
