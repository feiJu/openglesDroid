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

	private float radius = 1.0f; // 球体半径

	private float[] verticesCoordinates; // 顶点坐标

	private float[] verticesColors;	// 顶点颜色
	
	private float[] verTextureCoordinate;	// 顶点材质坐标
	
	// The order we like to connect them.
	private short[] indicesCoordinates; // 顶点坐标顺序
	
	/**
	 * 模型矩阵 Store the model matrix. This matrix is used to move models from
	 * object space (where each model can be thought of being located at the
	 * center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];
	
	//private int radiusHandle = -1;
	
	private final float[] mRotationMatrix = new float[16];	// 旋转矩阵
	/** Store the accumulated rotation. */
	private final float[] mAccumulatedRotation = new float[16];
	/** A temporary matrix. */
	private float[] mTemporaryMatrix = new float[16];
	private boolean isTouchScreenRotate = true;

	public Sphere(Context context, ErrorHandler errorHandler) {
		// TODO Auto-generated constructor stub
		initVertexDataNewCCM();	// 初始化顶点和顶点顺序
		try {
			setVerticesCoordinates(verticesCoordinates);	// 设置顶点坐标
			//setVerticesColors(verticesColors);
			setVerticesTextureCoordinates(verTextureCoordinate);
			setVerticesIndices(indicesCoordinates);	// 设置顶点顺序
			transformData2GPU();
		} catch (Exception e) {
			// TODO: handle exception
			errorHandler.handleError(ErrorType.BUFFER_CREATION_ERROR,
					e.getLocalizedMessage());
			e.printStackTrace();
		}
		initShader(context, R.raw.sphere_vertex,R.raw.sphere_frag,
				new String[] { ATTR_POSITION, ATTR_COLOR, ATTR_TEXTURE_COORDINATE });
//		radiusHandle = GLES20.glGetUniformLocation(mProgramHandle,
//				"u_radius");
//		GLES20.glUseProgram(mProgramHandle);
		
		//setLookAtM(0.0f,0.0f,1.0f,0.0f,0.0f,-5.0f,0.0f,1.0f,0.0f);	// 设置观察矩阵即camera
		setLookAtM(0, 0, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);	// 设置观察矩阵即camera
		Matrix.setIdentityM(mRotationMatrix, 0);	// 初始化为单位矩阵
	}

	// 初始化顶点坐标数据的方法（逆时针）
	// GL_LINE_STRIP绘制架构
	private void initVertexDataNewCCM() {
		// 顶点坐标数据的初始化================begin============================
		/**
		 * 原理见 http://www.tuicool.com/articles/Qjm6bmy
		 */
		ArrayList<Float> alVertix = new ArrayList<Float>();// 存放顶点坐标的ArrayList
		final int angleSpan = 2;// 将球进行单位切分的角度
		final int angleSpanH = 2;
		float x_tmp = 0.0f,y_tmp=0.0f,z_tmp=0.0f;
		for (int vAngle = -90; vAngle <= 90; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
		{
			for (int hAngle = 0; hAngle < 360; hAngle = hAngle + angleSpanH)// 水平方向angleSpan度一份
			{ 
				// 纵向横向各到一个角度后计算对应的此点在球面上的坐标
				float z0 = (float) (radius * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
						.toRadians(hAngle)));
				float x0 = (float) (radius * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
						.toRadians(hAngle)));
				float y0 = (float) (radius * UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				if(hAngle == 0){
					x_tmp = x0;
					y_tmp = y0;
					z_tmp = z0;
				}
				
				// 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
				alVertix.add(x0);
				alVertix.add(y0);
				alVertix.add(z0);
				
				// 纵向切分最后多添加一个点，弥补glDrawElements绘制贴图最后一个面绘制贴图坐标无法计算问题
				if(hAngle == (360 - angleSpanH)){	
					alVertix.add(x_tmp);
					alVertix.add(y_tmp);
					alVertix.add(z_tmp);
				}
			}
		}
		int vCount = alVertix.size() / 3;// 顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

		// 将alVertix中的坐标值转存到一个float数组中
		verticesCoordinates = new float[vCount * 3];
		for (int i = 0; i < alVertix.size(); i++) {
			verticesCoordinates[i] = alVertix.get(i);
		}

		verticesColors = new float[vCount*4];
		for(int i=0;i<verticesColors.length;i++){
			if(i%4 == 3){
				verticesColors[i] = 1.0f;
			}else{
				if(i%2 == 0){
					verticesColors[i] = 1.0f;
				}else{
					verticesColors[i] = 0.0f;
				}
				
			}
		}
		
		// Now build the index data
		ArrayList<Integer> alIndex = new ArrayList<Integer>();
		int row = (180 / angleSpan) + 1; // 球面切分的行数
		int col = 360 / angleSpanH +1; // 球面切分的列数，添加了最后一个点相当于多了一列
		for (int i = 0; i < row; i++) { // 对每一行循环
			if (i > 0 && i < row - 1) {

				if(i==1){
					for (int j = 0; j < col; j++) {
						// 中间行的两个相邻点与下一行的对应点构成三角形
						int k = i * col + j;

						alIndex.add(k - col);
						alIndex.add(k);
						alIndex.add(k - 1);

						if(j==col-1){
							alIndex.add(k - col); // 绘制框架添加
							alIndex.add(k - col + 1); // 绘制框架添加
							alIndex.add(k); // 绘制框架添加
						}else{
							alIndex.add(k); // 绘制框架添加
							alIndex.add(k - col); // 绘制框架添加
							alIndex.add(k - col + 1); // 绘制框架添加
						}
					}
				}
				

				// 中间行
				for (int j = 0; j < col; j++) {
					// 中间行的两个相邻点与上一行的对应点构成三角形
					int k = i * col + j;

					if (j == 0) {
						
						alIndex.add(k); // 绘制框架添加
						alIndex.add(k + col); // 绘制框架添加
						alIndex.add(k + col - 1); // 绘制框架添加
					} else {
						alIndex.add(k); // 绘制框架添加
						alIndex.add(k + col); // 绘制框架添加
						alIndex.add(k + col - 1); // 绘制框架添加
					}
				
					if(j == col-1){
						
						alIndex.add(k + col);
						alIndex.add(k);
						alIndex.add(k + 1);
					}else{
						alIndex.add(k + col);
						alIndex.add(k);
						alIndex.add(k + 1);
					}
				}
			}
		}

		// 将顶点导入数组
		indicesCoordinates = new short[alIndex.size()];
		for (int i = 0; i < alIndex.size(); i++) {
			indicesCoordinates[i] = alIndex.get(i).shortValue();
		}
		
//		for(int i=0;i<indicesCoordinates.length;i++){
//			System.out.print(indicesCoordinates[i]+",");
//		}
//		System.out.println("");
		
		ArrayList<Float> verTextArr = new ArrayList<Float>();// 存放顶点坐标的ArrayList
		
//		verTextureCoordinate = new float[]{
//				
//				0.0f,1.0f,
//				0.25f,1.0f,
//				0.5f,1.0f,
//				0.75f,1.0f,
//				1.0f,1.0f,
//				
//				
//				0.0f,1.0f,
//				0.25f,1.0f,
//				0.5f,1.0f,
//				0.75f,1.0f,
//				1.0f,1.0f,
//				
//				0.0f,0.0f,
//				0.25f,0.0f,
//				0.5f,0.0f,
//				0.75f,0.0f,
//				1.0f,0.0f
//		};
		
		verTextureCoordinate = new float[vCount*2];
		
		//int row_texture = (180 / angleSpan) + 1; // 球面切分的行数
		// 球面切分的列数，较顶点顺序少一列来算，弥补glDrawElements绘制贴图最后一个面绘制贴图坐标无法计算问题
		//int col_texture = 360 / angleSpanH +1;
		System.out.println("-------row ="+row+",col=="+col);
		float col_unit = 1.0f/(col-1);
		float row_unit = 1.0f/(row-1);
		for (int i = row-1; i >=0; i--) { // 对每一行循环
			
			for(int j=0; j <col; j++){	// 对每一列循环
				verTextArr.add(j * col_unit);
				verTextArr.add(i * row_unit);
			}
		}
		System.out.println("----- verTextArr  size=="+verTextArr.size());
		
//		for(int i=0;i<verTextArr.size();i++){
//			System.out.print(verTextArr.get(i)+"f,");
//		}
//		System.out.println("");
//		
		for(int i=0;i<verTextureCoordinate.length;i++){	
			verTextureCoordinate[i] = verTextArr.get(i);
		}
		System.out.println("texture ver num = "+verTextureCoordinate.length);
	}

	/**
	 * 绘制球体
	 * @param deltaX 
	 * @param deltaY 
	 */
	public void draw(float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		
		//GLES20.glFrontFace(GLES20.GL_CCW);	// 逆时针，观察球外贴图
		//GLES20.glFrontFace(GLES20.GL_CW);	// 逆时针，观察球内贴图
		
		// 将半径尺寸传入shader程序
		//GLES20.glUniform1f(radiusHandle, radius * UNIT_SIZE);
		
		// Draw the triangle facing straight on.
		Matrix.setIdentityM(mModelMatrix, 0);
		
//		if(isTouchScreenRotate){
//			Matrix.rotateM(mModelMatrix, 0, deltaX, 0.0f, 1.0f, 0.0f);	// 绕Y轴旋转
//			Matrix.rotateM(mModelMatrix, 0, deltaY, 1.0f, 0.0f, 0.0f);	// 绕X轴旋转
//		}else{
//			Matrix.multiplyMM(mModelMatrix, 0, mModelMatrix, 0, mRotationMatrix, 0);
//		}
		render(mModelMatrix);
	}

	public float[] getmRotationMatrix() {
		return mRotationMatrix;
	}

	public boolean isTouchScreenRotate() {
		return isTouchScreenRotate;
	}

	public void setTouchScreenRotate(boolean isTouchScreenRotate) {
		this.isTouchScreenRotate = isTouchScreenRotate;
	}
	
	public void setCamera(float test){
		System.out.println("------------ test=="+test);
		setLookAtM(0, 0, test, 0f, 0f, 0f, 0f, 1.0f, 0.0f);	// 设置观察矩阵即camera
	}
}
