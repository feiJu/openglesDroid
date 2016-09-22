package com.fenghun.openglesdroid.jni.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.R;
import com.fenghun.openglesdroid.jni.MyOpenglES;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Message;
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
 * @author fenghun
 * @date 2015-10-20
 */
public class GLES20SurfaceView extends GLSurfaceView implements Renderer {

	private static String TAG = "VideoGLES20SurfaceView";

	static Handler handler;

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
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				// case R.id.videoGLES20SurfaceView_refresh_UI:
				// requestRender(); // 重绘UI
				// break;
				default:
					break;
				}
			}
		};
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
//		Log.d(TAG,
//				"------- onSurfaceCreated(GL10 gl, EGLConfig config) is called!");
		MyOpenglES.onSurfaceCreated(640, 480);
//		// Set the background color to black ( rgba ).
//		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // OpenGL docs.
//
//		// Enable Smooth Shading, default not really needed.
//		gl.glShadeModel(GL10.GL_SMOOTH);// OpenGL docs.

		
		
//		// Depth buffer setup.
//		gl.glClearDepthf(1.0f);// OpenGL docs.
//
//		// Enables depth testing.
//		gl.glEnable(GL10.GL_DEPTH_TEST);// OpenGL docs.
//
//		// The type of depth testing to do.
//		gl.glDepthFunc(GL10.GL_LEQUAL);// OpenGL docs.
//
//		// Really nice perspective calculations.
//		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, // OpenGL docs.
//				GL10.GL_NICEST);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		Log.d(TAG,
				"----- onSurfaceChanged(GL10 gl, int width, int height) is called!");
		MyOpenglES.onSurfaceChanged(width, height);
//		// Sets the current view port to the new size.
//		gl.glViewport(0, 0, width, height);// OpenGL docs.
//
//		// Select the projection matrix
//		gl.glMatrixMode(GL10.GL_PROJECTION);// OpenGL docs.
//
//		// Reset the projection matrix
//		gl.glLoadIdentity();// OpenGL docs.
//
//		// Calculate the aspect ratio of the window
//		GLU.gluPerspective(gl, 45.0f,
//		(float) width / (float) height,
//		0.1f, 100.0f);
//
//		// Select the modelview matrix
//		gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.
//
//		// Reset the modelview matrix
//		gl.glLoadIdentity();// OpenGL docs.
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
//		Log.d(TAG, "------------- onDrawFrame(GL10 gl) is called!");
		// Clears the screen and depth buffer.
//		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | // OpenGL docs.
//				GL10.GL_DEPTH_BUFFER_BIT);
		MyOpenglES.onDrawFrame();
	}

	/**
	 * 请求渲染视频 Request render video
	 */
	// public static void drawVideoPlay() {
	// handler.sendEmptyMessage(R.id.videoGLES20SurfaceView_refresh_UI);
	// }

}