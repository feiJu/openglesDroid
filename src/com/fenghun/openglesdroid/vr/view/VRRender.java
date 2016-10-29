package com.fenghun.openglesdroid.vr.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.fenghun.openglesdroid.MainActivity;
import com.fenghun.openglesdroid.jni.bean20.Constant;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler;
import com.fenghun.openglesdroid.vr.beans.Rectangle;
import com.fenghun.openglesdroid.vr.beans.Sphere;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

/**
 * 
 * VR 视图渲染
 * 
 * @author fenghun5987@gmail.com
 * @date 2016-10-20
 * @function
 */
public class VRRender implements Renderer {

	private static String TAG = "VRRender";
	
	private Rectangle rectView;	// 普通模式
	
	private Sphere sphereView;	// 

	private ErrorHandler errorHandler;

	private Context context;

	private MainActivity mainActivity;

	private float deltaX;
	private float deltaY;
	
	//
	public VRRender(MainActivity mainActivity, ErrorHandler errorHandler) {
		// TODO Auto-generated constructor stub
		Log.d(TAG, "VRRender(MainActivity mainActivity, ErrorHandler errorHandler) is called!");
		context = mainActivity;
		this.mainActivity = mainActivity;
		this.errorHandler = errorHandler;
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onSurfaceCreated(GL10 glUnused, EGLConfig config) is called!");
		// 设置背景的颜色
		GLES20.glClearColor(0.0f, 0.5f, 0.5f, 1.0f);

		// GLES20.glFrontFace(GLES20.GL_CCW);
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		// 初始化rect View
		//rectViewInit();
		// 初始化sphere View
		sphereViewInit();
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onSurfaceChanged(GL10 glUnused, int width, int height) is called!");
		//rectView.setProjectionMatrix(width, height);
		float ratio = (float) width / height;
		sphereView.setProjectionMatrix(width,height,-ratio, ratio, -1, 1, 20, 100);
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		// TODO Auto-generated method stub
		//drawCommmonView();	// 普通模式
		drawSphereView();	// 
	}

	/**
	 * 初始化rect View
	 */
	private void rectViewInit() {
		// TODO Auto-generated method stub
		rectView = new Rectangle(context,mainActivity.getScreenWidth(),
				mainActivity.getScreenHeight(), errorHandler);	// 初始化GPU缓存需要opengl es 的上下文信息
		
	}

	/**
	 * 初始化sphere View
	 */
	private void sphereViewInit() {
		// TODO Auto-generated method stub
		sphereView = new Sphere(context,errorHandler);
	}
	
	/**
	 * 绘制矩形普通模式view
	 */
	private void drawCommmonView() {

		rectView.draw();
	}
	
	/**
	 * 
	 */
	private void drawSphereView() {
		// TODO Auto-generated method stub
		sphereView.draw(deltaX,deltaY);
	}

	public float getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(float deltaX) {
		this.deltaX = deltaX;
	}

	public float getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(float deltaY) {
		this.deltaY = deltaY;
	}
}
