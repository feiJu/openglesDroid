package com.fenghun.openglesdroid.vr.view;

import com.fenghun.openglesdroid.MainActivity;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler;

import android.opengl.GLSurfaceView;
import android.util.Log;


public class VRSurfaceView extends GLSurfaceView implements ErrorHandler{

	private static String TAG = "VRSurfaceView";
	
	private MainActivity mainActivity;

	public VRSurfaceView(MainActivity mainActivity, float density) {
		// TODO Auto-generated constructor stub
		super(mainActivity);
		// TODO Auto-generated constructor stub
		this.mainActivity = mainActivity;
		init();
	}

	/**
	 *  初始化底层Opengl es 与上层的连接
	 */
	private void init() {
		// TODO Auto-generated method stub
		Log.d(TAG, "------------ init() is called!");
		setEGLContextClientVersion(2);
		setRenderer(new VRRender(mainActivity,this));
		// setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // 设置为脏模式
	}
	
	@Override
	public void handleError(ErrorType errorType, String cause) {
		// TODO Auto-generated method stub
		Log.d(TAG, "errorType="+errorType.name()+","+cause);
	}
}
