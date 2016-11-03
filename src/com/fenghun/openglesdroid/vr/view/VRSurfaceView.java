package com.fenghun.openglesdroid.vr.view;

import com.fenghun.openglesdroid.MainActivity;
import com.fenghun.openglesdroid.jni.bean20.ErrorHandler;
import com.fenghun.openglesdroid.vr.beans.Sphere;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;


public class VRSurfaceView extends GLSurfaceView implements ErrorHandler{

	private static String TAG = "VRSurfaceView";
	
	private MainActivity mainActivity;

	 // Offsets for touch events	 
    private float mPreviousX;
    private float mPreviousY;
    private float mDensity;	// 屏幕密度
    private float mDeltaX;
    private float mDeltaY;
    private VRRender vrRender;
	public VRSurfaceView(MainActivity mainActivity, float density) {
		// TODO Auto-generated constructor stub
		super(mainActivity);
		// TODO Auto-generated constructor stub
		this.mainActivity = mainActivity;
		mDensity = density;
		init();
	}

	/**
	 *  初始化底层Opengl es 与上层的连接
	 */
	private void init() {
		// TODO Auto-generated method stub
		Log.d(TAG, "------------ init() is called!");
		setEGLContextClientVersion(2);
		vrRender = new VRRender(mainActivity,this);
		setRenderer(vrRender);
		// setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // 设置为脏模式
	}
	
	@Override
	public void handleError(ErrorType errorType, String cause) {
		// TODO Auto-generated method stub
		Log.d(TAG, "errorType="+errorType.name()+","+cause);
	}
	
	/**
	 * 
	 * 重写触屏事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if (event != null)
		{			
			float x = event.getX();
			float y = event.getY();
			
			if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				if (vrRender != null)
				{
					float deltaX = (x - mPreviousX) / mDensity / 2f;
					float deltaY = (y - mPreviousY) / mDensity / 2f;
					
					mDeltaX += deltaX;
					mDeltaY += deltaY;
					
					
					vrRender.setDeltaX(mDeltaX);
					vrRender.setDeltaY(mDeltaY);
//					Sphere sv = vrRender.getSphereView();
//					if(sv!= null) sv.setCamera(mDeltaY);
					//System.out.println("------mDeltaX=="+mDeltaX+",mDeltaY="+mDeltaY);
				}
			}	
			
			mPreviousX = x;
			mPreviousY = y;
			
			return true;
		}
		else
		{
			return super.onTouchEvent(event);
		}		
	}

	public VRRender getVrRender() {
		return vrRender;
	}

	public void setVrRender(VRRender vrRender) {
		this.vrRender = vrRender;
	}
	
}
