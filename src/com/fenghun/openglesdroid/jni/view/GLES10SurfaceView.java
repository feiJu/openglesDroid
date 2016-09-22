package com.fenghun.openglesdroid.jni.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.jni.MyOpenglES;
import com.fenghun.openglesdroid.jni.bean.Square;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 不支持OpenGLES2.0 使用OpenGLES1.0版本
 * @author fenghun
 * @date 2015-10-20
 */
public class GLES10SurfaceView extends GLSurfaceView implements Renderer{

	private static String TAG = "VideoGLES20SurfaceView";

	static Handler handler;

	// Initialize our square.
	Square square = new Square();
	
	
	public GLES10SurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public GLES10SurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 初始化底层Opengl es 与上层的连接
	 */
	private void init() {
		// TODO Auto-generated method stub
		Log.d(TAG, "------------ init() is called!");
		setEGLContextClientVersion(1);		// 学习 按教程的opengl 1.0 操作。
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
//		MyOpenglES.onSurfaceCreated(640, 480);
//		// Set the background color to black ( rgba ).
		gl.glClearColor(0.5f, 0.0f, 0.0f, 0.5f); // OpenGL docs.

		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);// OpenGL docs.

//		// Depth buffer setup.
		gl.glClearDepthf(1.0f);// OpenGL docs.

		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);// OpenGL docs.

		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);// OpenGL docs.

		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, // OpenGL docs.
				GL10.GL_NICEST);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		Log.d(TAG,
				"----- onSurfaceChanged(GL10 gl, int width, int height) is called!");
		MyOpenglES.onSurfaceChanged(width, height);
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);// OpenGL docs.

		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);// OpenGL docs.

		// Reset the projection matrix
		gl.glLoadIdentity();// OpenGL docs.

		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f,
		(float) width / (float) height,
		0.1f, 100.0f);

		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.

		// Reset the modelview matrix
		gl.glLoadIdentity();// OpenGL docs.
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
//		Log.d(TAG, "------------- onDrawFrame(GL10 gl) is called!");
//		MyOpenglES.onDrawFrame();
		// Clears the screen and depth buffer.// 清除屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | // OpenGL docs.
				GL10.GL_DEPTH_BUFFER_BIT);
		
		// Replace the current matrix with the identity matrix
		// 因为每次调用onDrawFrame 时，glTranslatef(0, 0, -4)每次都再向后移动4个单位，
		// 正方形迅速后移直至看不见,需要加上重置Matrix的代码。
		gl.glLoadIdentity();// 重置当前的模型观察矩阵  
		
		// Translates 4 units into the screen.
		// OpenGL ES从当前位置开始渲染，缺省坐标为(0,0,0)，和View port 的坐标一样，
		// 相当于把画面放在眼前，对应这种情况OpenGL不会渲染离view Port很近的画面，
		// 因此我们需要将画面向后退一点距离
		gl.glTranslatef(0, 0, -4);	// 平移变换，向z轴负方向移动4个单位
	
		
		// Draw our square.
		square.draw(gl);
		
		
		
	}

}
