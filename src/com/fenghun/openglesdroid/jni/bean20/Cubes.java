package com.fenghun.openglesdroid.jni.bean20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class Cubes {

	/** Size of the position data in elements. */
	protected static final int POSITION_DATA_SIZE = 3;

	/** How many bytes per float. */
	protected static final int BYTES_PER_FLOAT = 4;

	/** Size of the normal data in elements. */
	protected static final int NORMAL_DATA_SIZE = 3;

	/** Size of the texture coordinate data in elements. */
	static final int TEXTURE_COORDINATE_DATA_SIZE = 2;

	public abstract void render(int mPositionHandle, int mNormalHandle,
			int mTextureCoordinateHandle, int mActualCubeFactor);

	public abstract void release();

	FloatBuffer[] getBuffers(float[] cubePositions, float[] cubeNormals,
			float[] cubeTextureCoordinates, int generatedCubeFactor) {
		// First, copy cube information into client-side floating point buffers.
		final FloatBuffer cubePositionsBuffer;
		final FloatBuffer cubeNormalsBuffer;
		final FloatBuffer cubeTextureCoordinatesBuffer;

		cubePositionsBuffer = ByteBuffer
				.allocateDirect(cubePositions.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		cubePositionsBuffer.put(cubePositions).position(0);

		cubeNormalsBuffer = ByteBuffer
				.allocateDirect(
						cubeNormals.length * BYTES_PER_FLOAT
								* generatedCubeFactor * generatedCubeFactor
								* generatedCubeFactor)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		for (int i = 0; i < (generatedCubeFactor * generatedCubeFactor * generatedCubeFactor); i++) {
			cubeNormalsBuffer.put(cubeNormals);
		}

		cubeNormalsBuffer.position(0);

		cubeTextureCoordinatesBuffer = ByteBuffer
				.allocateDirect(
						cubeTextureCoordinates.length * BYTES_PER_FLOAT
								* generatedCubeFactor * generatedCubeFactor
								* generatedCubeFactor)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		for (int i = 0; i < (generatedCubeFactor * generatedCubeFactor * generatedCubeFactor); i++) {
			cubeTextureCoordinatesBuffer.put(cubeTextureCoordinates);
		}

		cubeTextureCoordinatesBuffer.position(0);

		return new FloatBuffer[] { cubePositionsBuffer, cubeNormalsBuffer,
				cubeTextureCoordinatesBuffer };
	}

	FloatBuffer getInterleavedBuffer(float[] cubePositions,
			float[] cubeNormals, float[] cubeTextureCoordinates,
			int generatedCubeFactor) {
		final int cubeDataLength = cubePositions.length
				+ (cubeNormals.length * generatedCubeFactor
						* generatedCubeFactor * generatedCubeFactor)
				+ (cubeTextureCoordinates.length * generatedCubeFactor
						* generatedCubeFactor * generatedCubeFactor);
		int cubePositionOffset = 0;
		int cubeNormalOffset = 0;
		int cubeTextureOffset = 0;

		final FloatBuffer cubeBuffer = ByteBuffer
				.allocateDirect(cubeDataLength * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		for (int i = 0; i < generatedCubeFactor * generatedCubeFactor
				* generatedCubeFactor; i++) {
			for (int v = 0; v < 36; v++) {
				cubeBuffer.put(cubePositions, cubePositionOffset,
						POSITION_DATA_SIZE);
				cubePositionOffset += POSITION_DATA_SIZE;
				cubeBuffer.put(cubeNormals, cubeNormalOffset, NORMAL_DATA_SIZE);
				cubeNormalOffset += NORMAL_DATA_SIZE;
				cubeBuffer.put(cubeTextureCoordinates, cubeTextureOffset,
						TEXTURE_COORDINATE_DATA_SIZE);
				cubeTextureOffset += TEXTURE_COORDINATE_DATA_SIZE;
			}

			// The normal and texture data is repeated for each cube.
			cubeNormalOffset = 0;
			cubeTextureOffset = 0;
		}

		cubeBuffer.position(0);

		return cubeBuffer;
	}
}
