package com.fenghun.openglesdroid.jni.bean20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;

import com.fenghun.openglesdroid.jni.utils.ShaderUtil;
import com.fenghun.openglesdroid.jni.view.MySurfaceView;

public class Ball {

	int mProgram;// 自定义渲染管线着色器程序id
	int muMVPMatrixHandle;// 总变换矩阵引用
	int maPositionHandle; // 顶点位置属性引用
	int muRHandle;// 球的半径属性引用
	String mVertexShader;// 顶点着色器
	String mFragmentShader;// 片元着色器

	FloatBuffer mVertexBuffer;// 顶点坐标数据缓冲
	ShortBuffer ballIndexDataBuffer;	// 顶点绘制顺序数据缓冲
	int vCount = 0;	// 顶点个数
	int iCount=0;  	// 顶点顺序个数
	public float yAngle = 0;// 绕y轴旋转的角度
	public float xAngle = 0;// 绕x轴旋转的角度
	public float zAngle = 0;// 绕z轴旋转的角度
	float r = 0.8f;

	
	private short[] ballIndexData;
	
	
	public Ball(MySurfaceView mv) {
		// 初始化顶点坐标与着色数据
		//initVertexData();
		
		initVertexDataNewCCM();
		//initVertexDataNewCM();
		
		// 初始化shader
		initShader(mv);
	}

	// 初始化顶点坐标数据的方法,采用 drawarray方式，不优
	public void initVertexData() {
		// 顶点坐标数据的初始化================begin============================
		/**
		 * 原理见 http://www.tuicool.com/articles/Qjm6bmy
		 */
		ArrayList<Float> alVertix = new ArrayList<Float>();// 存放顶点坐标的ArrayList
		final int angleSpan = 90;// 将球进行单位切分的角度
		final int angleSpanH = 45;
		for (int vAngle = -90; vAngle < 90; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
		{
			for (int hAngle = 0; hAngle <= 360; hAngle = hAngle + angleSpanH)// 水平方向angleSpan度一份
			{// 纵向横向各到一个角度后计算对应的此点在球面上的坐标
				float x0 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
						.toRadians(hAngle)));
				float y0 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
						.toRadians(hAngle)));
				float z0 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				float x1 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
						.toRadians(hAngle + angleSpan)));
				float y1 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
						.toRadians(hAngle + angleSpan)));
				float z1 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				float x2 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle + angleSpan)) * Math
						.cos(Math.toRadians(hAngle + angleSpan)));
				float y2 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle + angleSpan)) * Math
						.sin(Math.toRadians(hAngle + angleSpan)));
				float z2 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle + angleSpan)));

				float x3 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle + angleSpan)) * Math
						.cos(Math.toRadians(hAngle)));
				float y3 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle + angleSpan)) * Math
						.sin(Math.toRadians(hAngle)));
				float z3 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle + angleSpan)));

				// 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
				alVertix.add(x1);
				alVertix.add(y1);
				alVertix.add(z1);

				alVertix.add(x3);
				alVertix.add(y3);
				alVertix.add(z3);

				alVertix.add(x0);
				alVertix.add(y0);
				alVertix.add(z0);

				alVertix.add(x1);
				alVertix.add(y1);
				alVertix.add(z1);

				alVertix.add(x2);
				alVertix.add(y2);
				alVertix.add(z2);

				alVertix.add(x3);
				alVertix.add(y3);
				alVertix.add(z3);
			}
		}
		vCount = alVertix.size() / 3;// 顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

		// 将alVertix中的坐标值转存到一个float数组中
		float vertices[] = new float[vCount * 3];
		for (int i = 0; i < alVertix.size(); i++) {
			vertices[i] = alVertix.get(i);
		}

		// 创建顶点坐标数据缓冲
		// vertices.length*4是因为一个整数四个字节
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
		mVertexBuffer = vbb.asFloatBuffer();// 转换为int型缓冲
		mVertexBuffer.put(vertices);// 向缓冲区中放入顶点坐标数据
		mVertexBuffer.position(0);// 设置缓冲区起始位置
		// 特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
		// 转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
	}


	
	// 初始化顶点坐标数据的方法（顺时针绘制）
	public void initVertexDataNewCM() {
		// 顶点坐标数据的初始化================begin============================
		/**
		 * 原理见 http://www.tuicool.com/articles/Qjm6bmy
		 */
		ArrayList<Float> alVertix = new ArrayList<Float>();// 存放顶点坐标的ArrayList
		final int angleSpan = 45;// 将球进行单位切分的角度
		final int angleSpanH = 45;
		
		for (int vAngle = -90; vAngle <= 90; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
		{
			for (int hAngle = 0; hAngle < 360; hAngle = hAngle + angleSpanH)// 水平方向angleSpan度一份
			{	// 纵向横向各到一个角度后计算对应的此点在球面上的坐标
				float z0 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
						.toRadians(hAngle)));
				float x0 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
						.toRadians(hAngle)));
				float y0 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				System.out.println("---------cood0 vAngle==" + vAngle + " x="
						+ x0 + ",y=" + y0 + ",z=" + z0);

				// 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
				alVertix.add(x0);
				alVertix.add(y0);
				alVertix.add(z0);
			}
		}
		vCount = alVertix.size() / 3;// 顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

		// 将alVertix中的坐标值转存到一个float数组中
		float vertices[] = new float[vCount * 3];
		for (int i = 0; i < alVertix.size(); i++) {
			vertices[i] = alVertix.get(i);
		}
		
		
		System.out.println("---------- vCount=="+vCount);
		// Now build the index data
		ArrayList<Integer> alIndex=new ArrayList<Integer>();  
        int row=(180/angleSpan)+1; //球面切分的行数  
        int col=360/angleSpanH;  //球面切分的列数  
        for (int i = 0; i < row; i++) {  //对每一行循环  
            if(i>0 && i<row-1){ 
            	
            	  //中间行  
                for (int j = -1; j < col; j++) {   
                    //中间行的两个相邻点与下一行的对应点构成三角形  
                    int k=i*col+j;
                    
                    alIndex.add(k+col);  
                    alIndex.add(k+1); 
                    alIndex.add(k);  
                }  
            	
            	 for (int j = 0; j < col+1; j++) {  
                     //中间行的两个相邻点与上一行的对应点构成三角形          
                     int k=i*col+j; 
                     
                     alIndex.add(k-col);
                     alIndex.add(k-1);
                     alIndex.add(k);
                 }  
            }  
        }  
        iCount=alIndex.size();  
        ballIndexData =new short[iCount];  
        for (int i = 0; i < iCount; i++) {
        	//System.out.print(alIndex.get(i)+",");
        	ballIndexData[i]= alIndex.get(i).shortValue(); 
        	
        }  
		//System.out.println("");
		// 创建顶点坐标数据缓冲
		// vertices.length*4是因为一个整数四个字节
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
		mVertexBuffer = vbb.asFloatBuffer();// 转换为int型缓冲
		mVertexBuffer.put(vertices);// 向缓冲区中放入顶点坐标数据
		mVertexBuffer.position(0);// 设置缓冲区起始位置
		// 特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
		// 转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
		
		ballIndexDataBuffer = ByteBuffer
				.allocateDirect(ballIndexData.length * 2).order(ByteOrder.nativeOrder())
				.asShortBuffer();
		ballIndexDataBuffer.put(ballIndexData).position(0);
	}
	
	
	
	// 初始化顶点坐标数据的方法（逆时针）
	// GL_LINE_STRIP绘制架构
	public void initVertexDataNewCCM() {
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
				float z0 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.cos(Math
						.toRadians(hAngle)));
				float x0 = (float) (r * Constant.UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle)) * Math.sin(Math
						.toRadians(hAngle)));
				float y0 = (float) (r * Constant.UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				System.out.println("---------cood0 vAngle==" + vAngle + " x="
						+ x0 + ",y=" + y0 + ",z=" + z0);

				// 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
				alVertix.add(x0);
				alVertix.add(y0);
				alVertix.add(z0);
			}
		}
		vCount = alVertix.size() / 3;// 顶点的数量为坐标值数量的1/3，因为一个顶点有3个坐标

		// 将alVertix中的坐标值转存到一个float数组中
		float vertices[] = new float[vCount * 3];
		for (int i = 0; i < alVertix.size(); i++) {
			vertices[i] = alVertix.get(i);
		}

		System.out.println("---------- vCount==" + vCount);
		// Now build the index data
		ArrayList<Integer> alIndex = new ArrayList<Integer>();
		int row = (180 / angleSpan) + 1; // 球面切分的行数
		int col = 360 / angleSpanH; // 球面切分的列数
		for (int i = 0; i < row; i++) { // 对每一行循环
			if (i > 0 && i < row - 1) {

				for (int j = 0; j < col; j++) {
					// 中间行的两个相邻点与下一行的对应点构成三角形
					int k = i * col + j;

					// if (j == 0) { // 第一个顺序不同一般情况
					// alIndex.add(k - 1);
					// alIndex.add(k - col);
					// alIndex.add(k);
					// } else {
					alIndex.add(k - col);
					alIndex.add(k);
					alIndex.add(k - 1);
					// }

					// if(j==(col - 1 )){//最后的一个三角形顺序不同一般的
					// alIndex.add(k - col + 1); // 绘制框架添加
					// alIndex.add(k); // 绘制框架添加
					// alIndex.add(k - col); // 绘制框架添加
					// }else{
					alIndex.add(k); // 绘制框架添加
					alIndex.add(k - col); // 绘制框架添加
					alIndex.add(k - col + 1); // 绘制框架添加
					// }

				}
//				int length1 = alIndex.size();
//				for (int tmp = 0; tmp < alIndex.size(); tmp++) {
//					System.out.print(alIndex.get(tmp) + ",");
//				}
//				System.out.println("");

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
//				for (int tmp = length1; tmp < alIndex.size(); tmp++) {
//					System.out.print(alIndex.get(tmp) + ",");
//				}
//				System.out.println("");
			}
		}
		iCount = alIndex.size();
		ballIndexData = new short[iCount];
		for (int i = 0; i < iCount; i++) {
			//System.out.print(alIndex.get(i) + ",");
			ballIndexData[i] = alIndex.get(i).shortValue();

		}
		//System.out.println("");
		// 创建顶点坐标数据缓冲
		// vertices.length*4是因为一个整数四个字节
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
		mVertexBuffer = vbb.asFloatBuffer();// 转换为int型缓冲
		mVertexBuffer.put(vertices);// 向缓冲区中放入顶点坐标数据
		mVertexBuffer.position(0);// 设置缓冲区起始位置
		// 特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
		// 转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题

		ballIndexDataBuffer = ByteBuffer
				.allocateDirect(ballIndexData.length * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		ballIndexDataBuffer.put(ballIndexData).position(0);
	}

	// 初始化shader
	public void initShader(MySurfaceView mv) {
		// 加载顶点着色器的脚本内容
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.shader",
				mv.getResources());
		// 加载片元着色器的脚本内容
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.shader",
				mv.getResources());
		// 基于顶点着色器与片元着色器创建程序
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// 获取程序中顶点位置属性引用
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// 获取程序中总变换矩阵引用
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		// 获取程序中球半径引用
		muRHandle = GLES20.glGetUniformLocation(mProgram, "uR");
	} 
	
	public void drawSelf() {

		MatrixState.rotate(xAngle, 1, 0, 0);// 绕X轴转动
		MatrixState.rotate(yAngle, 0, 1, 0);// 绕Y轴转动
		MatrixState.rotate(zAngle, 0, 0, 1);// 绕Z轴转动
		// 制定使用某套shader程序
		GLES20.glUseProgram(mProgram);
		// 将最终变换矩阵传入shader程序
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);
		// 将半径尺寸传入shader程序
		GLES20.glUniform1f(muRHandle, r * Constant.UNIT_SIZE);
		// 为画笔指定顶点位置数据
//		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
//				false, 3 * 4, mVertexBuffer);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, mVertexBuffer);
		// 允许顶点位置数据数组
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, ballIndexData.length, 
				GLES20.GL_UNSIGNED_SHORT, ballIndexDataBuffer);
		// 绘制球
		//GLES20.glDrawArrays(GLES20.GL_LINES, 0, vCount);
		//GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 8);
	}
}
