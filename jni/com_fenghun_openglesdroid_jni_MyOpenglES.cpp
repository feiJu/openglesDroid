#include "com_fenghun_openglesdroid_jni_MyOpenglES.h"
#include "opengles20/render.h"

#include <android/log.h>
#include "android_log.h"
#ifndef LOG_TAG
#define LOG_TAG    "MyOpenglES.cpp"
#endif
/**
 * 测试函数
 *
 * Class:     com_fenghun_openglesdroid_jni_MyOpenglES
 * Method:    test
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_fenghun_openglesdroid_jni_MyOpenglES_test
  (JNIEnv *env, jclass thiz){
	LOGD("----------- test() is called! --------------");
}


/**
 * View 创建
 *
 * Class:     com_fenghun_openglesdroid_jni_MyOpenglES
 * Method:    onSurfaceCreated
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_fenghun_openglesdroid_jni_MyOpenglES_onSurfaceCreated
  (JNIEnv *env, jclass thiz, jint width, jint height){
	gl_initialize(width,height);
}

/**
 * View 翻转（横屏 竖屏）
 *
 * Class:     com_fenghun_openglesdroid_jni_MyOpenglES
 * Method:    onSurfaceChanged
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_fenghun_openglesdroid_jni_MyOpenglES_onSurfaceChanged
  (JNIEnv *env, jclass thiz, jint width, jint height){

	gl_view_changed(width,height);
}

/**
 * View 绘制
 *
 *
 * Class:     com_fenghun_openglesdroid_jni_MyOpenglES
 * Method:    onDrawFrame
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_fenghun_openglesdroid_jni_MyOpenglES_onDrawFrame
  (JNIEnv *env, jclass thiz){
	gl_drawFrame();
}
