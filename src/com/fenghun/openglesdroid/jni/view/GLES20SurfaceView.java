package com.fenghun.openglesdroid.jni.view;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.jni.bean20.Cube;
import com.fenghun.openglesdroid.jni.bean20.Point;
import com.fenghun.openglesdroid.jni.bean20.Square;
import com.fenghun.openglesdroid.jni.bean20.Triangle;
import com.fenghun.openglesdroid.jni.bean20.TriangleTest;
import com.fenghun.openglesdroid.jni.utils.GLES20Utils;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 支持OpenGLES2.0 使用OpenGLES2.0版本
 * 
 * 
 * Render的mode可以设为两种模式，一种是自动循环模式，也就是说GL线程以一
 * 定的时间间隔自动的循环调用用户实现的onDrawFrame（）方法进行一帧一帧的绘制， 还有一种的“脏”模式，也就是说当用户需要重绘的时候，主动
 * “拉”这个重绘过程，有点类似于Canvas中的invalidate（）具体的调用方法是在GLSurfaceView中 a.自动模式
 * .setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY); b."脏"模式
 * .setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
 * 当需要重绘时，调用GLSurfaceView.requestRender()
 * 一般情况下使用脏模式，这样可以有效降低cpu负载。测试结果表明，OpenGL真正绘图时一般会占到30%以上的cp
 * 
 * 1. opengl es 1.0 采用固定功能管线
 * 
 * 2. opengl es 2.0 采用可编程的功能管线，以可编程着色器为基础的，这意味着你绘制任何图形都必须有一
 * 个合适的着色器装载和绑定，比使用固定管线的桌面版本有更多代码。（允许提供编程来控制一些重要的工序，一些“繁琐”的工序比如栅格化等仍然是固定的。）
 * 
 * 
 * @author fenghun
 * @date 2015-10-20
 */
public class GLES20SurfaceView extends GLSurfaceView implements Renderer {

	private static String TAG = "GLES20SurfaceView";

	//private Triangle mTriangle;

	//private Square square;
	
	//private TriangleTest triangleTest;
	
	private Cube cube;
	
	private Point point;
	
	/** This is a handle to our per-vertex cube shading program. */
	private int mPerVertexProgramHandle;
	
	/** This is a handle to our light point program. */
	private int mPointProgramHandle; 
	
	/** This will be used to pass in the modelview matrix. */
	private int mMVMatrixHandle;
	
	/** This will be used to pass in the light position. */
	private int mLightPosHandle;
	
	/** This will be used to pass in model normal information. */
	private int mNormalHandle;
	
	/** 
	 * Stores a copy of the model matrix specifically for the light position.
	 */
	private float[] mLightModelMatrix = new float[16]; 
	
	/** Used to hold the current position of the light in world space (after transformation via model matrix). */
	private final float[] mLightPosInWorldSpace = new float[4];
	
	/** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
	 *  we multiply this by our transformation matrices. */
	private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
	
	/** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
	private final float[] mLightPosInEyeSpace = new float[4];
	
	/**
	 * Store the view matrix. This can be thought of as our camera. 
	 * This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];
	
	private int mProgram;
	
	

	private int mMVPMatrixHandle;
	
	private int mPositionHandle;

	private int mColorHandle;
	
	/** Store the projection matrix. This is used to project the scene onto a 2D viewport.
	 * 透视投影矩阵，用于投影场景到屏幕
	 *
	 */
	private float[] mProjectionMatrix = new float[16];

	 /**
	  * 模型矩阵
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];
    
    
    private float angleInDegrees = 0.0f;
    
	public GLES20SurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public GLES20SurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 初始化底层Opengl es 与上层的连接
	 */
	private void init() {
		// TODO Auto-generated method stub
		Log.d(TAG, "------------ init() is called!");
		setEGLContextClientVersion(2);
		setRenderer(this);
		// setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // 设置为脏模式

	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		// TODO Auto-generated method stub
		// MyOpenglES.onSurfaceCreated(640, 480);

		// 设置背景的颜色
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		
		// Position the eye behind the origin.
	    final float eyeX = 0.0f;
	    final float eyeY = 0.0f;
	    final float eyeZ = 1.5f;
	 
	    // We are looking toward the distance
	    final float lookX = 0.0f;
	    final float lookY = 0.0f;
	    final float lookZ = -5.0f;
	 
	    // Set our up vector. This is where our head would be pointing were we holding the camera.
	    final float upX = 0.0f;
	    final float upY = 1.0f;
	    final float upZ = 0.0f;
	 
	    // Set the view matrix. This matrix can be said to represent the camera position.
	    // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
	    // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
	    Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
		
		// 初始化一个三角形
//		mTriangle = new Triangle();
//		int vertexShader = mTriangle.getVertexShader();
//		int fragmentShader = mTriangle.getFragmentShader();
		
		// 初始化一个正方形
//		square = new Square();
//		int vertexShader = square.getVertexShader();
//		int fragmentShader = square.getFragmentShader();
		
	    // 
//	    triangleTest = new TriangleTest();
//	    String vertexShader = triangleTest.getVertexShader();
//	    String fragmentShader = triangleTest.getFragmentShader();
	    
	    // 初始化一个立方体
	    cube = new Cube();
//	    String vertexShader = cube.getVertexShader();
//	    String fragmentShader = cube.getFragmentShader();
	    String vertexShader = cube.getVertexShader_lightPerFragment();
	    String fragmentShader = cube.getFragmentShader_lightPerFragment();
	    point = new Point();	// 点光源
	    
	    int vertexShaderHandle = GLES20Utils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);

	    int fragmentShaderHandle = GLES20Utils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
	    
	    mPerVertexProgramHandle = GLES20Utils.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
	    		new String[] {"a_Position", "a_Color", "a_Normal"}); 
		
	    
	    if(point == null){	// 如果不用绘制点
	    	/**
			 * 一旦应用程序已经创建了顶点、片段着色器对象，它需要去创建项目对象，项目是最终的链接对象，
			 * 每个着色器在被绘制前都应该联系到项目或者项目对象。
			 * 
			 * 源码输入着色器对象，着色器对象被编辑为目标格式（.obj 文件）。完成后着色器对象能够链接到项目对象上，一个项目可以有多个着色器
			 * 
			 * OpenGL ES 中一个项目中有一个顶点着色器和一个片段着色器（不能多不能少），然后链接成可执行文件，最后能用来渲染。
			 * 
			 */
			mProgram = GLES20.glCreateProgram(); // 创建一个空的OpenGL ES Program

			GLES20.glAttachShader(mProgram, vertexShaderHandle); // 将顶点着色器添加到program
			GLES20.glAttachShader(mProgram, fragmentShaderHandle); // 将片段着色器添加到program

			GLES20.glLinkProgram(mProgram); // 链接项目
			// 检查项目链接错误
			int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if (linkStatus[0] != GLES20.GL_TRUE) {
				Log.e(TAG, "Could not link program: ");
				Log.e(TAG, GLES20.glGetProgramInfoLog(mProgram));
				GLES20.glDeleteProgram(mProgram);
				mProgram = 0;
			}
			
			/**
			 * 两个着色器被链接后，下一步应用设定顶点着色器 vPosition 属性
			 * // Bind vPosition to attribute 0
			 * glBindAttribLocation(programObject, 0, "vPosition");
			 * glBindAttribLocation 函数绑定 vPosition 属性到顶点着色器位置 0，当我们指定顶点数据后，位置指针指向下一个位置。
			 */
			
			 // Set program handles. These will later be used to pass in values to the program.
		    mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
		    if (mMVPMatrixHandle == -1) {
				throw new RuntimeException(
						"Could not get attrib location for mMVPMatrixHandle");
			}
		  
			// 获取指向vertex shader的成员vPosition的 handle,（这里直接获取的返回值为glBindAttribLocation中的第二个参数,
			//并完成glBindAttribLocation操作） 
			mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
			if (mPositionHandle == -1) {
				throw new RuntimeException(
						"Could not get attrib location for a_Position");
			}

			// 获取指向fragment shader的成员vColor的handle
			mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
			if (mColorHandle == -1) {
				throw new RuntimeException(
						"Could not get attrib location for a_Color");
			}
	    }else{
			final int pointVertexShaderHandle = GLES20Utils.loadShader(
					GLES20.GL_VERTEX_SHADER, point.getPointVertexShader());
			final int pointFragmentShaderHandle = GLES20Utils.loadShader(
					GLES20.GL_FRAGMENT_SHADER, point.getPointFragmentShader());
			mPointProgramHandle = GLES20Utils.createAndLinkProgram(
					pointVertexShaderHandle, pointFragmentShaderHandle,
					new String[] { "a_Position" });
		}
		
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// TODO Auto-generated method stub
		Log.d(TAG,
				"----- onSurfaceChanged(GL10 gl, int width, int height) is called!");
		 // Set the OpenGL viewport to the same size as the surface.
		// MyOpenglES.onSurfaceChanged(width, height);
		GLES20.glViewport(0, 0, width, height);
		
		// Create a new perspective projection matrix. The height will stay the same
	    // while the width will vary as per aspect ratio.
		// 创建一个透视投影矩阵，高度保持一致，宽度则按比例计算
	    final float ratio = (float) width / height;
	    final float left = -ratio;
	    final float right = ratio;
	    final float bottom = -1.0f;
	    final float top = 1.0f;
	    final float near = 1.0f;
	    final float far = 10.0f;
	 
	    Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		// TODO Auto-generated method stub
		// MyOpenglES.onDrawFrame();
		// 重绘背景色
		//GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
		//testTriangles(triangleTest);	
		testCubes(cube);
	}
	
	/**
	 * 测试绘制立方体
	 * 
	 * @param cube
	 */
	private void testCubes(Cube cube){
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			        
        
        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;        
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);                
        
        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mPerVertexProgramHandle);
        
        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix"); 
        mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
        mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal"); 
        
        /**
         * 光源实现的效果为在，以Z=-5为轴，半径为2个单位旋转
         * 实现步骤（旋转平移操作的代码步骤与矩阵相乘的计算顺序相反，矩阵相乘左乘）：
         * 1. 先将光源向z轴的正方向平移2个单位
         * 2. 再让光源绕Y轴旋转
         * 3. 再将光源向Z轴的负方向平移5个单位（相当于把旋转轴平移了5个单位）
         */
        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);	// 初始化光照模型矩阵为单位矩阵
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);   // 向z轴的负方向平移5个单位   
        Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);	// 绕Y轴旋转,旋转半径为2个单位
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);	// 再向z轴正方向平移2个单位
        
        /**
         * 模型自身的坐标系==》世界坐标系==》二维屏幕的3d坐标系
         */
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);//模型坐标转为世界坐标
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);  	// 世界坐标转为屏幕3D坐标
        
        // Draw some cubes.   
        /**
         * 绘制以自身X方向为轴旋转的立方体，位置在X轴正向4个单位处，Z轴负方向7个单位处
         */
        Matrix.setIdentityM(mModelMatrix, 0);	// 初始化立方体模型矩阵为单位矩阵
        Matrix.translateM(mModelMatrix, 0, 4.0f, 0.0f, -7.0f);	// 模型向X轴正方向平移4个单位，向Z轴负方向平移7个单位
		Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 0.0f, 0.0f); // 模型绕X轴旋转
		cube.drawCube(mPositionHandle, mColorHandle, mNormalHandle,
				mViewMatrix, mModelMatrix, mMVMatrixHandle, mProjectionMatrix,
				mMVPMatrixHandle, mLightPosHandle, mLightPosInEyeSpace);
                        
		/**
		 * 绘制以自身Y方向为轴旋转的立方体，位置在X轴负方向4个单位处，Z轴负方向7个单位处
		 */
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -4.0f, 0.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);        
		cube.drawCube(mPositionHandle, mColorHandle, mNormalHandle,
				mViewMatrix, mModelMatrix, mMVMatrixHandle, mProjectionMatrix,
				mMVPMatrixHandle, mLightPosHandle, mLightPosInEyeSpace);
        /**
         * 绘制以自身Z方向为轴旋转的立方体，位置在Y轴正向4个单位处，Z轴负方向7个单位处      
         */
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 4.0f, -7.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);        
		cube.drawCube(mPositionHandle, mColorHandle, mNormalHandle,
				mViewMatrix, mModelMatrix, mMVMatrixHandle, mProjectionMatrix,
				mMVPMatrixHandle, mLightPosHandle, mLightPosInEyeSpace);

		/**
		 * 绘制位置在Y轴负方向4个单位处，Z轴负方向7个单位处的立方体
		 */
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -4.0f, -7.0f);
		cube.drawCube(mPositionHandle, mColorHandle, mNormalHandle,
				mViewMatrix, mModelMatrix, mMVMatrixHandle, mProjectionMatrix,
				mMVPMatrixHandle, mLightPosHandle, mLightPosInEyeSpace);

		/**
		 * 以X=Y为轴，绕自身旋转，位置在Z轴负方向5个单位处的立方体
		 */
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 1.0f, 1.0f, 0.0f);        
		cube.drawCube(mPositionHandle, mColorHandle, mNormalHandle,
				mViewMatrix, mModelMatrix, mMVMatrixHandle, mProjectionMatrix,
				mMVPMatrixHandle, mLightPosHandle, mLightPosInEyeSpace);

		// 绘制点表示光源
        // Draw a point to indicate the light.
        GLES20.glUseProgram(mPointProgramHandle);        
		point.drawLight(cube.getmMVPMatrix(),mLightModelMatrix, mProjectionMatrix,
				mPointProgramHandle, mLightPosInModelSpace, mViewMatrix);

	}
	
	
	
	/**
	 * 
	 * 测试时绘制三角形
	 * 
	 * @param triangleTest
	 */
	private void testTriangles(TriangleTest triangleTest){
		/**
		 * OpenGL ES 内部存放图形数据的Buffer有COLOR ,DEPTH (深度信息）等， 在绘制图形之前一般需要清空COLOR 和
		 * DEPTH Buffer。
		 */
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		/**
		 * 使用 glUseProgram 调用项目句柄后，所有的以后的渲染将使用顶点着色器和片段着色器去联系项目对象
		 */
		GLES20.glUseProgram(mProgram);// 将program加入OpenGL ES环境中
		GLES20Utils.checkGlError(TAG, "glUseProgram");
		
		//mTriangle.draw(mProgram, mPositionHandle, mColorHandle); // 绘制三角形
		//square.draw(mProgram, mPositionHandle, mColorHandle); // 绘制三角形
	
		// Draw the triangle facing straight on.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        triangleTest.drawTriangle(triangleTest.getmTriangle1Vertices(), mPositionHandle, mColorHandle, 
        		mViewMatrix, mModelMatrix, mProjectionMatrix, mMVPMatrixHandle);
	
        
        // Draw one translated a bit down and rotated to be flat on the ground.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);        
        triangleTest.drawTriangle(triangleTest.getmTriangle2Vertices(), mPositionHandle, mColorHandle, 
        		mViewMatrix, mModelMatrix, mProjectionMatrix, mMVPMatrixHandle);
    	
        // Draw one translated a bit to the right and rotated to be facing to the left.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        triangleTest.drawTriangle(triangleTest.getmTriangle3Vertices(), mPositionHandle, mColorHandle, 
        		mViewMatrix, mModelMatrix, mProjectionMatrix, mMVPMatrixHandle);
    	
        angleInDegrees++;
        angleInDegrees = angleInDegrees % 360;
	}

	
	
	
}