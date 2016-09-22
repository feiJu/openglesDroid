package com.fenghun.openglesdroid.jni;

/**
 * 
 * @author Administrator
 *
 */
public class MyOpenglES {


	static {
		System.loadLibrary("myOpenglES");
	}
	
	/**
	 * 测试函数
	 */
	public static native void test(); 
	
	public static native void onSurfaceCreated(int width,int height);
	
	public static native void onSurfaceChanged(int width,int height);
	
	public static native void onDrawFrame();
	
}
