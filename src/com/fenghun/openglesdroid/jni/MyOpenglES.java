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
}
