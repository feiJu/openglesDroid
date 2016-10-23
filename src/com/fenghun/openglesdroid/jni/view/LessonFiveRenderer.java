package com.fenghun.openglesdroid.jni.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.R;
import com.fenghun.openglesdroid.jni.utils.GLES20Utils;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

/**
 * This class implements our custom renderer. Note that the GL10 parameter
 * passed in is unused for OpenGL ES 2.0 renderers -- the static class GLES20 is
 * used instead.
 */
public class LessonFiveRenderer implements GLSurfaceView.Renderer {
	/** Used for debug logs. */
	private static final String TAG = "LessonFiveRenderer";

	private final Context mActivityContext;

	/**
	 * Store the model matrix. This matrix is used to move models from object
	 * space (where each model can be thought of being located at the center of
	 * the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix
	 * transforms world space to eye space; it positions things relative to our
	 * eye.
	 */
	private float[] mViewMatrix = new float[16];

	/**
	 * Store the projection matrix. This is used to project the scene onto a 2D
	 * viewport.
	 */
	private float[] mProjectionMatrix = new float[16];

	/**
	 * Allocate storage for the final combined matrix. This will be passed into
	 * the shader program.
	 */
	private float[] mMVPMatrix = new float[16];

	/** Store our model data in a float buffer. */
	private final FloatBuffer mCubePositions;
	private final FloatBuffer mCubeColors;

	/** This will be used to pass in the transformation matrix. */
	private int mMVPMatrixHandle;

	/** This will be used to pass in model position information. */
	private int mPositionHandle;

	/** This will be used to pass in model color information. */
	private int mColorHandle;

	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;

	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;

	/** Size of the color data in elements. */
	private final int mColorDataSize = 4;

	/** This is a handle to our cube shading program. */
	private int mProgramHandle;

	/** This will be used to switch between blending mode and regular mode. */
	private boolean mBlending = true;

	/**
	 * Initialize the model data.
	 */
	public LessonFiveRenderer(final Context activityContext) {
		mActivityContext = activityContext;

		// Define points for a cube.
		// X, Y, Z
		final float[] p1p = { -1.0f, 1.0f, 1.0f };
		final float[] p2p = { 1.0f, 1.0f, 1.0f };
		final float[] p3p = { -1.0f, -1.0f, 1.0f };
		final float[] p4p = { 1.0f, -1.0f, 1.0f };
		final float[] p5p = { -1.0f, 1.0f, -1.0f };
		final float[] p6p = { 1.0f, 1.0f, -1.0f };
		final float[] p7p = { -1.0f, -1.0f, -1.0f };
		final float[] p8p = { 1.0f, -1.0f, -1.0f };

		final float[] cubePositionData = generateCubeData(p1p, p2p, p3p, p4p,
				p5p, p6p, p7p, p8p, p1p.length);

		// Points of the cube: color information
		// R, G, B, A
		final float[] p1c = { 1.0f, 0.0f, 0.0f, 1.0f }; // red
		final float[] p2c = { 1.0f, 0.0f, 1.0f, 1.0f }; // magenta
		final float[] p3c = { 0.0f, 0.0f, 0.0f, 1.0f }; // black
		final float[] p4c = { 0.0f, 0.0f, 1.0f, 1.0f }; // blue
		final float[] p5c = { 1.0f, 1.0f, 0.0f, 1.0f }; // yellow
		final float[] p6c = { 1.0f, 1.0f, 1.0f, 1.0f }; // white
		final float[] p7c = { 0.0f, 1.0f, 0.0f, 1.0f }; // green
		final float[] p8c = { 0.0f, 1.0f, 1.0f, 1.0f }; // cyan

		final float[] cubeColorData = generateCubeData(p1c, p2c,
				p3c, p4c, p5c, p6c, p7c, p8c, p1c.length);

		// Initialize the buffers.
		mCubePositions = ByteBuffer
				.allocateDirect(cubePositionData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubePositions.put(cubePositionData).position(0);

		mCubeColors = ByteBuffer
				.allocateDirect(cubeColorData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeColors.put(cubeColorData).position(0);
	}

	protected String getVertexShader() {
		return GLES20Utils.readTextFileFromRawResource(mActivityContext,
				R.raw.blending_vertex);
	}

	protected String getFragmentShader() {
		return GLES20Utils.readTextFileFromRawResource(mActivityContext,
				R.raw.blending_fragment);
	}

	public void switchMode() {
		mBlending = !mBlending;

		if (mBlending) {
			// No culling of back faces
			GLES20.glDisable(GLES20.GL_CULL_FACE);

			// No depth testing
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);

			// Enable blending
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
		} else {
			// Cull back faces
			GLES20.glEnable(GLES20.GL_CULL_FACE);

			// Enable depth testing
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);

			// Disable blending
			GLES20.glDisable(GLES20.GL_BLEND);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		// Set the background clear color to black.
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// No culling of back faces
		GLES20.glDisable(GLES20.GL_CULL_FACE);

		// No depth testing
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);

		// Enable blending
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
		// GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);

		// Position the eye in front of the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = -0.5f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -5.0f;

		// Set our up vector. This is where our head would be pointing were we
		// holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera
		// position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination
		// of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices
		// separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY,
				lookZ, upX, upY, upZ);

		final String vertexShader = getVertexShader();
		final String fragmentShader = getFragmentShader();

		final int vertexShaderHandle = GLES20Utils.loadShader(
				GLES20.GL_VERTEX_SHADER, vertexShader);
		final int fragmentShaderHandle = GLES20Utils.loadShader(
				GLES20.GL_FRAGMENT_SHADER, fragmentShader);

		mProgramHandle = GLES20Utils.createAndLinkProgram(vertexShaderHandle,
				fragmentShaderHandle, new String[] { "a_Position", "a_Color" });
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the
		// same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;

		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near,
				far);
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		if (mBlending) {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		} else {
			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
					| GLES20.GL_DEPTH_BUFFER_BIT);
		}

		// Do a complete rotation every 10 seconds.
		long time = SystemClock.uptimeMillis() % 10000L;
		float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

		// Set our program
		GLES20.glUseProgram(mProgramHandle);

		// Set program handles for cube drawing.
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle,
				"u_MVPMatrix");
		mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle,
				"a_Position");
		mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color");

		// Draw some cubes.
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, 4.0f, 0.0f, -7.0f);
		Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 0.0f, 0.0f);
		drawCube();

		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, -4.0f, 0.0f, -7.0f);
		Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
		drawCube();

		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, 0.0f, 4.0f, -7.0f);
		Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
		drawCube();

		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, 0.0f, -4.0f, -7.0f);
		drawCube();

		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -5.0f);
		Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 1.0f, 0.0f);
		drawCube();
	}

	/**
	 * Draws a cube.
	 */
	private void drawCube() {
		// Pass in the position information
		mCubePositions.position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
				GLES20.GL_FLOAT, false, 0, mCubePositions);

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		mCubeColors.position(0);
		GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize,
				GLES20.GL_FLOAT, false, 0, mCubeColors);

		GLES20.glEnableVertexAttribArray(mColorHandle);

		// This multiplies the view matrix by the model matrix, and stores the
		// result in the MVP matrix
		// (which currently contains model * view).
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

		// This multiplies the modelview matrix by the projection matrix, and
		// stores the result in the MVP matrix
		// (which now contains model * view * projection).
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

		// Pass in the combined matrix.
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		// Draw the cube.
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
	}

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

}