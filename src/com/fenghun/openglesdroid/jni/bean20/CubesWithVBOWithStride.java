package com.fenghun.openglesdroid.jni.bean20;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class CubesWithVBOWithStride extends Cubes{
	final int mCubeBufferIdx;

	public CubesWithVBOWithStride(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
		
		FloatBuffer cubeBuffer = getInterleavedBuffer(cubePositions, cubeNormals, cubeTextureCoordinates, generatedCubeFactor);			
		
		// Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.
		// 复制内存数据到OPENGL内存，之后可以释放掉客户端的内存数据
		final int buffers[] = new int[1];	// 用于存储opengl 分配的内存句柄
		GLES20.glGenBuffers(1, buffers, 0);		// 初始化内存，句柄为0				

		// Bind to the buffer. Future commands will affect this buffer specifically
		// 绑定内存
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		
		// Transfer data from client memory to the buffer.
		// We can release the client memory after this call.
		// 将客户端内存数据传入OPENGL es,
		/**
		 * 参数说明：
		 * GL_ARRAY_BUFFER: This buffer contains an array of vertex data.
		 * cubePositionsBuffer.capacity() * BYTES_PER_FLOAT: The number of bytes this buffer should contain.总数byte
		 * cubePositionsBuffer: The source that will be copied to this vertex buffer object.内存数据
		 * GL_STATIC_DRAW: The buffer will not be updated dynamically. 数据不会被动态更新

		 */
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubeBuffer.capacity() * BYTES_PER_FLOAT, cubeBuffer, GLES20.GL_STATIC_DRAW);			
		
		// IMPORTANT: Unbind from the buffer when we're done with it.
		// 数据传输完成，解除绑定
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		// 存储句柄
		mCubeBufferIdx = buffers[0];			
		
		// 释放客户端内存
		cubeBuffer.limit(0);
		cubeBuffer = null;
	}

	@Override
	public void render(int mPositionHandle,int mNormalHandle,int mTextureCoordinateHandle,int mActualCubeFactor) {	    
		final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;
		
		// Our call to glVertexAttribPointer looks a little bit different, 
		// as the last parameter is now an offset rather than a pointer to our client-side memory:
		// 传入信息的方式与使用客户端内存传入信息的方式略有不同
		
		// Pass in the position information
		// 传入顶点信息
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);		// 绑定
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

		// Pass in the normal information，法线信息
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);		// 绑定
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, POSITION_DATA_SIZE * BYTES_PER_FLOAT);
		
		// Pass in the texture information
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);	// 绑定
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
				stride, (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * BYTES_PER_FLOAT);

		// Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
		// 数据传输完成，解除绑定
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		// Draw the cubes.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mActualCubeFactor * mActualCubeFactor * mActualCubeFactor * 36);
	}

	@Override
	public void release() {
		// Delete buffers from OpenGL's memory
		final int[] buffersToDelete = new int[] { mCubeBufferIdx };
		GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
	}

}
