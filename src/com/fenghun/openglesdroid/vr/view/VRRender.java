package com.fenghun.openglesdroid.vr.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.MainActivity;
import com.fenghun.openglesdroid.R;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler;
import com.fenghun.openglesdroid.jni.utils.GLES20Utils;
import com.fenghun.openglesdroid.vr.beans.Rectangle;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

/**
 * VR 视图渲染
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-10-20
 * @function
 */
public class VRRender implements Renderer {

	/**
	 * Store the projection matrix. This is used to project the scene onto a 2D
	 * viewport. 透视投影矩阵，用于投影场景到屏幕
	 */
	private float[] mProjectionMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix
	 * transforms world space to eye space; it positions things relative to our
	 * eye.
	 */
	private float[] mViewMatrix = new float[16];

	/**
	 * 模型矩阵 Store the model matrix. This matrix is used to move models from
	 * object space (where each model can be thought of being located at the
	 * center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];

	/**
	 * Allocate storage for the final combined matrix. This will be passed into
	 * the shader program.
	 */
	private float[] mMVPMatrix = new float[16];

	private Rectangle rectView;

	private ErrorHandler errorHandler;

	private Context context;

	private int mProgramHandle;

	private int mMVPMatrixHandle = -1;

	private int mPositionHandle = -1;

//	private int mColorHandle = -1;
	
	/** This will be used to pass in model texture coordinate information. */
	private int mTextureCoordinateHandle=-1;
	
	/** This will be used to pass in the texture. */
	private int mTextureUniformHandle = -1;
	
	private int mOneFrameDataHandle = -1;

	private boolean isLandscape = true; // 是否为横屏，默认为是，可以动态获取手机的方向

	private float translateZ = 0.0f; // 向Z轴平移的数量

	private MainActivity mainActivity;
	//
	public VRRender(MainActivity mainActivity, ErrorHandler errorHandler) {
		// TODO Auto-generated constructor stub
		context = mainActivity;
		this.mainActivity = mainActivity;
		this.errorHandler = errorHandler;
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		// TODO Auto-generated method stub
		// 设置背景的颜色
		GLES20.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);

		// GLES20.glFrontFace(GLES20.GL_CCW);
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		// Position the eye behind the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = 1.0f;
		
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

		// 初始化rect View
		rectViewInit();
		// squareInit();

	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// TODO Auto-generated method stub
		GLES20.glViewport(0, 0, width, height);
		// Create a new perspective projection matrix. The height will stay the
		// same
		// while the width will vary as per aspect ratio.
		// 创建一个透视投影矩阵，高度保持一致，宽度则按比例计算
		final float ratio = (float) width / height;
		if (ratio >= 1.0) {
			isLandscape = true;
		} else {
			isLandscape = false;
		}
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 100.0f;

		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near,
				far);
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		// TODO Auto-generated method stub
		rectView();
	}

	/**
	 * 初始化rect View
	 */
	private void rectViewInit() {
		// TODO Auto-generated method stub
		rectView = new Rectangle(mainActivity.getScreenWidth(),mainActivity.getScreenHeight(),errorHandler);
		
		final String vertexShader = GLES20Utils.readTextFileFromRawResource(
				context, R.raw.rect_view_vertex_shader);
		final String fragmentShader = GLES20Utils.readTextFileFromRawResource(
				context, R.raw.rect_view_distortion_fragment_shader);

		final int vertexShaderHandle = GLES20Utils.loadShader(
				GLES20.GL_VERTEX_SHADER, vertexShader);
		final int fragmentShaderHandle = GLES20Utils.loadShader(
				GLES20.GL_FRAGMENT_SHADER, fragmentShader);

		mProgramHandle = GLES20Utils.createAndLinkProgram(vertexShaderHandle,
				fragmentShaderHandle, new String[] { "a_Position", /*"a_Color",*/"a_TexCoordinate" });

		// 加载贴图数据
		mOneFrameDataHandle = GLES20Utils.loadTexture(context, R.drawable.sea360);		
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);	
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOneFrameDataHandle);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOneFrameDataHandle);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle,"u_MVPMatrix");
		if (mMVPMatrixHandle == -1) {
			throw new RuntimeException(
					"Could not get uniform location for u_MVPMatrix");
		}

		mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
		if (mTextureUniformHandle == -1) {
			throw new RuntimeException(
					"Could not get uniform location for u_Texture");
		}
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle,
				"a_Position");
		if (mPositionHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for a_Position");
		}
//		mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
//		if (mColorHandle == -1) {
//			throw new RuntimeException(
//					"Could not get attrib location for a_Color");
//		}
		
		mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
		if (mTextureCoordinateHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for a_TexCoordinate");
		}

	}

	private void rectView() {

		// Do a complete rotation every 10 seconds.
		long time = SystemClock.uptimeMillis() % 10000L;
		float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
		// System.out.println("--------------- angleInDegrees==="+angleInDegrees);

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		// Set our per-vertex lighting program.
		GLES20.glUseProgram(mProgramHandle);

		// Draw the triangle facing straight on.
		Matrix.setIdentityM(mModelMatrix, 0);

		if (isLandscape) {
			translateZ = 0.0f;
		} else {
			translateZ = -4.0f;
		}
		Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, translateZ);
		// Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
		// This multiplies the view matrix by the model matrix, and stores the
		// result in the MVP matrix
		// (which currently contains model * view).
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		// This multiplies the modelview matrix by the projection matrix, and
		// stores the result in the MVP matrix
		// (which now contains model * view * projection).
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		// Pass in the texture information
		// Set the active texture unit to texture unit 0.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOneFrameDataHandle);
		// Tell the texture uniform sampler to use this texture in the
		// shader by binding to texture unit 0.
		GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		rectView.render(mPositionHandle, /*mColorHandle,*/mTextureCoordinateHandle);
	}
}
