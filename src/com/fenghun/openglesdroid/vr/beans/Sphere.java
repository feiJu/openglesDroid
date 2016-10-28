package com.fenghun.openglesdroid.vr.beans;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fenghun.openglesdroid.R;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler.ErrorType;

/**
 * 球体
 * 
 * @author fenghun5987@gmail.com
 * @date Oct 23, 2016
 */
public class Sphere extends Mesh {

	private final float UNIT_SIZE = 1.0f; // 单位尺寸

	private float radius = 0.8f; // 球体半径

	private float[] verticesCoordinates; // 顶点坐标

	//private float[] verticesColors;
	
	// The order we like to connect them.
	private short[] indicesCoordinates; // 顶点坐标顺序
	
	/**
	 * 模型矩阵 Store the model matrix. This matrix is used to move models from
	 * object space (where each model can be thought of being located at the
	 * center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];
	
	private int radiusHandle = -1;

	public Sphere(Context context, ErrorHandler errorHandler) {
		// TODO Auto-generated constructor stub
		initVertexDataNewCCM();	// 初始化顶点和顶点顺序
		try {
			setVerticesCoordinates(verticesCoordinates);	// 设置顶点坐标
			//setVerticesColors(verticesColors);
			setVerticesIndices(indicesCoordinates);	// 设置顶点顺序
			transformData2GPU();
		} catch (Exception e) {
			// TODO: handle exception
			errorHandler.handleError(ErrorType.BUFFER_CREATION_ERROR,
					e.getLocalizedMessage());
			e.printStackTrace();
		}
		initShader(context, R.raw.sphere_vertex,R.raw.sphere_frag,
				new String[] { ATTR_POSITION, /*ATTR_COLOR, ATTR_TEXTURE_COORDINATE*/ });
		radiusHandle = GLES20.glGetUniformLocation(mProgramHandle,
				"u_radius");
		GLES20.glUseProgram(mProgramHandle);
		
		//setLookAtM(0.0f,0.0f,1.0f,0.0f,0.0f,-5.0f,0.0f,1.0f,0.0f);	// 设置观察矩阵即camera
		setLookAtM(0, 0, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);	// 设置观察矩阵即camera
	}

	// 初始化顶点坐标数据的方法（逆时针）
	// GL_LINE_STRIP绘制架构
	private void initVertexDataNewCCM() {
		// 顶点坐标数据的初始化================begin============================
		/**
		 * 原理见 http://www.tuicool.com/articles/Qjm6bmy
		 */
		ArrayList<Float> alVertix = new ArrayList<Float>();// 存放顶点坐标的ArrayList
		final int angleSpan = 1;// 将球进行单位切分的角度
		final int angleSpanH = 1;

		for (int vAngle = -90; vAngle <= 90; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
		{
			for (int hAngle = 0; hAngle < 360; hAngle = hAngle + angleSpanH)// 水平方向angleSpan度一份
			{ // 纵向横向各到一个角度后计算对应的此点在球面上的坐标
				float z0 = (float) (radius * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
						.toRadians(hAngle)));
				float x0 = (float) (radius * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
						.toRadians(hAngle)));
				float y0 = (float) (radius * UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				// 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
				alVertix.add(x0);
				alVertix.add(y0);
				alVertix.add(z0);
			}
		}
		int vCount = alVertix.size() / 3;// 顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

		// 将alVertix中的坐标值转存到一个float数组中
		verticesCoordinates = new float[vCount * 3];
		for (int i = 0; i < alVertix.size(); i++) {
			verticesCoordinates[i] = alVertix.get(i);
		}

		// Now build the index data
		ArrayList<Integer> alIndex = new ArrayList<Integer>();
		int row = (180 / angleSpan) + 1; // 球面切分的行数
		int col = 360 / angleSpanH; // 球面切分的列数
		for (int i = 0; i < row; i++) { // 对每一行循环
			if (i > 0 && i < row - 1) {

				for (int j = 0; j < col; j++) {
					// 中间行的两个相邻点与下一行的对应点构成三角形
					int k = i * col + j;

					alIndex.add(k - col);
					alIndex.add(k);
					alIndex.add(k - 1);

					alIndex.add(k); // 绘制框架添加
					alIndex.add(k - col); // 绘制框架添加
					alIndex.add(k - col + 1); // 绘制框架添加

				}

				// 中间行
				for (int j = 0; j < col; j++) {
					// 中间行的两个相邻点与上一行的对应点构成三角形
					int k = i * col + j;

					if (j == 0) {
						alIndex.add(k + col); // 绘制框架添加
						alIndex.add(k + col - 1); // 绘制框架添加
						alIndex.add(k); // 绘制框架添加

					} else {
						alIndex.add(k); // 绘制框架添加
						alIndex.add(k + col); // 绘制框架添加
						alIndex.add(k + col - 1); // 绘制框架添加
					}
					alIndex.add(k + col);
					alIndex.add(k);
					alIndex.add(k + 1);

				}
			}
		}

		// 将顶点导入数组
		indicesCoordinates = new short[alIndex.size()];
		for (int i = 0; i < alIndex.size(); i++) {
			indicesCoordinates[i] = alIndex.get(i).shortValue();
		}
	}

	/**
	 * 绘制球体
	 */
	public void draw() {
		// TODO Auto-generated method stub
		
		
		// 将半径尺寸传入shader程序
		GLES20.glUniform1f(radiusHandle, radius * UNIT_SIZE);
		
		// Draw the triangle facing straight on.
		Matrix.setIdentityM(mModelMatrix, 0);
		render(mModelMatrix);
	}
}
