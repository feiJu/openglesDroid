package com.fenghun.openglesdroid.jni.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.jni.bean20.Triangle;
import com.fenghun.openglesdroid.jni.utils.GLES20Utils;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
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

	private Triangle mTriangle;

	/**
	 * OpenGL ES 2.0 中，在有效的顶点着色器和片段着色器被装载前，什么渲染都做不了。
	 * 我们介绍管线时介绍了顶点着色器和片段着色器，做任何渲染前，必须有顶点和片段着色器。
	 */
	// 顶点着色器，
	private final String vertexShaderCode = "attribute vec4 vPosition;" // 输入属性，4个成员矢量的vPosition
			+ "void main() {" // 主函数声明着色器宣布着色器开始执行
			+ "  gl_Position = vPosition;" + "}";

	// 片段着色器
	private final String fragmentShaderCode = "precision mediump float;" // 声明着色器默认的浮点变量精度
			+ "uniform vec4 vColor;"
			+ "void main() {"
			+ "  gl_FragColor = vColor;" // gl_FragColor是片段着色器最终的输出值
			+ "}";

	private int mProgram;

	private int mPositionHandle;

	private int mColorHandle;

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
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		// MyOpenglES.onSurfaceCreated(640, 480);

		// 设置背景的颜色
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

		// 初始化一个三角形
		mTriangle = new Triangle();

		int vertexShader = GLES20Utils.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode); // 加载顶点着色器
		int fragmentShader = GLES20Utils.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode); // 加载片段着色器

		/**
		 * 一旦应用程序已经创建了顶点、片段着色器对象，它需要去创建项目对象，项目是最终的链接对象，
		 * 每个着色器在被绘制前都应该联系到项目或者项目对象。
		 */
		mProgram = GLES20.glCreateProgram(); // 创建一个空的OpenGL ES Program

		GLES20.glAttachShader(mProgram, vertexShader); // 将顶点着色器添加到program
		GLES20.glAttachShader(mProgram, fragmentShader); // 将片段着色器添加到program

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
		
		
		// 获取指向vertex shader的成员vPosition的 handle,（这里直接获取的返回值为glBindAttribLocation中的第二个参数,
		//并完成glBindAttribLocation操作） 
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		if (mPositionHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for aPosition");
		}

		// 获取指向fragment shader的成员vColor的handle
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		if (mColorHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for color");
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		Log.d(TAG,
				"----- onSurfaceChanged(GL10 gl, int width, int height) is called!");
		// MyOpenglES.onSurfaceChanged(width, height);
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		// MyOpenglES.onDrawFrame();
		// 重绘背景色
		//GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
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
		
		mTriangle.draw(mProgram, mPositionHandle, mColorHandle); // 绘制三角形
	}
}