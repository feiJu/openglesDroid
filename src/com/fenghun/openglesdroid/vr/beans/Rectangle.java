package com.fenghun.openglesdroid.vr.beans;

import com.fenghun.openglesdroid.R;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler.ErrorType;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

/**
 * 矩形
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-10-20
 * @function
 */
public class Rectangle extends Mesh {

	private static String TAG = "Rectangle";

	// 顶点信息，opengl 的坐标系与屏幕的坐标系不同，
	// opengl 的原点在画布的中心，向左为x正，向上为y正，向外为z正。（右手坐标系）
	// X, Y, Z,
	private float verticesCoordinates[] = { 
			-1.7777778f, 1.0f, 0.0f, // 0, Top Left
			-1.7777778f, -1.0f, 0.0f, // 1, Bottom Left
			1.7777778f, -1.0f, 0.0f, // 2, Bottom Right
			1.7777778f, 1.0f, 0.0f // 3, Top Right
	};

	// R, G, B, A
	private float verticesColors[] = { 1.0f, 0.0f, 0.0f, 1.0f, // 0
			0.0f, 0.0f, 1.0f, 1.0f, // 1
			0.0f, 1.0f, 0.0f, 1.0f, // 2
			1.0f, 1.0f, 0.0f, 1.0f // 3
	};

	/**
	 * 定义面的顶点的顺序很重要 在拼接曲面的时候，用来定义面的顶点的顺序非常重要，
	 * 因为顶点的顺序定义了面的朝向（前向或是后向），为了获取绘制的高性能，一般情况不会绘制面的前面和后面，
	 * 只绘制面的“前面”。虽然“前面”“后面”的定义可以应人而易，但一般为所有的“前面”定义统一的顶点顺序(顺时针或是逆时针方向）。
	 */
	// The order we like to connect them.
	private short[] indicesCoordinates = { 0, 1, 2, 0, 2, 3 }; // 顶点坐标绘制的顺序，

	final float[] rectTextureCoordinateData = { 
			0.0f, 0.0f, // 0
			0.0f, 1.0f, // 1
			
			1.0f, 1.0f, // 2
			1.0f, 0.0f // 3
	};
	/**
	 * 模型矩阵 Store the model matrix. This matrix is used to move models from
	 * object space (where each model can be thought of being located at the
	 * center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];

	private boolean isLandscape = true; // 是否为横屏，默认为是，可以动态获取手机的方向

	private float translateZ = 0.0f; // 向Z轴平移的数量

	public Rectangle(Context context, float screenWidth, float screenHeight,
			ErrorHandler errorHandler) {
		// TODO Auto-generated constructor stub
		try {
			// 初始化坐标信息
			float ratio = screenWidth / screenHeight > 1 ? screenWidth
					/ screenHeight : screenHeight / screenWidth;
			verticesCoordinates = new float[] { -ratio, 1.0f, 0.0f, // 0, Top
																	// Left
					-ratio, -1.0f, 0.0f, // 1, Bottom Left
					ratio, -1.0f, 0.0f, // 2, Bottom Right
					ratio, 1.0f, 0.0f // 3, Top Right
			};
			// 设置顶点数据信息
			setVerticesCoordinates(verticesCoordinates);
			//setVerticesColors(verticesColors);
			setVerticesTextureCoordinates(rectTextureCoordinateData);
			setVerticesIndices(indicesCoordinates);
			// 将顶点数据信息缓存到GPU
			transformData2GPU();

		} catch (Throwable t) {
			Log.w(TAG, t);
			errorHandler.handleError(ErrorType.BUFFER_CREATION_ERROR,
					t.getLocalizedMessage());
		}
		
		initShader(context, R.raw.no_color_rect_view_vertex_shader,R.raw.no_color_rect_view_fragment_shader,
				new String[] { ATTR_POSITION, /*ATTR_COLOR,*/ ATTR_TEXTURE_COORDINATE });
		setLookAtM(0.0f,0.0f,1.0f,0.0f,0.0f,-5.0f,0.0f,1.0f,0.0f);	// 设置观察矩阵即camera
	}

	@Override
	public void setProjectionMatrix(int width, int height) {

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

	public void draw() {


		// Do a complete rotation every 10 seconds.
		long time = SystemClock.uptimeMillis() % 10000L;
		float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
		// System.out.println("--------------- angleInDegrees==="+angleInDegrees);
		
		// Draw the triangle facing straight on.
		Matrix.setIdentityM(mModelMatrix, 0);
		if (isLandscape) {
			translateZ = 0.0f;
		} else {
			translateZ = -4.0f;
		}
		Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, translateZ);
		render(mModelMatrix);
	}

}
